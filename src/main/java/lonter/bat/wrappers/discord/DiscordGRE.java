package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatGRE;

import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import org.jetbrains.annotations.NotNull;

public final class DiscordGRE extends BatGRE {
  public DiscordGRE(final @NotNull GenericMessageReactionEvent e) {
    val emoji = e.getReaction().getEmoji();
    String id;

    try {
      id = emoji.asCustom().getId();
    }

    catch(final @NotNull Exception ex) {
      id = emoji.getName();
    }

    super(e.getMessageIdLong(), new DiscordUser(e.retrieveUser().complete()), id, "discord",
      e instanceof MessageReactionAddEvent ? "add" : "remove");
  }
}