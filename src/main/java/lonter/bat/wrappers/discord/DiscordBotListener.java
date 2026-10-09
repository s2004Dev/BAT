package lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;

import lonter.bat.SharedResources;
import lonter.bat.batobjs.BatListener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public final class DiscordBotListener extends ListenerAdapter {
  private static final String SOURCE = "discord";

  private final SharedResources shared;
  private final BatListener listener;

  @Override public void onMessageReceived(final @NotNull MessageReceivedEvent e) {
    listener.onMessageReceived(new DiscordMRE(e));
  }

  @Override public void onGuildReady(final @NotNull GuildReadyEvent __) {
    if(shared.isAwaiting(SOURCE))
      shared.serverReady(SOURCE);

    listener.onServerReady(SOURCE);
  }

  @Override public void onMessageReactionAdd(final @NotNull MessageReactionAddEvent e) {
    listener.onMessageReaction(new DiscordGRE(e));
  }

  @Override public void onMessageReactionRemove(final @NotNull MessageReactionRemoveEvent e) {
    listener.onMessageReaction(new DiscordGRE(e));
  }

  @Override public void onGuildMemberJoin(final @NotNull GuildMemberJoinEvent e) {
    listener.onMemberJoinLeave(new DiscordGGE(e));
  }

  @Override public void onGuildMemberRemove(final @NotNull GuildMemberRemoveEvent e) {
    listener.onMemberJoinLeave(new DiscordGGE(e));
  }

  @Override public void onGuildMemberRoleAdd(final @NotNull GuildMemberRoleAddEvent e) {
    listener.onServerMemberRoleChange(new DiscordRCE(e));
  }

  @Override public void onGuildMemberRoleRemove(final @NotNull GuildMemberRoleRemoveEvent e) {
    listener.onServerMemberRoleChange(new DiscordRCE(e));
  }
}