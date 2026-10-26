package it.lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;
import lombok.val;

import it.lonter.bat.batobjs.Bat;
import it.lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.JDA;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@AllArgsConstructor
public final class DiscordBat extends Bat {
  public final @NotNull JDA jda;

  @Override public @NotNull Optional<BatUser> getUserById(final long id) {
    try {
      return Optional.of(new DiscordUser(jda.retrieveUserById(id).complete()));
    }

    catch(final @NotNull Exception e) {
      return Optional.empty();
    }
  }

  @Override public @NotNull Optional<BatUser> getUserByTag(final @NotNull String tag) {
    val user = jda.getUserByTag(tag);
    return Optional.ofNullable(user == null ? null : new DiscordUser(user));
  }

  @Override public long getPing() {
    return jda.getGatewayPing();
  }
}