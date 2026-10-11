package lonter.bat.wrappers.fluxer;

import lombok.val;

import lonter.bat.batobjs.BatGGE;
import lonter.jfa.api.events.guild.member.GuildMemberJoinEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRemoveEvent;

import org.jetbrains.annotations.NotNull;

public final class FluxerGGE extends BatGGE {
  public FluxerGGE(final @NotNull GuildMemberJoinEvent e) {
    super(new FluxerUser(e.getMember()), "fluxer", "join");
  }

  public FluxerGGE(final @NotNull GuildMemberRemoveEvent e) {
    val member = e.getMember();

    if(member == null)
      throw new IllegalStateException("Member is null");

    super(new FluxerUser(member), "fluxer", "leave");
  }
}