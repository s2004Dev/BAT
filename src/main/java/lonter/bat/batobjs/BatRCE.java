package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * BatRoleCEvent (I really don't remember why I called it like this)
 */
@AllArgsConstructor
public abstract class BatRCE {
  public final @NotNull BatUser author;
  public final @NotNull ArrayList<BatRole> roles;
  public final @NotNull String source;
  public final @NotNull String eventType;
}