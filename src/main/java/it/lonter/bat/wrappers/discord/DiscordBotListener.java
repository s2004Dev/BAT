package it.lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;

import lombok.val;

import it.lonter.bat.BatLogger;
import it.lonter.bat.CommandHandler;
import it.lonter.bat.SharedResources;
import it.lonter.bat.batobjs.BatListener;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component @AllArgsConstructor
public final class DiscordBotListener extends ListenerAdapter {
  private static final String SOURCE = "discord";

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final CommandHandler handler;
  private final Optional<SharedResources> op;
  private final BatListener listener;

  @Override public void onMessageReceived(final @NotNull MessageReceivedEvent e) {
    val mre = new DiscordMRE(e);

    if(ignoreBots() && mre.author.isBot)
      return;

    try {
      handler.invoke(mre);
      listener.onMessageReceived(mre);
    }

    catch(final @NotNull Exception ex) {
      BatLogger.logMreError(SOURCE, log, mre, ex);
    }
  }

  private boolean ignoreBots() {
    return op.map(i -> i.ignoreBots).orElse(true);
  }

  @Override public void onGuildReady(final @NotNull GuildReadyEvent __) {
    op.ifPresent(shared -> {
      if(shared.isAwaiting(SOURCE))
        shared.serverReady(SOURCE);
    });

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