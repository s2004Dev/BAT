package lonter.bat.annotations.rets;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Extend this class to define a custom action to perform before returning a value to the user.
 * <p>The return value will be an annotation annotated with {@link AtRet}.
 * <p>To make it work, you need to make child class that extends {@link ReturnType} first, and it must be annotated
 * with {@link ImplRet}.
 */
public abstract class ReturnType {
    /**
     * This function will be called before returning a value to the user.
     * @param e the Discord message event
     * @param output what the original function returned
     * @param at the annotation itself
     */
    public abstract void action(final @NotNull MessageReceivedEvent e, final @NotNull Object output,
                                final @NotNull Annotation at);
}