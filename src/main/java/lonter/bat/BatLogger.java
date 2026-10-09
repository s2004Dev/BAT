package lonter.bat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;

import lombok.val;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BatLogger extends AppenderBase<ILoggingEvent> {
  private static final String RESET = "\u001B[0m";
  private static final String BOLD_RED = "\u001B[1;31m";
  private static final String BOLD_YELLOW = "\u001B[1;33m";
  private static final String GREEN = "\u001B[0;32m";
  private static final String CYAN = "\u001B[0;36m";
  private static final String GRAY = "\u001B[0;30m";

  private final AtomicBoolean ready = new AtomicBoolean(false);
  private final Queue<ILoggingEvent> buffer = new ConcurrentLinkedQueue<>();

  private static volatile SharedResources shared;

  public static void setSharedResources(final @NotNull SharedResources sharedResources) {
    shared = sharedResources;
  }

  @Override protected void append(final @NotNull ILoggingEvent log) {
    if(shared == null || !ready.get()) {
      if(shared != null && shared.allReady()) {
        synchronized(this) {
          if(!ready.get()) {
            ready.set(true);

            if(shared.logger) {
              shared.initBroadcast();
              flushBuffer();
            }

            else
              buffer.clear();
          }
        }

        if(shared.logger)
          send(log);

        return;
      }

      log.prepareForDeferredProcessing();
      buffer.add(log);

      return;
    }

    if(shared.logger)
      send(log);
  }

  private void flushBuffer() {
    val backLog = new StringBuilder();

    ILoggingEvent bufferedEvent;

    while((bufferedEvent = buffer.poll()) != null)
      backLog.append(format(bufferedEvent)).append("\n");

    if(!backLog.isEmpty())
      shared.broadcastLog(backLog.toString());
  }

  private void send(final @NotNull ILoggingEvent log) {
    if(shared != null)
      shared.broadcastLog(format(log));
  }

  public @NotNull String format(final @NotNull ILoggingEvent log) {
    val ret = new StringBuilder();
    val level = log.getLevel();

    ret.append(String.format("%s[%s]%s %s", getColorForLevel(level), String.format("%-5s", level), RESET,
      log.getFormattedMessage()));

    if(log.getThrowableProxy() != null)
      ret.append("\n").append(BOLD_RED).append(ThrowableProxyUtil.asString(log.getThrowableProxy())).append(RESET);

    return ret.toString();
  }

  private @NotNull String getColorForLevel(final @NotNull Level level) {
    return switch(level.toInt()) {
      case Level.ERROR_INT -> BOLD_RED;
      case Level.WARN_INT -> BOLD_YELLOW;
      case Level.INFO_INT -> GREEN;
      case Level.DEBUG_INT -> CYAN;

      default -> GRAY;
    };
  }
}