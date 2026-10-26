package it.lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;

public class BatException extends RuntimeException {
  public BatException(final @NotNull String message) {
    super(message);
  }
}