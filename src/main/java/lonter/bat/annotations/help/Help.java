package lonter.bat.annotations.help;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this annotation to register the command on the help command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Help {
  String description();
  /**
   * The name of the function is already written by default, use this parameter only if the command has additional
   * parameters instead of just calling it.
   */
  String usage() default "";
  /**
   * By default, this parameter will allow you to categorize commands. If left blank the default value will be the
   * name of the class in which the method is declared.
   */
  String category() default "";
}