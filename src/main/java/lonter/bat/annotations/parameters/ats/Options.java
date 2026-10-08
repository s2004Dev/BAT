package lonter.bat.annotations.parameters.ats;

import lonter.bat.annotations.parameters.AtParam;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) @AtParam
public @interface Options {
  /**
   * Set this value to true if you want the annotation to inject into your method an array of strings containing
   * the original options of the called command without any modification. By default, it's set on false.
   */
  boolean value() default false;
  /**
   * Set this value to which separator you want the annotation to process the options with, it will then inject them
   * into your method as an array of strings. By default, it's set as `--`.
   */
  @NotNull String separator() default "--";
}