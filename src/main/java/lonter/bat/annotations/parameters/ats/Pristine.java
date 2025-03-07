package lonter.bat.annotations.parameters.ats;

import lonter.bat.annotations.parameters.AtParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will inject into your function an array of strings containing the original arguments of a called
 * command without any modification.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) @AtParam
public @interface Pristine { }