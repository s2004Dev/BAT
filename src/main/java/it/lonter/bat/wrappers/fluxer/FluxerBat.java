package it.lonter.bat.wrappers.fluxer;

import lombok.AllArgsConstructor;
import lombok.val;

import it.lonter.bat.batobjs.Bat;
import it.lonter.bat.batobjs.BatUser;
import it.lonter.jfa.api.JFA;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@AllArgsConstructor
public final class FluxerBat extends Bat {
  public final @NotNull JFA jfa;

  @Override public @NotNull Optional<BatUser> getUserById(final long id) {
    try {
      return Optional.of(new FluxerUser(jfa.retrieveUserById(id).complete()));
    }

    catch(final @NotNull Exception e) {
      return Optional.empty();
    }
  }

  @Override public @NotNull Optional<BatUser> getUserByTag(final @NotNull String tag) {
    val user = jfa.getUserByTag(tag);
    return Optional.ofNullable(user == null ? null : new FluxerUser(user));
  }

  @Override public long getPing() {
    return jfa.getGatewayPing();
  }
}