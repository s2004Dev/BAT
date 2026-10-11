package lonter.bat.wrappers.fluxer;

import lonter.bat.batobjs.*;
import lonter.jfa.api.EmbedBuilder;
import lonter.jfa.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public final class FluxerMRE extends BatMRE {
  public FluxerMRE(final @NotNull MessageReceivedEvent event) {
    super(new FluxerMessage(event.getMessage()), new FluxerChannel(event.getChannel()),
      new FluxerUser(event.getAuthor()), new FluxerUser(event.getJFA().getSelfUser()),
      event.isFromGuild() ? new FluxerServer(event.getGuild()) : null, new FluxerBat(event.getJFA()), "fluxer");
  }

  protected static @NotNull EmbedBuilder getEmbedFluxer(final @NotNull BatEmbed embed) {
    return new EmbedBuilder() {{
      setTitle(embed.title);
      setDescription(embed.description);
      setFooter(embed.footer);
      setImage(embed.imageUrl);
      setThumbnail(embed.thumbnailUrl);

      if(embed.color != null)
        setColor(Color.decode(embed.color));
    }};
  }
}