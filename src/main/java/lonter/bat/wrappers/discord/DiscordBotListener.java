package lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;

import lonter.bat.batobjs.BatListener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @AllArgsConstructor
public final class DiscordBotListener extends ListenerAdapter {
  private static final String source = "discord";

  private final List<BatListener> listener;

  @Override public void onMessageReceived(final @NotNull MessageReceivedEvent e) {
    listener.forEach(i -> i.onMessageReceived(new DiscordMRE(e)));
  }

  @Override public void onGuildReady(final @NotNull GuildReadyEvent __) {
    listener.forEach(i -> i.onGuildReady(source));
  }

  @Override public void onMessageReactionAdd(final @NotNull MessageReactionAddEvent e) {
    listener.forEach(i -> i.onMessageReaction(new DiscordGRE(e)));
  }

  @Override public void onMessageReactionRemove(final @NotNull MessageReactionRemoveEvent e) {
    listener.forEach(i -> i.onMessageReaction(new DiscordGRE(e)));
  }

  @Override public void onGuildMemberJoin(final @NotNull GuildMemberJoinEvent e) {
    listener.forEach(i -> i.onMemberJoinLeave(new DiscordGGE(e)));
  }

  @Override public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {
    listener.forEach(i -> i.onMemberJoinLeave(new DiscordGGE(e)));
  }

  @Override public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent e) {
    listener.forEach(i -> i.onGuildMemberRoleAdd(new DiscordRCE(e)));
  }
}