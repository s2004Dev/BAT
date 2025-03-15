package lonter.bat.annotations.parameters.impls;

import lonter.bat.annotations.parameters.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@ImplParam
public final class Args extends CommandArg {
    @Override
    public @NotNull Object value(@NotNull MessageReceivedEvent e, final @NotNull Annotation at) {
        if(!(at instanceof lonter.bat.annotations.parameters.ats.Args args)) {
            System.err.println("An error occurred in Reply action.");
            return new String[]{ };
        }

        final var input = e.getMessage().getContentRaw();
        return removeCommand(args.value() ? input : input.toLowerCase()
          .replaceAll("\\s+", " "));
    }
}