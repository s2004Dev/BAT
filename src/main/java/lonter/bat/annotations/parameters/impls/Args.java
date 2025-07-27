package lonter.bat.annotations.parameters.impls;

import lonter.bat.annotations.parameters.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ImplParam @Component
public final class Args extends CommandArg {
  @Override
  public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.parameters.ats.Args.class;
  }

  @Override
  public @NotNull Object value(final @NotNull MessageReceivedEvent e, final @NotNull Annotation at) {
    if(!(at instanceof lonter.bat.annotations.parameters.ats.Args args)) {
      System.err.println("An error occurred while injecting @Args.");
      return new String[]{ };
    }

    final var input = e.getMessage().getContentRaw();
    return removeCommand(args.value() ? input : input.toLowerCase().replaceAll("\\s+", " "));
  }
}