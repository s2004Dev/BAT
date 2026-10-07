package lonter.bat.wrappers.discord;

import lonter.bat.batobjs.BatMessage;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;

import org.jetbrains.annotations.NotNull;

public class DiscordMessage extends BatMessage {
  private final @NotNull Message message;

  public DiscordMessage(final @NotNull Message message) {
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