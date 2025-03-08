package lonter.bat.annotations.parameters.impls;

import lonter.bat.annotations.parameters.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@ImplParam
public final class Pristine extends CommandArg {
    @Override
    public @NotNull Object value(@NotNull MessageReceivedEvent e, final @NotNull Annotation at) {
        return removeCommand(e.getMessage().getContentRaw(), true);
    }
}