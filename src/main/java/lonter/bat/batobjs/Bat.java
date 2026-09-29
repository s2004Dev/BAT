package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Bat {
  public abstract @Nullable BatUser getUserById(final long id);
  public abstract @Nullable BatUser getUserByTag(final @NotNull String tag);
  public abstract long getPing();
}