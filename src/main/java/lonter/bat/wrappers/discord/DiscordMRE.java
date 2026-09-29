package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DiscordMRE extends BatMessageReceivedEvent {
  private final @NotNull MessageReceivedEvent disc;

  public DiscordMRE(final @NotNull MessageReceivedEvent disc) {
    super(new BatMessage(disc.getMessage().getContentRaw()), new DiscordUser(disc.getAuthor()),
      new DiscordUser(disc.getJDA().getSelfUser()), new DiscordServer(disc.getGuild()),
      new DiscordBat(disc.getJDA()));

    this.disc = disc;
  }

  @Override public void sendMessage(final @NotNull String text) {
    disc.getChannel().sendMessage(text).queue();
  }

  private @NotNull EmbedBuilder getEmbedDS(final @NotNull BatEmbed embed) {
    val embedDS = new EmbedBuilder();

    embedDS.setTitle(embed.title);
    embedDS.setDescription(embed.description);
    embedDS.setFooter(embed.footer);
    embedDS.setImage(embed.imageUrl);
    embedDS.setThumbnail(embed.thumbnailUrl);

    if(embed.color != null)
      embedDS.setColor(Color.decode(embed.color));

    return embedDS;
  }

  @Override public void sendEmbed(final @NotNull BatEmbed embed) {
    disc.getChannel().sendMessageEmbeds(getEmbedDS(embed).build()).queue();
  }

  @Override public void reply(final @NotNull String text, final boolean ping) {
    disc.getMessage().reply(text).mentionRepliedUser(ping).queue();
  }

  @Override public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping) {
    disc.getMessage().replyEmbeds(getEmbedDS(embed).build()).mentionRepliedUser(ping).queue();
  }
}