# The BAT library

#### by @thepirateweasel

** **

The BAT utility library will allow you to easily make bots in Java; at the current version, only for Discord.  
This library uses `JDA` (Java Discord Adaptation).

** **

## Getting started

### Dependencies

First of all, this library currently uses Java 23 in preview mode. In the future I might make support for older
versions.  
As said in the introduction, this library uses JDA, but also Spring Boot 3.  
Besides these dependencies you will also need to install Lombok, JetBrains annotations and Reflections.  

For more information and versions, look at the `pom.xml` file.

### How to use the BAT library

To use this library follow these instructions.

- After normally initializing the bot, you will need to inject the component `CommandHandler` in the same class.
Then, in your `eventListener` you will need override the `onMessageReceived` method and finally call the method
`invoke()`. You will need to pass as argument the `MessageReceivedEvent`.
- In your property file you will need to define two properties:
  - `app.prefix`: It must contain the prefix of your BOT.
  - `app.groupId`: It must contain the value `@project.groupId@` or just your groupId.
- Now, to make your own commands you will need to make a class (it's name will define the category of the commands
inside), annotate it with the `@CommandClass` annotation and then make your own methods. The methods will be used as
commands, though only if annotated with the `@Command` annotation (their name will also be command name), the return
value will be what the bot will send as an output.
- See the final command by typing on Discord, in a channel the BOT has access to, the prefix + the name of the command.

#### Warning:

If the bot doesn't start and gives you an error about not finding certain classes it might be a problem related with
the packages. In that case you might want to be sure that your groupId includes the library as well and if your main
is in a different package then the library you might want to adopt this solution:

```java
@SpringBootApplication
@ComponentScan(basePackages = { "your.application.package", "lonter.bat" })
public class App {
    void main(final String @NotNull[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

## BAT concepts

### What's a category?

To easily sort commands for the end user, the library uses categories.  
A category is just a list of commands that is used for the `help` command.

### Help command

This library allows you to easily document your commands with the `@Help` annotation. If you annotate a command with
it, it will be registered in the `help` command. The user can access to it by typing the prefix + `help`.

By typing just the command without any argument, the bot will send an embed containing a list of categories. The user
will then have to type the prefix + `help ` + `<category name>` to see all the possible commands in the category. If
the user then types the prefix + `help ` + `<command name>` the bot will send a description for the command.

#### Define your own help command

W.I.P.

### Injectable parameters

Inside your commands you can inject values to perform specific tasks, for example the arguments of the command itself,
like what the user typed in the message, or the message event itself.

**The annotation must be the first of the parameter.**

Examples:

Correct  
`void command(@Args @SomethingElse String[] args) { }`.

Incorrect  
`void command(@SomethingElse @Args String[] args) { }`.

#### Define your own injectable parameter

W.I.P.

### Default return types

When defining a command, you can specify a return type, here are the allowed ones.

#### void

The bot will not return anything, but you are allowed to perform actions anyway.

#### String

The bot will just send a normal message containing the returned text.

#### EmbedBuilder

The bot will send an embedded Discord message.

### Annotated Return types

Inside your commands you can tell the bot to perform specific actions before returning the value.

**The annotation must be the first of the parameter.**

Examples:

Correct  
`@Reply @SomethingElse String command() { }`.

Incorrect  
`@SomethingElse @Reply String command() { }`.

#### Define your own return type

W.I.P.

## Documentation

### @CommandClass

Used to annotate a class and mark it as a command class, the library will then look for callable commands in the
class.

**The name of the class will be used to categorize the commands inside, unless stated otherwise.**  
If you don't want the commands to be categorized with the same name as the class they're in you can use the parameter
`value` to give them a different category.

### @Command

Used to annotate a method and mark it as a command. This Annotation can only be used inside a class annotated with the
`@CommandClass` annotation.

**The name of the method will be used to name the command, unless stated otherwise.**  
If you don't want the command to have the same name as the method you can use the parameter `value` to give them a
different one.

You can call the command with multiple different names at the same time with the parameter `aliases`. 

### @Help

Used to annotate a method and register it in the help command. This Annotation can only be used inside a class
annotated with the `@CommandClass` annotation.

- Parameter **description**: This parameter is the description of the command the final user will see when he will
call the prefix + `help ` + `<command name>` command.
- Parameter **usage**: This is an optional parameter that allows you to describe the syntax of the command, by default
the library automatically put in this field the name of the command, and you will not need to write it.
- Parameter **category**: This is an optional parameter that allows you to insert the command in a different category
from the class the command is in. It has priority over the class' category.

#### Subcommand

Used to annotate a method and register it in the help command. This Annotation can only be used inside a class
annotated with the `@CommandClass` annotation. You can mark a method multiple times.

It will be registered as a subcommand of another existing command.

- Parameter **name**: This parameter is used to give the subcommand a name. Leave it blank to use the method's name
instead.
- Parameter **parent**: Use this parameter to define who's the original command. Leave it blank to use the method's
name instead.
- Parameter **description**: This parameter is the description of the subcommand the final user will see when he will
  call the prefix + `help ` + `<command name>` command.
- Parameter **usage**: This is an optional parameter that allows you to describe the syntax of the subcommand, by
default the library automatically put in this field the name of the subcommand, and you will not need to write it.

### Injectable Parameters

#### @Args

Used to annotate a parameter in a method, it will inject an array of strings containing the arguments of a called
command without the command itself, trimmed and lowercased.

It only works if the method is annotated with `@Command`.

- Parameter **value**: This is an optional value, set it to true if you want the annotation to inject into your method
an array of strings containing the original arguments of the called command without any modification. By default, it's
set on false.

#### @Event

Used to annotate a parameter in a method, it will inject the base event of a Discord message.

It only works if the method is annotated with `@Command`.

### Return Types

#### @Reply

Used to annotate the return value of a method, it will tell the Discord bot to reply the message of the user who
called the command.

It only works if the method is annotated with `@Command`.

- Parameter **value**: This is an optional parameter that allows you to define weather you want the bot to mention or
not the user who sent the message. By default, it's set on false (no mention).

## Code examples

### Ping command

#### Code

```java
@CommandClass
public final class Miscellaneous {
  @Command @Help(description = "Will return the current ping.")
  public @NotNull String ping(final @Event @NotNull MessageReceivedEvent e) {
    return "Pong! Current latency: **" + e.getJDA().getGatewayPing() + "ms**.";
  }
}
```

#### Execution

(the prefix is `!`)

**User**: !ping  
**BOT**: Pong! Current latency: **5ms**.

#### Observations

The bot will create a single category called "Miscellaneous", in which there's only a single command called "ping";
if the user calls the `.help` command the bot will automatically understand that they want to see the list of commands
inside Miscellaneous because that's the only available category.