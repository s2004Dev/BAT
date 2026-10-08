package lonter.bat.annotations.help;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Use this annotation to mark a method as a subcommand to another method, this will allow the help command to show
 * better information about complex commands. You can mark a method multiple times.
 */
@Target(ElementType.METHOD)
@Repeatable(Subcommands.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
  /**
   * This parameter is used to give the subcommand a name. Leave it blank to use the method's name instead.
   */
  @NotNull String name() default "";

  /**
   * If a subcommand can be called with different names.
   */
  @NotNull String[] aliases() default { };

  /**
   * Use this parameter to define who's the original command. Leave it blank to use the method's name instead.
   */
  @NotNull String parent() default "";

  @NotNull String description();

  /**
   * The name of the function is already written by default, use this parameter only if the subcommand has additional
   * parameters instead of just calling it.
   */
  @NotNull String usage() default "";
}