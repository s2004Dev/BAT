package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class BatRole {
  public long id;

  /**
  * Should make something like `roleList.contains(role)` work.
  */
  @Override public boolean equals(final @Nullable Object obj) {
    return obj instanceof BatRole role && id == role.id;
  }
}