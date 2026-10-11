package lonter.bat.wrappers.fluxer;

import lonter.bat.batobjs.BatRole;
import lonter.jfa.api.entities.Role;

import org.jetbrains.annotations.NotNull;

public final class FluxerRole extends BatRole {
  public FluxerRole(final @NotNull Role role) {
    super(role.getIdLong());
  }
}