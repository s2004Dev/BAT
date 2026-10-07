package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class BatMessage {
  public @NotNull String text;

  public abstract boolean isSystemPinned();
  public abstract void delete();
}