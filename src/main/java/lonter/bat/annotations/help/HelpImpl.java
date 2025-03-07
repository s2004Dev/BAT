package lonter.bat.annotations.help;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotate a class with this annotation to mark it as a help command implementation.
 * Then the class should also implement the {@link HelpInt} interface.
 * <p>The `help()` command will be called whenever the user calls for it.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HelpImpl { }