package lonter.bat.wrappers.fluxer;

import lonter.bat.batobjs.*;
import lonter.jfa.api.EmbedBuilder;
import lonter.jfa.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class FluxerMRE extends BatMRE {
  private final @NotNull MessageReceivedEvent event;

  public FluxerMRE(final @NotNull MessageReceivedEvent event) {
    super(new FluxerMessage(event.getMessage()), new FluxerChannel(event.getChannel()), new FluxerUser(event.getAuthor()),
      new FluxerUser(event.getJFA().getSelfUser()), event.isFromGuild() ? new FluxerServer(event.getGuild()) : null,
      new FluxerBat(event.getJFA()), "fluxer");

    this.event = event;
  }

  protected static @NotNull EmbedBuilder getEmbedDS(final @NotNull BatEmbed embed) {
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

  @Override public void sendMessage(final @NotNull String text) {
    event.getChannel().sendMessage(text).queue();
  }

  @Override public void reply(final @NotNull String text, final boolean ping) {
    event.getMessage().reply(text).mentionRepliedUser(ping).queue();
  }

  @Override public void sendEmbed(final @NotNull BatEmbed embed) {
    event.getChannel().sendMessageEmbeds(getEmbedDS(embed).build()).queue();
  }

  @Override public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping) {
    event.getMessage().replyEmbeds(getEmbedDS(embed).build()).mentionRepliedUser(ping).queue();
  }
}