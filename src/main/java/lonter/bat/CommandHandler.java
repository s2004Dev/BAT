package lonter.bat;

import jakarta.annotation.PostConstruct;

import lombok.val;

import lonter.bat.annotations.*;
import lonter.bat.annotations.help.*;
import lonter.bat.annotations.parameters.*;
import lonter.bat.annotations.rets.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This class is essential to make the library work correctly. You must call the `invoke()` function in your
 * bot handling class.
 */
@Component
public final class CommandHandler {
  @Value("${app.prefix:#{null}}")
  private String prefix;

  @Value("${app.embedColor:#{null}}")
  private String color;

  @Value("${app.groupId:#{null}}")
  private String groupId;

  private final ApplicationContext applicationContext;

  private final List<HelpInt> help;
  private final List<CommandArg> parameterHandlers;
  private final List<ReturnType> returnHandlers;

  private final Map<Class<? extends Annotation>, CommandArg> parameterInjections = new HashMap<>();
  private final Map<Class<? extends Annotation>, ReturnType> returnInjections = new HashMap<>();

  private Collection<Object> commandBeans;

  public CommandHandler(final @NotNull ApplicationContext applicationContext,
                        final @NotNull Optional<List<HelpInt>> help,
                        final @NotNull Optional<List<CommandArg>> parameterHandlers,
                        final @NotNull Optional<List<ReturnType>> returnHandlers) {
    this.applicationContext = applicationContext;
    this.help = help.orElse(Collections.emptyList());
    this.parameterHandlers = parameterHandlers.orElse(Collections.emptyList());
    this.returnHandlers = returnHandlers.orElse(Collections.emptyList());
  }

  @PostConstruct
  private void init() {
    if(prefix == null) {
      System.err.println("`prefix` cannot be null: please, set an app.prefix value in your property file.");
      System.exit(-1);
    }

    if(groupId == null) {
      System.err.println("`groupId` cannot be null: please, set an app.groupId value in your property file.");
      System.exit(-1);
    }

    this.commandBeans = applicationContext.getBeansWithAnnotation(CommandClass.class).values();

    for(val handler: this.parameterHandlers)
      parameterInjections.put(handler.getAnnotationType(), handler);

    for(val handler: this.returnHandlers)
      returnInjections.put(handler.getAnnotationType(), handler);

    if(help.size() > 1)
      System.err.println("Multiple help implementations are not allowed, a random one will be used.");
  }

  /**
   * Call this function in the MessageReceivedEvent function of your Discord bot.
   * @param e the Discord MessageReceivedEvent
   */
  public void invoke(final @NotNull MessageReceivedEvent e) {
    val input = e.getMessage().getContentRaw();
    val command = input.split(" ")[0];

    if(!command.startsWith(prefix))
      return;

    if(command.equalsIgnoreCase(prefix + "help")) {
      if(!help.isEmpty()) {
        help.getFirst().help(e);
        return;
      }

      help(e);
      return;
    }

    for(val bean: commandBeans) {
      for(val method: bean.getClass().getDeclaredMethods()) {
        if(!method.isAnnotationPresent(Command.class))
          continue;

        val commandAt = method.getAnnotation(Command.class);

        if(!command.equalsIgnoreCase(prefix + (commandAt.value().isEmpty() ? method.getName() : commandAt.value())) &&
          Arrays.stream(commandAt.aliases()).noneMatch(alias -> command.equalsIgnoreCase(prefix + alias)))
          continue;

        try {
          handleReturnValue(e, method, method.invoke(bean, prepareArguments(e, method)));
        }

        catch(final @NotNull Exception ex) {
          if(ex instanceof IllegalArgumentException)
            System.err.println(method.getName() + " has an illegal argument type.");

          else
            ex.printStackTrace();
        }

        return;
      }
    }
  }

  private Object @NotNull[] prepareArguments(final @NotNull MessageReceivedEvent e, final @NotNull Method method) {
    val parameters = method.getParameters();
    val args = new Object[parameters.length];

    for(var i = 0; i < parameters.length; i++) {
      val parameter = parameters[i];

      if(parameter.getAnnotations().length == 0)
        continue;

      val annotation = parameter.getAnnotations()[0];
      val handler = parameterInjections.get(annotation.annotationType());

      if(handler != null)
        args[i] = handler.value(e, annotation);
    }

    return args;
  }

  private void handleReturnValue(final @NotNull MessageReceivedEvent e, final @NotNull Method method,
                                 final @Nullable Object output) {
    if(output == null)
      return;

    val returnAnnotations = method.getAnnotatedReturnType().getAnnotations();

    if(returnAnnotations.length > 0) {
      val annotation = returnAnnotations[0];
      val handler = returnInjections.get(annotation.annotationType());

      if(handler != null) {
        handler.action(e, output, annotation);
        return;
      }
    }

    if(output instanceof String message)
      e.getChannel().sendMessage(message).queue();

    else if(output instanceof EmbedBuilder embed) {
      if(embed.build().getColor() == null && color != null) {
        try {
          embed.setColor(Color.decode(color));
        }

        catch(final @NotNull Exception ignored) { }
      }

      e.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
  }

  private void help(final @NotNull MessageReceivedEvent e) {
    val splitted = e.getMessage().getContentRaw().split(" ");

    val categories = new HashSet<String>();
    val helpAts = new HashMap<ArrayList<String>, Help>();
    val helpCategories = new HashMap<Help, String>();
    val subcommands = new HashMap<String, Subcommand>();

    commandBeans.forEach(bean -> {
      val at = bean.getClass();

      for(val method: at.getDeclaredMethods()) {
        if(method.isAnnotationPresent(Command.class) &&
          method.isAnnotationPresent(Help.class)) {
          val help = method.getAnnotation(Help.class);
          val command = method.getAnnotation(Command.class);

          val names = new ArrayList<>(List.of(command.value().isBlank() ? method.getName() :
            command.value()));

          names.addAll(Arrays.stream(command.aliases()).toList());

          helpAts.put(names, help);

          var category = help.category().isBlank() ?
            at.getAnnotation(CommandClass.class).value() : help.category();

          category = category.isBlank() ? at.getSimpleName().toLowerCase() : category.toLowerCase();

          categories.add(category);
          helpCategories.put(help, category);
        }

        for(val subcommand: method.getAnnotationsByType(Subcommand.class))
          subcommands.put(safe(subcommand.name().isBlank() ? method.getName() : subcommand.name()) + "/" +
            safe(subcommand.parent().isBlank() ? method.getName() : subcommand.parent()), subcommand);
      }
    });

    if(helpAts.isEmpty()) {
      e.getChannel().sendMessage("No commands registered.").queue();
      return;
    }

    if(splitted.length == 1 && categories.size() > 1) {
      sendEmbed(e, categories, "Categories", "category");
      return;
    }

    val value = (splitted.length == 1 && categories.size() == 1) ? categories.iterator().next() :
      splitted[1].toLowerCase().trim();

    if(categories.contains(value)) {
      val commands = new HashSet<String>();

      helpAts.forEach((name, help) -> {
        if(!helpCategories.get(help).equalsIgnoreCase(value))
          return;

        commands.add(name.getFirst());
      });

      sendEmbed(e, commands, toCamelCase(value), "command");

      return;
    }

    val found = new AtomicBoolean(false);

    helpAts.forEach((names, help) -> {
      if(names.stream().noneMatch(s -> s.equalsIgnoreCase(value)))
        return;

      val subDesc = new StringBuilder();
      val sorted = new TreeMap<>(subcommands);

      sorted.forEach((subName, subcommand) -> {
        if(names.stream().noneMatch(name -> (subcommand.parent().isBlank() ? subName : safe(subcommand.parent()))
          .contains(safe(name))))
          return;

        if(!subDesc.toString().isEmpty())
          subDesc.append("\n");

        subDesc.append("- **").append(prefix).append(names.getFirst()).append(" ").append(unsafe(subName
          .substring(0, subName.indexOf("/"))));

        if(subcommand.aliases().length > 0)
          subDesc.append(" (").append(String.join(", ", subcommand.aliases())).append(")");

        if(!subcommand.usage().isBlank())
          subDesc.append(" ").append(subcommand.usage());

        subDesc.append("** - ")
          .append(subcommand.description());
      });

      val aliases = names.subList(1, names.size());

      var desc = (!aliases.isEmpty() ? "**Aliases:** " + String.join(", ", aliases) : "") +
        "\n**Category:** " + toCamelCase(helpCategories.get(help)) + "\n**Usage:** " + prefix + names.getFirst();

      if(!help.usage().isBlank())
        desc += " " + help.usage();

      desc += "\n\n\n**Description:**\n\n" + help.description() + (!subDesc.toString().isEmpty() ?
        "\n\n\n**Subcommands:**\n\n" + subDesc : "");

      sendEmbed(e, toCamelCase(names.getFirst()), desc);
      found.set(true);
    });

    if(!found.get())
      e.getMessage().getChannel().sendMessage("No corresponding commands found.").queue();
  }

  private static @NotNull String safe(final @NotNull String input) {
    return Base64.getUrlEncoder().encodeToString(input.getBytes());
  }

  private static @NotNull String unsafe(final @NotNull String input) {
    return new String(Base64.getUrlDecoder().decode(input));
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull String title,
                         final @NotNull String description, final @NotNull String footer) {
    val embed = new EmbedBuilder();

    embed.setTitle(title);

    try {
      embed.setColor(Color.decode(color));
    }

    catch(final @NotNull Exception _) { }

    embed.setDescription(description);
    embed.setFooter(footer);

    e.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull HashSet<String> list,
                         final @NotNull String title, final @NotNull String object) {
    val sorted = new ArrayList<>(list);
    Collections.sort(sorted);

    sendEmbed(e, title, "- **" + String.join("**;\n- **", sorted.stream()
      .map(CommandHandler::toCamelCase).toList()) + "**.", "To see more information about each " + object +
      " type `" + prefix + "help <" + object +  ">`.");
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull String title,
                         final @NotNull String description) {
    sendEmbed(e, title, description, "");
  }

  private static String toCamelCase(final @NotNull String input) {
    return input.isBlank() ? input : Arrays.stream(input.split("\\s+")).map(word ->
      word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
  }
}