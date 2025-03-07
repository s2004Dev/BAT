package lonter.bat.annotations.parameters.ats;

import lonter.bat.annotations.parameters.AtParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will inject into your function the base event of a Discord message.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) @AtParam
public @interface Event { }