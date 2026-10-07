package lonter.bat.batobjs;

import org.jetbrains.annotations.Nullable;

public abstract class BatShard {
  public abstract @Nullable BatServer getServerById(final long id);
}