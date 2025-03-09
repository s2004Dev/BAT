package lonter.bat.annotations;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotate a class with this function to let the bot know it will have to search the commands in there.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandClass {
  /**
   * This parameter is used to give your command a category. Leave it blank to use the class' name instead.
   */
  @AliasFor(annotation = Component.class)
  @NotNull String value() default "";
}