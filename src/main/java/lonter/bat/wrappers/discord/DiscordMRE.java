package lonter.bat.wrappers.discord;

import lonter.bat.batobjs.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public final class DiscordMRE extends BatMRE {
  public DiscordMRE(final @NotNull MessageReceivedEvent event) {
    super(new DiscordMessage(event.getMessage()), new DiscordChannel(event.getChannel()),
      new DiscordUser(event.getAuthor()), new DiscordUser(event.getJDA().getSelfUser()),
      event.isFromGuild() ? new DiscordServer(event.getGuild()) : null, new DiscordBat(event.getJDA()), "discord");
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
}