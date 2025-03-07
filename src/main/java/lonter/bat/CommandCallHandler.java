package lonter.bat;

import jakarta.annotation.PostConstruct;

import lonter.bat.annotations.*;
import lonter.bat.annotations.help.*;
import lonter.bat.annotations.parameters.*;
import lonter.bat.annotations.rets.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public final class CommandCallHandler {
  @Value("${app.prefix}")
  private String prefix;

  @Value("${app.embedColor}")
  private String color;

  @Value("${app.groupId}")
  private String groupId;

  private final ApplicationContext applicationContext;

  private Set<Class<?>> implParam;
  private Set<Class<?>> atParam;
  private Set<Class<?>> commandClass;
  private Set<Class<?>> implRet;
  private Set<Class<?>> help;

  public CommandCallHandler(final @NotNull ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  public void init() {
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
        if(method.isAnnotationPresent(CommandCall.class))
          invokables.add(new Invokable(instance, method));
    });

    for(final var invokable: invokables) {
      final var method = invokable.method;
      final var value = method.getAnnotation(CommandCall.class).value();

      if(!command.equalsIgnoreCase(prefix + (value.isEmpty() ? method.getName() : value)))
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
          embed.setColor(Color.decode(color));
          channel.sendMessageEmbeds(embed.build()).queue();
        }
      }

      catch(final @NotNull Exception ex) {
        ex.printStackTrace();
      }

      return;
    }
  }

  private void help(final @NotNull MessageReceivedEvent e) {
    final var splitted = e.getMessage().getContentRaw().split(" ");

    final var categories = new HashSet<String>();
    final var helpAts = new HashMap<String, Help>();
    final var subcommands = new HashMap<String, Subcommand>();

    commandClass.forEach(at -> {
      for(final var method: at.getDeclaredMethods()) {
        if(method.isAnnotationPresent(Help.class)) {
          final var help = method.getAnnotation(Help.class);

          helpAts.put(method.getName(), help);
          categories.add(help.category());
        }

        else if(method.isAnnotationPresent(Subcommand.class))
          subcommands.put(method.getName(), method.getAnnotation(Subcommand.class));
      }
    });

    if(splitted.length == 1) {
      sendEmbed(e, categories, "Categories", "category");
      return;
    }

    final var value = splitted[1].toLowerCase().trim();

    if(categories.contains(value)) {
      final var commands = new HashSet<String>();

      helpAts.forEach((name, help) -> {
        if(!help.category().equalsIgnoreCase(value))
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

      var desc = "**Category:** " + help.category() + "\n**Usage:** " +
        prefix + name;

      if(!help.usage().isBlank())
        desc += " " + help.usage();

      desc += "\n\n\n**Description:**\n\n" + help.description() + (!subDesc.toString().isEmpty() ?
        "\n\n\n**Subcommands:**\n\n" + subDesc + "." : "");

      sendEmbed(e, toCamelCase(name), desc);
      found.set(true);
    });

    if(!found.get())
      e.getMessage().getChannel().sendMessage("No corresponding commands found.").queue();
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull HashSet<String> list,
                         final @NotNull String title, final @NotNull String object) {
    final var sorted = new ArrayList<>(list);
    Collections.sort(sorted);

    sendEmbed(e, title, "- **" + String.join("**;\n- **", sorted.stream()
      .map(CommandCallHandler::toCamelCase).toList()) + "**.",
      "To see more information about each " + object + " type `" + prefix + "help <" + object +  ">`.");
  }

  private void sendEmbed(final @NotNull MessageReceivedEvent e, final @NotNull String title,
                         final @NotNull String description, final @NotNull String footer) {
    final var embed = new EmbedBuilder();

    embed.setTitle(title);
    embed.setColor(Color.decode(color));
    embed.setDescription(description);
    embed.setFooter(footer);

    e.getMessage().getChannel().sendMessageEmbeds(embed.build()).queue();
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