package lonter.bat.wrappers.fluxer;

import lombok.val;

import lonter.bat.batobjs.BatRole;
import lonter.bat.batobjs.BatUser;
import lonter.jfa.api.entities.Member;
import lonter.jfa.api.entities.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class FluxerUser extends BatUser {
  private final @NotNull User user;
  private final @Nullable Member member;
  private final boolean localPfp;

  public FluxerUser(final @NotNull User user) {
    super(user.getIdLong(), user.getAsTag(), user.getGlobalName(), user.getEffectiveName(), user.getAsMention(),
      user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());

    this.user = user;
    this.member = null;
    this.localPfp = false;
  }

  public FluxerUser(final @NotNull Member member) {
    val user = member.getUser();

    super(member.getIdLong(), user.getAsTag(), user.getGlobalName(), member.getEffectiveName(), user.getAsMention(),
      user.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl());

    this.user = user;
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

  @Override public boolean isBot() {
    return user.isBot();
  }
}