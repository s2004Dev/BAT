package lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;
import lombok.val;

import lonter.bat.batobjs.Bat;
import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.JDA;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public final class DiscordBat extends Bat {
  public final @NotNull JDA jda;

  @Override public @Nullable BatUser getUserById(final long id) {
    try {
      return new DiscordUser(jda.retrieveUserById(id).complete());
    }

    catch(final @NotNull Exception e) {
      return null;
    }
  }

  @Override public @Nullable BatUser getUserByTag(final @NotNull String tag) {
    val user = jda.getUserByTag(tag);
    return user == null ? null : new DiscordUser(user);
  }

  @Override public long getPing() {
    return jda.getGatewayPing();
  }
}