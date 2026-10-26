# The BAT framework

#### by thePirateWeasel#2311 on FluxFur

** **

The BAT framework will allow you to easily make bots in Java; at the current version, Discord and FluxFur are
supported by default.  
BAT uses [JDA](https://github.com/discord-jda/JDA) and [JFA](https://github.com/s2004Dev/Java-FluxFur-API).

** **

With the **BAT** framework you can **bet** your **bot** will run everywhere in just a **bit**...
**but** it's still a work in progress.

** **

## Getting started

### Dependencies

First of all, this framework currently uses Java 25. In the future I might make support for older versions.  
As said in the introduction, this framework uses JDA and JFA, but also Spring Boot 4.  
Besides these dependencies you will also need to install Lombok and JetBrains' annotations.  

For more information and versions, look at the `pom.xml` file.

### How to install BAT

If you want to use JFA inside your Maven project as a dependency, you need to paste the following markup inside your
`pom.xml`:

```xml
<dependency>
  <groupId> it.lonter </groupId>
  <artifactId> bat </artifactId>
  <version> 0.1.0 </version>
</dependency>
```

If you want to compile it from the source you can by pasting these commands inside your terminal emulator (you'll
need to have [GIT](https://git-scm.com/) and [Maven](https://maven.apache.org/) installed).

```bash
git clone https://github.com/s2004Dev/BAT.git
cd ./BAT
mvn clean install
```

### How to use BAT

To use this framework follow these instructions.

- In the main class, you will need to perform a component scan of the BAT and your own packages in order to let the
framework know where to find the classes and methods. You will need to do it like in the example:

```java
@SpringBootApplication

@ComponentScan(basePackages = {
  "your.application.package",
  "it.lonter.bat"
})

public class YourBot {
  static void main(final String @NotNull[] args) {
    SpringApplication.run(YourBot.class, args);
  }
}
```

- In the bot initialization, you will need to inject the respective `BotListener` component in the same class (for
example, for Discord, a DiscordBotListener), and pass it as an argument for the BotListener class.
- In your property file you will need to define two properties:
  - `app.prefix`: It must contain the prefix of your bot.
  - `app.groupId`: It must contain the value `@project.groupId@` or just your groupId.
- Now, to make your own commands you will need to make a class (its name will define the category of the commands
inside), annotate it with the `@CommandClass` annotation and then make your own methods. The methods will be used as
commands, though only if annotated with the `@Command` annotation (their name will also be the command name), the
return value will be what the bot will send as an output.
- See the final command by typing on Discord, in a channel the bot has access to, the prefix + the name of the
command.

> [!tip]
> You can clone the [BAT-Starter](https://github.com/s2004Dev/BAT-Starter) repo to have all of this set up for you
already (plus a little extra).

> [!tip]
> You can look up at [BuiBot](https://github.com/s2004Dev/BAT-BuiBot)'s code, a real example of a bot made with the
BAT framework.

## BAT concepts

### What's a category?

To easily sort commands for the end user, the framework uses categories.  
A category is just a list of commands that is used for the `help` command.

### Help command

The BAT framework allows you to easily document your commands, in order to do so, you just need to annotate your
method with `@Help`, it will then be registered in the `help` command. The user can access your bot documentation by
typing prefix + `help`.

By typing just the command without any argument, the bot will send an embed containing a list of categories. The user
will then have to type prefix + `help ` + `<category name>` to see all the possible commands in the category. If
the user then types prefix + `help ` + `<command name>` the bot will send the description set in the `@Help`
annotation for that command.

#### Define your own help command

In order to define your own help command, you need to make a class implementing the `HelpInt` interface and its
method, and annotate it with the `@HelpImpl` annotation.

```java
@HelpImpl
public final class MyHelpClass implements HelpInt {
  public void help(final @NotNull BatMRE e) {
    // Your implementation
  }
}
```

### Injectable parameters

Inside your commands you can inject values to perform specific tasks, for example the arguments of the command itself,
what the user typed in the message, or the message event itself.

> [!warning]
> **The annotation must be the first one of the parameter.**
> 
> **Correct:**  
> ```java
> void command(final @Args @SomethingElse String[] args) { }
> ```
> 
> **Incorrect:**  
> ```java
> void command(final @SomethingElse @Args String[] args) { }
> ```

#### Define your own injectable parameter

In order to define your own injectable parameter you will need to do two things:  

1. Define the `@AtParam`

Wherever you want in your codebase, you can define an `@interface` class that must be annotated with the `@AtParam`
annotation and the ordinary Java Annotation boilerplate, just like in the example:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) @AtParam
public @interface MyParam {
  // You can add your values in here
}
```

2. Define the `@ImplParam`

Wherever you want in your codebase, you can define the actual parameter logic; in order to do so, you will need to
define a class that must be annotated with the `@ImplParam` and `@Component` annotations, then, you will have to
extend the `CommandArg` class and implement its abstract methods, like shown in the example:

```java
@ImplParam @Component
public final class MyParam extends CommandArg {
  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    // IMPORTANT: This must be your @AtParam, not the @ImplParam
    return your.application.package_.MyParam.class;
  }

  @Override public @NotNull Object value(final @NotNull BatMRE e, final @NotNull Annotation at) {
    if(!(at instanceof your.application.package_.MyParam myParam)) {
      log.error("An error occurred while injecting @MyParam.");
      return new String[] { };
    }

    // Your implementation
  }
}
```

> [!warning]
> Both the annotation class and the implementation one must share the same name.

### Default return types

When defining a command, you can specify a return type, here are the allowed ones.

#### void

The bot will not return anything, but you are allowed to perform actions anyway.

#### String

The bot will just send a normal message containing the returned text.

#### BatEmbed

The bot will send an embedded message.

### Annotated Return types

Inside your commands you can tell the bot to perform specific actions before returning the value.

> [!warning]
> **The annotation must be the first one of the parameter.**
>
> **Correct:**
> ```java
> @Reply @SomethingElse String command() { }
> ```
>
> **Incorrect:**
> ```java
> @SomethingElse @Reply String command() { }
> ```

#### Define your own return type

> [!warning]
> **THIS SECTION WILL BE REWORKED**.

In order to define your own return type you will need to do two things:

1. Define the `@AtRet`

Wherever you want in your codebase, you can define an `@interface` class that must be annotated with the `@AtRet`
annotation and the ordinary Java Annotation boilerplate, just like in the example:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE) @AtRet
public @interface MyRetParam {
  // You can add your values in here
}
```

2. Define the `@ImplRet`

Wherever you want in your codebase, you can define the actual return type logic; in order to do so, you will need to
define a class that must be annotated with the `@ImplRet` and `@Component` annotations, then, you will have to
extend the `ReturnType` class and implement its abstract methods, like shown in the example:

```java
@ImplRet @Component
public final class MyRetParam extends ReturnType {
  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    // IMPORTANT: This must be your @AtRet, not the @ImplRet
    return your.application.package_.MyRetParam.class;
  }

  @Override public void action(final @NotNull BatMRE e, final @NotNull Annotation at) {
    if(!(at instanceof your.application.package_.MyRetParam myRetParam)) {
      log.error("An error occurred while injecting @MyRetParam.");
      return;
    }

    // Your implementation
  }
}
```

> [!warning]
> Both the annotation class and the implementation one must share the same name.

## Documentation

### @CommandClass

Used to annotate a class and mark it as a command class, the BAT framework will then look for callable commands in
the class.

> [!tip]
> **The name of the class will be used to categorize the commands inside, unless stated otherwise.**  
If you don't want the commands to be categorized with the same name as the class they're in you can use the
parameter `value` to give them a different category.

### @Command

Used to annotate a method and mark it as a command. This Annotation can only be used inside a class annotated with the
`@CommandClass` annotation.

> [!tip]
> **The name of the class will be used to categorize the commands inside, unless stated otherwise.**  
If you don't want the commands to be categorized with the same name as the class they're in you can use the
parameter `value` to give them a different category.  
You can call the command with multiple different names at the same time with the parameter `aliases`. 

### @Help

Used to annotate a method and register it in the help command.

> [!warning]
> The class in which you'll use this annotation must be annotated with the `@CommandClass` annotation.

- Parameter **description**: This parameter is the description of the command the final user will see when he will
call the prefix + `help ` + `<command name>` command.
- Parameter **usage**: This is an optional parameter that allows you to describe the syntax of the command, by default
the framework automatically put in this field the name of the command, and you will not need to write it.
- Parameter **category**: This is an optional parameter that allows you to insert the command in a different category
from the class the command is in. It has priority over the class' category.

#### Subcommand

Used to annotate a method and register it in the help command as a subcommand of another existing command.
You can mark a method multiple times for multiple subcommands.

> [!warning]
> The class in which you'll use this annotation must be annotated with the `@CommandClass` annotation.

- Parameter **name**: This parameter is used to give the subcommand a name. Leave it blank to use the method's name
instead.
- You can mark the subcommand to have multiple different names with the parameter `aliases`, currently it is only
used for utility only.
- Parameter **parent**: Use this parameter to define who's the original command. Leave it blank to use the method's
name instead.
- Parameter **description**: This parameter is the description of the subcommand the final user will see when he will
  call the prefix + `help ` + `<command name>` command.
- Parameter **usage**: This is an optional parameter that allows you to describe the syntax of the subcommand, by
default the framework automatically put in this field the name of the subcommand, and you will not need to write it.

### Injectable Parameters

#### @Args

Used to annotate a parameter in a method, it will inject an array of strings containing the arguments of a called
command without the command itself, trimmed and lowercased.

> [!warning]
> The method to which you'll use this annotation must be annotated with the `@Command` annotation.

- Parameter **value**: This is an optional value, set it to true if you want the annotation to inject into your method
an array of strings containing the original arguments of the called command without any modification. By default, it's
set on false.

#### @Event

Used to annotate a parameter in a method, it will inject the base event of a Discord message.

> [!warning]
> The method to which you'll use this annotation must be annotated with the `@Command` annotation.

#### @Options

Used to annotate a parameter in a method, it will inject an array of strings containing the options of a called
command, trimmed and lowercased.

> [!warning]
> The method to which you'll use this annotation must be annotated with the `@Command` annotation.

- Parameter **value**: This is an optional value, set it to true if you want the annotation to inject into your method
an array of strings containing the original arguments of the called command without any modification. By default, it's
set on false.

### Return Types

> [!warning]
> **THIS SECTION WILL BE REWORKED**.

#### @Reply

Used to annotate the return value of a method, it will tell the Discord bot to reply the message of the user who
called the command.

> [!warning]
> The method to which you'll use this annotation must be annotated with the `@Command` annotation.

- Parameter **value**: This is an optional parameter that allows you to define weather you want the bot to mention or
not the user who sent the message. By default, it's set on false (no mention).

## BAT Wrappers

The BAT Wrappers objects are what allows the framework to work on multiple platforms at the same time, in order to
achieve this result, many objects from other platforms' SDKs have been wrapped into batobjs.

You can easily find them into the source code at `it.lonter.bat.batobjs`. If you need more specific objects you can ask
me to add them as I, for simplicity, only added a few.

### How do I extend support to a platform that isn't supported?

In order to extend support to a platform that isn't supported yet, you will have to extend all the batobjs as shown
in `it.lonter.bat.wrappers`.

As this topic might be a bit complicated to explain, I accept suggestion for this file and I will gladly explain via
personal messages how everything works on any of my platforms.

## SharedResources

For an easier bot development, the framework provides an injectable Spring component called "SharedResources", but in
order to use it you will have to implement it yourself in a custom class. It is completely optional, but it
highly recommended and actually needed in case you want to use some other features of the framework like the logger.

### Why do I need to implement it?

This is to ensure additional customization is still available in case some assets are static between all the platform,
and you want them to be declared inside the class instead of using the `getValue()` method.

### How it works

The class provides two things

#### Default Properties

There is one mandatory property that is `app.prefix`, this will be the prefix the framework will use to understand if
a command was sent.

Then there are a few other optional properties

- `app.embedColor`: it will set a default color for all embeds, it is `null` by default. Must be a hexadecimal;
- `app.logger`: used to toggle the logger feature, it is set as `false` by default;
- `app.ignoreBots`: tells the framework whether the commands should ignore bots or not, it is set as
`true` by default.
- `app.logLength`: used to set the maximum log length per message, it is set as `1000` by default.

#### Methods

The class also has some public methods usable by the other classes

- `getValue()`

```java
@NotNull String getValue(final @NotNull String source, final @NotNull String name);
```

This method will return a property given its source and name, the property must be defined in the property file with
the `app.source.name` convention. If the property is not found the method will throw an `IllegalStateException`.

** **

- `getValueOrNull()`

```java
@Nullable String getValueOrNull(final @NotNull String source, final @NotNull String name);
```

This method will return a property given its source and name, the property should be defined in the property file with
the `app.source.name` convention. If the property is not found the method will return `null`.

** **

- `setShard()`

```java
void setShard(final @NotNull String source, final @NotNull BatShard shard);
```

While initializing one of the platform, you can set the platform's shard with this command.  
It is mandatory for the logger feature to work.

** **

- `getShard()`

```java
@NotNull BatShard getShard(final @NotNull String source);
```

If the shard for the given source is set, this method will return it, otherwise it will throw an
`IllegalStateException`.

** **

- `getShardOrNull()`

```java
@Nullable BatShard getShardOrNull(final @NotNull String source);
```

If the shard for the given source is set, this method will return it, otherwise it will return `null`.

** **

- `initServer()`

```java
void initServer(final @NotNull String source);
```

Use this method only if you want to use the logger feature.  
While initializing one of the platform, you must tell the framework to initialize a source's server. In order for
this to work you will also need to define an `app.source.mainServer` and an `app.source.logger`.

## Logger

The framework allows you to set up a server channel as a logger for your bot, the logger will send a message in the
channel for each log in the console.

In order for this to work, you will have to:

- Set the `app.source.mainServer` and `app.source.logger` properties;
- Implement the `SharedResources` class;
- Set a `source` with the `SharedResources.initServer()` method during platform initialization;
- Set a `BatShard` with the `SharedResources.initShard()` method during platform initialization;

Now, at this point, you'll be able to toggle whether the feature is on or off with the `app.logger` property.

## Code examples

### Ping command

#### Code

```java
@CommandClass
public final class General {
  @Command @Help(description = "The bot will send its current latency.")
  public @NotNull String ping(final @Event @NotNull BatMRE e) {
    return "Pong! My ping is: **" + e.bat.getPing() + "ms**.";
  }
}
```

#### Execution

(the prefix is `!`)

**User**: !ping  
**Bot**: Pong! My ping is: **5ms**.

> [!tip]
> The bot will create a single category called "General", in which there's only a single command called "ping";
if the user calls the `.help` command the bot will automatically understand that they want to see the list of commands
inside "General" because that's the only available category.

## Final thoughts

This project is still heavily a work in progress, you can contribute **BY HAND** (**no LLMs** allowed) by contacting
me on any of my platform and suggest changes.  
This project also doesn't have a repo on Maven Central cause I really don't know how to do that and I need to
investigate it.  
I made a [FluxFur server](https://fluxfur.com/invite/KElL5zwo) to discuss all of this if you want to.