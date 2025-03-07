package lonter.bat.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to make a custom injectable parameter for your commands.
 * <p>The injectable object will be an annotation annotated with {@link AtParam}.
 * <p>To make it work, you need to make child class that extends {@link CommandArg} first, and it must be annotated
 * with {@link ImplParam}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AtParam { }