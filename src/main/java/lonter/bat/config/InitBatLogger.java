package lonter.bat.config;

import lonter.bat.BatLogger;
import lonter.bat.SharedResources;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public final class InitBatLogger {
  public InitBatLogger(final @NotNull SharedResources shared) {
    BatLogger.setSharedResources(shared);
  }
}