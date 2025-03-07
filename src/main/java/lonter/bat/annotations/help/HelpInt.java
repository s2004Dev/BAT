package lonter.bat.annotations.help;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

/**
 * Implement this interface to make your custom help command.
 * <p>The class in which this method is declared must be annotated with the {@link HelpImpl} annotation.
 */
public interface HelpInt {
  void help(final @NotNull MessageReceivedEvent e);
}