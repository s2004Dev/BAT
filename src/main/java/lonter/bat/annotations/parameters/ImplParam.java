package lonter.bat.annotations.parameters;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to annotate the class containing the implementation of an injectable parameter.
 * <p>The injectable object will be an annotation annotated with {@link AtParam}.
 * <p>To make it work, you need to make child class that extends {@link CommandArg} first, and it must be annotated
 * with {@link ImplParam}.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplParam { }