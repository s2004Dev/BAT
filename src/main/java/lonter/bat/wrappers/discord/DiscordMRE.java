package lonter.bat.wrappers.discord;

import lonter.bat.batobjs.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Optional;

public final class DiscordMRE extends BatMRE {
  public DiscordMRE(final @NotNull MessageReceivedEvent event) {
    super(new DiscordMessage(event.getMessage()), new DiscordChannel(event.getChannel()),
      new DiscordUser(event.getAuthor()), new DiscordUser(event.getJDA().getSelfUser()),
      Optional.ofNullable(event.isFromGuild() ? new DiscordServer(event.getGuild()) : null),
      new DiscordBat(event.getJDA()), "discord");
  }

  public static @NotNull EmbedBuilder getEmbedDS(final @NotNull BatEmbed embed) {
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