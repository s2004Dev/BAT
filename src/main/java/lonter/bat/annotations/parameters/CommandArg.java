package lonter.bat.annotations.parameters;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Extend this class to make an injectable object for your commands' parameter.
 * <p>The injectable object will be an annotation annotated with {@link AtParam}.
 * <p>To make it work, you need to make child class that extends {@link CommandArg} first, and it must be annotated
 * with {@link ImplParam}.
 */
public abstract class CommandArg {
  /**
   * Implement this method to declare which parameter annotation this class handles.
   * This is crucial for the CommandHandler to map annotations to the correct logic.
   *
   * @return The .class of the annotation you are handling (e.g., return Args.class;).
   */
  public abstract Class<? extends Annotation> getAnnotationType();

  /**
   * This function will be called every time you need to inject your parameter. The return value is what the command
   * will get injected.
   * @param e the Discord message event
   * @param at the annotation itself
   * @return the value to inject
   */
  public abstract @NotNull Object value(final @NotNull MessageReceivedEvent e, final @NotNull Annotation at);

  /**
   * Utility to remove the first element from a string (in this case the command from the args).
   * @param input the original string with prefix, command and arguments.
   * @return an array of string containing only the arguments.
   */
  protected static String @NotNull[] removeCommand(final @NotNull String input) {
    final var array = input.split(" ");
    final var newArray = new String[array.length-1];

    System.arraycopy(array, 1, newArray, 0, newArray.length);

    return newArray;
  }
}