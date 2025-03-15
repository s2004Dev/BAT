package lonter.bat.annotations.parameters.ats;

import lonter.bat.annotations.parameters.AtParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will inject into your method an array of strings containing the arguments of a called command
 * without the command itself, trimmed and lowercased.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) @AtParam
public @interface Args {
  /**
   * Set this value to true if you want the annotation to inject into your method an array of strings containing
   * the original arguments of the called command without any modification. By default, it's set on false.
   */
  boolean value() default false;
}