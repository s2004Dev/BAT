package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import org.jetbrains.annotations.NotNull;

public class DiscordUser extends BatUser {
  public final @NotNull User disc;
  private final boolean localPfp;

  public DiscordUser(final @NotNull User disc) {
    super(disc.getIdLong(), disc.getAsTag(), disc.getGlobalName(), disc.getEffectiveName(),
      disc.getEffectiveAvatarUrl(), disc.getEffectiveAvatarUrl());

    this.disc = disc;
    this.localPfp = false;
  }

  public DiscordUser(final @NotNull Member disc) {
    val user = disc.getUser();

    super(disc.getIdLong(), user.getAsTag(), user.getGlobalName(), disc.getEffectiveName(),
      user.getEffectiveAvatarUrl(), disc.getEffectiveAvatarUrl());

    this.disc = user;
    this.localPfp = disc.getAvatarId() != null;
  }

  @Override public boolean hasLocalPfp() {
    return localPfp;
  }
}