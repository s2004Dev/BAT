package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import org.jetbrains.annotations.NotNull;

public class DiscordUser extends BatUser {
  public final @NotNull User disc;

  public DiscordUser(final @NotNull User disc) {
    super(disc.getIdLong(), disc.getAsTag(), disc.getGlobalName(), disc.getEffectiveName(), disc.getAvatarId(),
      disc.getAvatarUrl());

    this.disc = disc;
  }

  public DiscordUser(final @NotNull Member disc) {
    val user = disc.getUser();

    super(disc.getIdLong(), user.getAsTag(), user.getGlobalName(), disc.getEffectiveName(), disc.getAvatarId(),
      disc.getAvatarUrl());

    this.disc = user;
  }

  @Override public boolean hasLocalPfp() {
    return disc.getAvatarId() != null;
  }
}