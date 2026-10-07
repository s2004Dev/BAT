package lonter.bat.wrappers.discord;

import static lonter.bat.wrappers.discord.DiscordMRE.getEmbedDS;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatEmbed;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordChannel extends BatChannel {
  public @Nullable final MessageChannelUnion unionChannel;
  public @Nullable final TextChannel textChannel;

  public DiscordChannel(final @NotNull MessageChannelUnion channel) {
    super(channel.getIdLong(), channel.getName(), channel.getAsMention());

    unionChannel = channel;
    textChannel = null;
  }

  public DiscordChannel(final @NotNull TextChannel channel) {
    super(channel.getIdLong(), channel.getName(), channel.getAsMention());

    unionChannel = null;
    textChannel = channel;
  }

  @Override public void sendMessage(final @NotNull String text) {
    if(unionChannel == null && textChannel == null)
      throw new IllegalStateException("Both unionChannel and textChannel are null");

    (unionChannel == null ? textChannel : unionChannel).sendMessage(text).queue();
  }

  @Override public void sendEmbed(final @NotNull BatEmbed embed) {
    if(unionChannel == null && textChannel == null)
      throw new IllegalStateException("Both unionChannel and textChannel are null");

    (unionChannel == null ? textChannel : unionChannel).sendMessageEmbeds(getEmbedDS(embed).build()).queue();
  }
}