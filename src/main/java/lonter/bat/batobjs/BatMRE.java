package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * BatMessageReceivedEvent
 */
@AllArgsConstructor
public abstract class BatMRE {
  public final @NotNull BatMessage message;
  public final @NotNull BatChannel channel;
  public final @NotNull BatUser author;
  public final @NotNull BatUser self;
  public final @Nullable BatServer server;
  public final @NotNull Bat bat;
  public final @NotNull String source;

  public void sendMessage(final @NotNull String text) {
    sendMessage(text, null);
  }

  public void sendMessage(final @NotNull String text, final @Nullable Consumer<BatMessage> success) {
    channel.sendMessage(text, success);
  }

  public void reply(final @NotNull String text, final boolean ping) {
    reply(text, ping, null);
  }

  public void reply(final @NotNull String text, final boolean ping, final @Nullable Consumer<BatMessage> success) {
    message.reply(text, ping, success);
  }

  public void sendEmbed(final @NotNull BatEmbed embed) {
    sendEmbed(embed, null);
  }

  public void sendEmbed(final @NotNull BatEmbed embed, final @Nullable Consumer<BatMessage> success) {
    channel.sendEmbed(embed, success);
  }

  public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping) {
    replyEmbed(embed, ping, null);
  }

  public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping,
                         final @Nullable Consumer<BatMessage> success) {
    message.replyEmbed(embed, ping, success);
  }

  public void deleteMessage() {
    message.delete();
  }
}