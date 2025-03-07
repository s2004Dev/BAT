package lonter.bat.annotations.rets;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to make a custom action to perform before returning a value to the user.
 * <p>The return value will be an annotation annotated with {@link AtRet}.
 * <p>To make it work, you need to make child class that extends {@link ReturnType} first, and it must be annotated
 * with {@link ImplRet}.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplRet { }