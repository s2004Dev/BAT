package it.lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class Bat {
  public abstract @NotNull Optional<BatUser> getUserById(final long id);
  public abstract @NotNull Optional<BatUser> getUserByTag(final @NotNull String tag);
  public abstract long getPing();
}