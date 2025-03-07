package lonter.bat.annotations.rets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to make a custom action to perform before returning a value to the user.
 * <p>The return value will be an annotation annotated with {@link AtRet}.
 * <p>To make it work, you need to make child class that extends {@link ReturnType} first, and it must be annotated
 * with {@link ImplRet}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AtRet { }