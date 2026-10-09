package lonter.bat.wrappers.fluxer;

import lombok.AllArgsConstructor;

import lonter.bat.SharedResources;
import lonter.bat.batobjs.BatListener;
import lonter.jfa.api.events.guild.GuildReadyEvent;
import lonter.jfa.api.events.guild.member.GuildMemberJoinEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRemoveEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRoleAddEvent;
import lonter.jfa.api.events.guild.member.GuildMemberRoleRemoveEvent;
import lonter.jfa.api.events.message.MessageReceivedEvent;
import lonter.jfa.api.events.message.react.MessageReactionAddEvent;
import lonter.jfa.api.events.message.react.MessageReactionRemoveEvent;
import lonter.jfa.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public final class FluxerBotListener extends ListenerAdapter {
  private static final String SOURCE = "fluxer";

  private final SharedResources shared;
  private final BatListener listener;

  @Override public void onMessageReceived(final @NotNull MessageReceivedEvent e) {
    listener.onMessageReceived(new FluxerMRE(e));
  }

  @Override public void onGuildReady(final @NotNull GuildReadyEvent __) {
    if(shared.isAwaiting(SOURCE))
      shared.serverReady(SOURCE);

    listener.onServerReady(SOURCE);
  }

  @Override public void onMessageReactionAdd(final @NotNull MessageReactionAddEvent e) {
    listener.onMessageReaction(new FluxerGRE(e));
  }

  @Override public void onMessageReactionRemove(final @NotNull MessageReactionRemoveEvent e) {
    listener.onMessageReaction(new FluxerGRE(e));
  }

  @Override public void onGuildMemberJoin(final @NotNull GuildMemberJoinEvent e) {
    listener.onMemberJoinLeave(new FluxerGGE(e));
  }

  @Override public void onGuildMemberRemove(final @NotNull GuildMemberRemoveEvent e) {
    listener.onMemberJoinLeave(new FluxerGGE(e));
  }

  @Override public void onGuildMemberRoleAdd(final @NotNull GuildMemberRoleAddEvent e) {
    listener.onServerMemberRoleChange(new FluxerRCE(e));
  }

  @Override public void onGuildMemberRoleRemove(final @NotNull GuildMemberRoleRemoveEvent e) {
    listener.onServerMemberRoleChange(new FluxerRCE(e));
  }
}