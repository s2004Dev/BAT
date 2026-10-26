package it.lonter.bat.wrappers.fluxer;

import lombok.val;

import it.lonter.bat.batobjs.BatRole;
import it.lonter.bat.batobjs.BatUser;
import it.lonter.jfa.api.entities.Member;
import it.lonter.jfa.api.entities.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public final class FluxerUser extends BatUser {
  private final @Nullable Member member;
  private final boolean localPfp;

  public FluxerUser(final @NotNull User user) {
    super(user.getIdLong(), user.getAsTag(), user.getGlobalName(), user.getEffectiveName(), user.getAsMention(),
      user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl(), user.getTimeCreated().toLocalDateTime(),
      user.isBot());

    this.member = null;
    this.localPfp = false;
  }

  public FluxerUser(final @NotNull Member member) {
    val user = member.getUser();

    super(member.getIdLong(), user.getAsTag(), user.getGlobalName(), member.getEffectiveName(),
      user.getAsMention(), user.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl(),
      user.getTimeCreated().toLocalDateTime(), user.isBot());

    this.member = member;
    this.localPfp = member.getAvatarId() != null;
  }

  @Override public boolean hasLocalPfp() {
    return localPfp;
  }

  private @NotNull Optional<Member> getMember() {
    return Optional.ofNullable(member);
  }

  @Override public @NotNull ArrayList<BatRole> getRoles() {
    return getMember().map(m -> new ArrayList<BatRole>(m.getRoles().stream().map(FluxerRole::new).toList()))
      .orElseGet(ArrayList::new);
  }
}