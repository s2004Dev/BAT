package lonter.bat.wrappers.fluxer;

import lonter.bat.batobjs.BatMessage;
import lonter.jfa.api.entities.Message;
import lonter.jfa.api.entities.MessageType;

import org.jetbrains.annotations.NotNull;

public class FluxerMessage extends BatMessage {
  private final @NotNull Message message;

  public FluxerMessage(final @NotNull Message message) {
    super(message.getContentRaw());
    this.message = message;
  }

  @Override public boolean isSystemPinned() {
    return message.getType() == MessageType.CHANNEL_PINNED_ADD;
  }

  @Override public void delete() {
    message.delete().queue();
  }
}