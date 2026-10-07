package lonter.bat.wrappers.fluxer;

import lombok.AllArgsConstructor;
import lombok.val;

import lonter.bat.batobjs.Bat;
import lonter.bat.batobjs.BatUser;
import lonter.jfa.api.JFA;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class FluxerBat extends Bat {
  public final @NotNull JFA jfa;

  @Override public @Nullable BatUser getUserById(final long id) {
    try {
      return new FluxerUser(jfa.retrieveUserById(id).complete());
    }

    catch(final @NotNull Exception e) {
      return null;
    }
  }

  @Override public @Nullable BatUser getUserByTag(final @NotNull String tag) {
    val user = jfa.getUserByTag(tag);
    return user == null ? null : new FluxerUser(user);
  }

  @Override public long getPing() {
    return jfa.getGatewayPing();
  }
}