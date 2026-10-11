package lonter.bat.wrappers.fluxer;

import lonter.bat.batobjs.BatRCE;
import lonter.jfa.api.events.guild.member.GuildMemberRoleAddEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRoleRemoveEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class FluxerRCE extends BatRCE {
  public FluxerRCE(final @NotNull GuildMemberRoleAddEvent e) {
    super(new FluxerUser(e.getUser()), new ArrayList<>(e.getRoles().stream().map(FluxerRole::new).toList()),
      "fluxer", "add");
  }

  public FluxerRCE(final @NotNull GuildMemberRoleRemoveEvent e) {
    super(new FluxerUser(e.getUser()), new ArrayList<>(e.getRoles().stream().map(FluxerRole::new).toList()),
      "fluxer", "remove");
  }
}