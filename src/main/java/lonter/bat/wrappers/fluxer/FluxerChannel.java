package lonter.bat.wrappers.fluxer;

import static lonter.bat.wrappers.fluxer.FluxerMRE.getEmbedFluxer;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessage;

import lonter.jfa.api.entities.channel.middleman.MessageChannel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class FluxerChannel extends BatChannel {
  public @NotNull final MessageChannel channel;

  public FluxerChannel(final @NotNull MessageChannel channel) {
    super(channel.getIdLong(), channel.getName(), channel.getAsMention());
    this.channel = channel;
  }

  @Override public void sendMessage(final @NotNull String text, final @Nullable Consumer<BatMessage> success) {
    channel.sendMessage(text).queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }

  @Override public void sendEmbed(final @NotNull BatEmbed embed, final @Nullable Consumer<BatMessage> success) {
    channel.sendMessageEmbeds(getEmbedFluxer(embed).build())
      .queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }

  @Override public void editMessageById(final long id, final @NotNull String text,
                                        final @Nullable Consumer<BatMessage> success) {
    channel.editMessageById(id, text).queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }

  @Override public void editMessageEmbedsById(final long id, final @NotNull BatEmbed embed,
                                              final @Nullable Consumer<BatMessage> success) {
    channel.editMessageEmbedsById(id, getEmbedFluxer(embed).build())
      .queue(success == null ? null : i -> success.accept(new FluxerMessage(i)));
  }
}