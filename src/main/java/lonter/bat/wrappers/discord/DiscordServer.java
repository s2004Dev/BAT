package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.entities.Guild;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordServer extends BatServer {
  public final @NotNull Guild guild;

  public DiscordServer(final @NotNull Guild guild) {
    super(guild.getIdLong());
    this.guild = guild;
  }

  @Override public @Nullable BatUser getMemberById(final long id) {
    val disc = guild.getMemberById(id);
    return disc == null ? null : new DiscordUser(disc);
  }
}