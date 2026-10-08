package lonter.bat.wrappers.discord;

import static lonter.bat.wrappers.discord.DiscordMRE.getEmbedDS;

import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessage;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DiscordMessage extends BatMessage {
  private final @NotNull Message message;

  public DiscordMessage(final @NotNull Message message) {
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
      .queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }

  @Override public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping,
                                   final @Nullable Consumer<BatMessage> success) {
    message.replyEmbeds(getEmbedDS(embed).build()).mentionRepliedUser(ping)
      .queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }
}