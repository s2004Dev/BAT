package lonter.bat.wrappers.fluxer;

import lombok.AllArgsConstructor;

import lonter.bat.batobjs.BatListener;
import lonter.jfa.api.events.guild.GuildReadyEvent;
import lonter.jfa.api.events.guild.member.GuildMemberJoinEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRemoveEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRoleAddEvent;
import lonter.jfa.api.events.message.MessageReceivedEvent;
import lonter.jfa.api.events.message.react.MessageReactionAddEvent;
import lonter.jfa.api.events.message.react.MessageReactionRemoveEvent;
import lonter.jfa.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @AllArgsConstructor
public final class FluxerBotListener extends ListenerAdapter {
  private static final String source = "fluxer";

  private final List<BatListener> listener;

  @Override public void onMessageReceived(final @NotNull MessageReceivedEvent e) {
    listener.forEach(i -> i.onMessageReceived(new FluxerMRE(e)));
  }

  @Override public void onGuildReady(final @NotNull GuildReadyEvent __) {
    listener.forEach(i -> i.onGuildReady(source));
  }

  @Override public void onMessageReactionAdd(final @NotNull MessageReactionAddEvent e) {
    listener.forEach(i -> i.onMessageReaction(new FluxerGRE(e)));
  }

  @Override public void onMessageReactionRemove(final @NotNull MessageReactionRemoveEvent e) {
    listener.forEach(i -> i.onMessageReaction(new FluxerGRE(e)));
  }

  @Override public void onGuildMemberJoin(final @NotNull GuildMemberJoinEvent e) {
    listener.forEach(i -> i.onMemberJoinLeave(new FluxerGGE(e)));
  }

  @Override public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {
    listener.forEach(i -> i.onMemberJoinLeave(new FluxerGGE(e)));
  }

  @Override public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent e) {
    listener.forEach(i -> i.onGuildMemberRoleAdd(new FluxerRCE(e)));
  }
}