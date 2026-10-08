package lonter.bat.wrappers.fluxer;

import static lonter.bat.wrappers.fluxer.FluxerMRE.getEmbedFluxer;

import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessage;
import lonter.jfa.api.entities.Message;
import lonter.jfa.api.entities.MessageType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FluxerMessage extends BatMessage {
  private final @NotNull Message message;

  public FluxerMessage(final @NotNull Message message) {
    super(message.getIdLong(), message.getContentRaw());
    this.message = message;
  }

  @Override public boolean isSystemPinned() {
    return message.getType() == MessageType.CHANNEL_PINNED_ADD;
  }

  @Override public void delete() {
    message.delete().queue();
  }

  @Override public void reply(final @NotNull String text, final boolean ping,
                              final @Nullable Consumer<BatMessage> success) {
    message.reply(text).mentionRepliedUser(ping)
      .queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }

  @Override public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping,
                                   final @Nullable Consumer<BatMessage> success) {
    message.replyEmbeds(getEmbedFluxer(embed).build()).mentionRepliedUser(ping)
      .queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }
}