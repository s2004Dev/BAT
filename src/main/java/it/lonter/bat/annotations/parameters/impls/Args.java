package it.lonter.bat.annotations.parameters.impls;

import lombok.val;

import it.lonter.bat.batobjs.BatMRE;
import it.lonter.bat.annotations.parameters.*;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ImplParam @Component
public final class Args extends CommandArg {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    return it.lonter.bat.annotations.parameters.ats.Args.class;
  }

  @Override public @NotNull Object value(final @NotNull BatMRE e, final @NotNull Annotation at) {
    if(!(at instanceof it.lonter.bat.annotations.parameters.ats.Args args)) {
      log.error("An error occurred while injecting @Args.");
      return new String[] { };
    }

    val input = e.message.text;
    return removeCommand(args.value() ? input : input.toLowerCase().replaceAll("\\s+", " "));
  }
}