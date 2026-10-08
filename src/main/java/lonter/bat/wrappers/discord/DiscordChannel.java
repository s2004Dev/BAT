package lonter.bat.wrappers.discord;

import static lonter.bat.wrappers.discord.DiscordMRE.getEmbedDS;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessage;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DiscordChannel extends BatChannel {
  public @NotNull final MessageChannel channel;

  public DiscordChannel(final @NotNull MessageChannel channel) {
    super(channel.getIdLong(), channel.getName(), channel.getAsMention());
    this.channel = channel;
  }

  @Override public void sendMessage(final @NotNull String text, final @Nullable Consumer<BatMessage> success) {
    channel.sendMessage(text).queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }

  @Override public void sendEmbed(final @NotNull BatEmbed embed, final @Nullable Consumer<BatMessage> success) {
    channel.sendMessageEmbeds(getEmbedDS(embed).build())
      .queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }

  @Override public void editMessageById(final long id, final @NotNull String text,
                                        final @Nullable Consumer<BatMessage> success) {
    channel.editMessageById(id, text).queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }

  @Override public void editMessageEmbedsById(final long id, final @NotNull BatEmbed embed,
                                              final @Nullable Consumer<BatMessage> success) {
    channel.editMessageEmbedsById(id, getEmbedDS(embed).build())
      .queue(success == null ? null : i -> success.accept(new DiscordMessage(i)));
  }
}