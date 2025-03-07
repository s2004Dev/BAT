package lonter.bat.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotate a class with this function to let the bot know it will have to search the commands in there.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandClass { }