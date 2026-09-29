package lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;
import lombok.val;

import lonter.bat.batobjs.Bat;
import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.JDA;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class DiscordBat extends Bat {
  public final @NotNull JDA jda;

  @Override public @Nullable BatUser getUserById(final long id) {
    val disc = jda.retrieveUserById(id).complete();
    return disc == null ? null : new DiscordUser(disc);
  }

  @Override public @Nullable BatUser getUserByTag(final @NotNull String tag) {
    val disc = jda.getUserByTag(tag);
    return disc == null ? null : new DiscordUser(disc);
  }

  @Override public long getPing() {
    return jda.getGatewayPing();
  }
}
