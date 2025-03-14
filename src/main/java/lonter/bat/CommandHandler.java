package lonter.bat;

import jakarta.annotation.PostConstruct;

import lonter.bat.annotations.*;
import lonter.bat.annotations.help.*;
import lonter.bat.annotations.parameters.*;
import lonter.bat.annotations.rets.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.reflections.Reflections;

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

  private Set<Class<?>> implParam;
  private Set<Class<?>> atParam;
  private Set<Class<?>> commandClass;
  private Set<Class<?>> implRet;
  private Set<Class<?>> help;

  private CommandHandler(final @NotNull ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
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

    final var scope = new Reflections(groupId);

    implParam = scope.getTypesAnnotatedWith(ImplParam.class);
    atParam = scope.getTypesAnnotatedWith(AtParam.class);
    commandClass = scope.getTypesAnnotatedWith(CommandClass.class);
    implRet = scope.getTypesAnnotatedWith(ImplRet.class);
    help = scope.getTypesAnnotatedWith(HelpImpl.class);

    if(help.size() > 1)
      System.err.println("Multiple help implementation are not allowed, random one will be used.");
  }

  /**
   * Call this function in the MessageReceivedEvent function of your Discord bot.
   * @param e the Discord MessageReceivedEvent
   */
  public void invoke(final @NotNull MessageReceivedEvent e) {
    final var input = e.getMessage().getContentRaw();
    final var command = input.split(" ")[0];

    if(!command.startsWith(prefix))
      return;

    if(command.equalsIgnoreCase(prefix + "help")) {
      if(!help.isEmpty()) {
        final var testImpl = help.iterator().next();

        if(HelpInt.class.isAssignableFrom(testImpl)) {
          try {
            ((HelpInt) testImpl.getDeclaredConstructor().newInstance()).help(e);
            return;
          }

          catch(final @NotNull Exception ex) {
            ex.printStackTrace();
          }
        }

        System.err.println("The help class is not implementing HelpInt.");
      }

      help(e);
      return;
    }

    final var invokables = new ArrayList<Invokable>();
    final var injections = new HashMap<Class<? extends Annotation>, CommandArg>();

    implParam.forEach(impl -> {
      if(!CommandArg.class.isAssignableFrom(impl))
        return;

      try {
        final var instance = impl.getDeclaredConstructor().newInstance();

        final var commandArg = (CommandArg) instance;

        atParam.forEach(at -> {
          if(at.getSimpleName().equals(impl.getSimpleName())) // noinspection unchecked
            injections.put((Class<? extends Annotation>) at, commandArg);
        });
      }

      catch(final @NotNull Exception ex) {
        ex.printStackTrace();
      }
    });

    commandClass.forEach(i -> {
      final var instance = applicationContext.getBean(i);

      for(final var method: i.getDeclaredMethods())
        if(method.isAnnotationPresent(Command.class))
          invokables.add(new Invokable(instance, method));
    });

    for(final var invokable: invokables) {
      final var method = invokable.method;
      final var commandAt = method.getAnnotation(Command.class);
      final var value = commandAt.value();

      if(!command.equalsIgnoreCase(prefix + (value.isEmpty() ? method.getName() : value)) &&
         Arrays.stream(commandAt.aliases()).noneMatch(alias ->
           command.equalsIgnoreCase(prefix + alias)))
        continue;

      final var parameters = method.getParameters();
      final var args = new Object[parameters.length];

      for(int i = 0; i < parameters.length; i++) {
        final var annotation = parameters[i].getAnnotations()[0];
        final var atType = annotation.annotationType();

        if(!injections.containsKey(atType))
          continue;

        args[i] = injections.get(atType).value(e, annotation);
      }

      try {
        final var output = method.invoke(invokable.parent, args);
        final var ats = method.getAnnotatedReturnType().getAnnotations();

        var success = false;

        if(ats.length > 0) {
          final var at = ats[0];

          for(final var impl: implRet) {
            if(!ReturnType.class.isAssignableFrom(impl) ||
              !at.annotationType().getSimpleName().equals(impl.getSimpleName()))
              continue;

            ((ReturnType) impl.getDeclaredConstructor().newInstance()).action(e, output, at);
            success = true;

            break;
          }
        }

        if(success)
          return;

        final var channel = e.getChannel();

        if(output instanceof String message)
          channel.sendMessage(message).queue();

        else if(output instanceof EmbedBuilder embed) {
          try {
            embed.setColor(Color.decode(color));
          } catch(final @NotNull Exception _) { }

          channel.sendMessageEmbeds(embed.build()).queue();
        }
      }

      catch(final @NotNull Exception ex) {
        if(ex instanceof IllegalArgumentException)
          System.err.println(invokable.method.getName() + " has an illegal argument type.");

        else
          ex.printStackTrace();
      }
    }
  }

  private void help(final @NotNull MessageReceivedEvent e) {
    final var splitted = e.getMessage().getContentRaw().split(" ");

    final var categories = new HashSet<String>();
    final var helpAts = new HashMap<String, Help>();
    final var helpCategories = new HashMap<Help, String>();
    final var subcommands = new HashMap<String, Subcommand>();

    commandClass.forEach(at -> {
      for(final var method: at.getDeclaredMethods()) {
        if(method.isAnnotationPresent(Command.class) &&
           method.isAnnotationPresent(Help.class)) {
          final var help = method.getAnnotation(Help.class);
          final var command = method.getAnnotation(Command.class);

          helpAts.put(command.value().isBlank() ? method.getName() : command.value(), help);

          var category = help.category().isBlank() ?
            at.getAnnotation(CommandClass.class).value() : help.category();

          category = category.isBlank() ? at.getSimpleName().toLowerCase() : category.toLowerCase();

          categories.add(category);
          helpCategories.put(help, category);
        }

        else if(method.isAnnotationPresent(Subcommand.class)) {
          final var subcommand = method.getAnnotation(Subcommand.class);
          subcommands.put(subcommand.name().isBlank() ? method.getName() : subcommand.name(), subcommand);
        }
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

    final var value = (splitted.length == 1 && categories.size() == 1) ? categories.iterator().next() :
      splitted[1].toLowerCase().trim();

    if(categories.contains(value)) {
      final var commands = new HashSet<String>();

      helpAts.forEach((name, help) -> {
        if(!helpCategories.get(help).equalsIgnoreCase(value))
          return;

        commands.add(name);
      });

      sendEmbed(e, commands, toCamelCase(value), "command");

      return;
    }

    final var found = new AtomicBoolean(false);

    helpAts.forEach((name, help) -> {
      if(!name.equalsIgnoreCase(value))
        return;

      final var subDesc = new StringBuilder();
      final var sorted = new TreeMap<>(subcommands);

      sorted.forEach((subName, subcommand) -> {
        if(!subcommand.parent().equalsIgnoreCase(name))
          return;

        if(!subDesc.toString().isEmpty())
          subDesc.append(";\n");

        subDesc.append("- **").append(prefix).append(name).append(" ").append(subName);

        if(!subcommand.usage().isBlank())
          subDesc.append(" ").append(subcommand.usage());

        subDesc.append("** - ")
          .append(subcommand.description());
      });

      var desc = "**Category:** " + toCamelCase(helpCategories.get(help)) + "\n**Usage:** " +
        prefix + name;

      if(!help.usage().isBlank())
        desc += " " + help.usage();

      desc += "\n\n\n**Description:**\n\n" + help.description() + (!subDesc.toString().isEmpty() ?
        "\n\n\n**Subcommands:**\n\n" + subDesc : "");

      sendEmbed(e, toCamelCase(name), desc);
      found.set(true);
    });

    if(!found.get())
      e.getMessage().getChannel().sendMessage("No corresponding commands found.").queue();
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull String title,
                         final @NotNull String description, final @NotNull String footer) {
    final var embed = new EmbedBuilder();

    embed.setTitle(title);

    try {
      embed.setColor(Color.decode(color));
    } catch(final @NotNull Exception _) { }

    embed.setDescription(description);
    embed.setFooter(footer);

    e.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull HashSet<String> list,
                         final @NotNull String title, final @NotNull String object) {
    final var sorted = new ArrayList<>(list);
    Collections.sort(sorted);

    sendEmbed(e, title, "- **" + String.join("**;\n- **", sorted.stream()
      .map(CommandHandler::toCamelCase).toList()) + "**.",
      "To see more information about each " + object + " type `" + prefix + "help <" + object +  ">`.");
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull String title,
                         final @NotNull String description) {
    sendEmbed(e, title, description, "");
  }

  private static String toCamelCase(final @NotNull String input) {
    if(input.isBlank())
      return input;

    return Arrays.stream(input.split("\\s+")).map(word ->
      word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
      .collect(Collectors.joining(" "));
  }

  private record Invokable(Object parent, Method method) { }
}