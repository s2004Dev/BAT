package lonter.bat.annotations.help;

import lonter.bat.batobjs.BatMRE;

import org.jetbrains.annotations.NotNull;

/**
 * Implement this interface to make your custom help command.
 * <p>The class in which this method is declared must be annotated with the {@link HelpImpl} annotation.
 */
public interface HelpInt {
  void help(final @NotNull BatMRE e);
}