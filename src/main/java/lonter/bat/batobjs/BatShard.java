package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class BatShard {
  public abstract @NotNull Optional<BatServer> getServerById(final long id);
}