package lonter.bat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this annotation to mark it as a callable command.
 * <p>The class containing the command must be annotated with {@link CommandClass}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandCall {
  /**
   * This parameter is used to give your command a name. Leave it blank to use the function's name instead.
   */
  String value() default "";
}