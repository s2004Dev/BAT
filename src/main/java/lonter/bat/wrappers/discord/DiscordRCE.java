package lonter.bat.wrappers.discord;

import lonter.bat.batobjs.BatRCE;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DiscordRCE extends BatRCE {
  public DiscordRCE(final @NotNull GuildMemberRoleAddEvent e) {
    super(new DiscordUser(e.getUser()), new ArrayList<>(e.getRoles().stream().map(DiscordRole::new).toList()),
      "discord", "add");
  }

  public DiscordRCE(final @NotNull GuildMemberRoleRemoveEvent e) {
    super(new DiscordUser(e.getUser()), new ArrayList<>(e.getRoles().stream().map(DiscordRole::new).toList()),
      "discord", "remove");
  }
}