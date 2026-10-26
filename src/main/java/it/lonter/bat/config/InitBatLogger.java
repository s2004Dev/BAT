package it.lonter.bat.config;

import it.lonter.bat.BatLogger;
import it.lonter.bat.SharedResources;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class InitBatLogger {
  public InitBatLogger(final @NotNull Optional<SharedResources> shared) {
    shared.ifPresentOrElse(BatLogger::setSharedResources, BatLogger::disableLogger);
  }
}