package lonter.bat.annotations.rets.ats;

import lonter.bat.annotations.rets.AtRet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate your return value with this annotation to tell the bot it has to reply the message of the user who called
 * the command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE) @AtRet
public @interface Reply {
    /** This value define weather you want the bot to mention or not the user who sent the message.
     * <p>By default, it's set on false (no mention).
     */
    boolean value() default false;
}