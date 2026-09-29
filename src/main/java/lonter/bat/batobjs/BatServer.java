package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class BatServer {
  public final long id;

  public abstract @Nullable BatUser getMemberById(final long id);
}