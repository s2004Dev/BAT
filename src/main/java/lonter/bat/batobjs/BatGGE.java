package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class BatGGE {
  public final @NotNull BatUser author;
  public final @NotNull String source;
  public final @NotNull String eventType;
}