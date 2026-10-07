package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatGGE;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

import org.jetbrains.annotations.NotNull;

public class DiscordGGE extends BatGGE {
  public DiscordGGE(final @NotNull GuildMemberJoinEvent e) {
    super(new DiscordUser(e.getMember()), "discord", "join");
  }

  public DiscordGGE(final @NotNull GuildMemberRemoveEvent e) {
    val member = e.getMember();

    if(member == null)
      throw new IllegalStateException("Member is null");

    super(new DiscordUser(member), "discord", "leave");
  }
}