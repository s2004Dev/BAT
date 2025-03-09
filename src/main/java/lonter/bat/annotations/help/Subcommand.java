package lonter.bat.annotations.help;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark a method as a subcommand to another method, this will allow the help command to show
 * better information about complex commands.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subcommand {
  /**
   * This parameter is used to give the subcommand a name. Leave it blank to use the method's name instead.
   */
  String name() default "";
  /**
   * Use this parameter to define who's the original command.
   */
  String parent();
  String description();
  /**
   * The name of the function is already written by default, use this parameter only if the subcommand has additional
   * parameters instead of just calling it.
   */
  String usage() default "";
}