package lonter.bat.wrappers.fluxer;

import lombok.val;

import lonter.bat.batobjs.BatGRE;
import lonter.jfa.api.events.message.react.GenericMessageReactionEvent;
import lonter.jfa.api.events.message.react.MessageReactionAddEvent;

import org.jetbrains.annotations.NotNull;

public class FluxerGRE extends BatGRE {
  public FluxerGRE(final @NotNull GenericMessageReactionEvent e) {
    val emoji = e.getReaction().getEmoji();
    String id;

    try {
      id = emoji.asCustom().getId();
    }

    catch(final @NotNull Exception ex) {
      id = emoji.getName();
    }

    super(e.getMessageIdLong(), new FluxerUser(e.retrieveUser().complete()), id, "fluxer",
      e instanceof MessageReactionAddEvent ? "add" : "remove");
  }
}