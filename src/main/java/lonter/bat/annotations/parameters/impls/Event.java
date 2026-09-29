package lonter.bat.annotations.parameters.impls;

import lonter.bat.batobjs.BatMessageReceivedEvent;
import lonter.bat.annotations.parameters.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ImplParam @Component
public final class Event extends CommandArg {
  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.parameters.ats.Event.class;
  }

  @Override public @NotNull Object value(final @NotNull BatMessageReceivedEvent e, final @NotNull Annotation at) {
    return e;
  }
}