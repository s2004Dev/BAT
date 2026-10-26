package it.lonter.bat.wrappers.fluxer;

import it.lonter.bat.batobjs.*;
import it.lonter.jfa.api.EmbedBuilder;
import it.lonter.jfa.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Optional;

public final class FluxerMRE extends BatMRE {
  public FluxerMRE(final @NotNull MessageReceivedEvent event) {
    super(new FluxerMessage(event.getMessage()), new FluxerChannel(event.getChannel()),
      new FluxerUser(event.getAuthor()), new FluxerUser(event.getJFA().getSelfUser()),
      Optional.ofNullable(event.isFromGuild() ? new FluxerServer(event.getGuild()) : null),
      new FluxerBat(event.getJFA()), "fluxer");
  }

  public static @NotNull EmbedBuilder getEmbedFluxer(final @NotNull BatEmbed embed) {
    return new EmbedBuilder() {{
      setTitle(checkField(embed.title));
      setDescription(checkField(embed.description));
      setFooter(checkField(embed.footer));
      setImage(checkField(embed.imageUrl));
      setThumbnail(checkField(embed.thumbnailUrl));

      if(embed.color != null && !embed.color.isBlank())
        setColor(Color.decode(embed.color));
    }};
  }

  private static @Nullable String checkField(final @Nullable String field) {
    return field != null && !field.isBlank() ? field : null;
  }
}