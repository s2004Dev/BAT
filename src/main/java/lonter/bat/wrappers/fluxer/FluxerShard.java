package lonter.bat.wrappers.fluxer;

import lombok.AllArgsConstructor;
import lombok.val;

import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatShard;
import lonter.jfa.api.sharding.ShardManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class FluxerShard extends BatShard {
  public final @NotNull ShardManager shard;

  @Override public @Nullable BatServer getServerById(final long id) {
    val guild = shard.getGuildById(id);
    return guild == null ? null : new FluxerServer(guild);
  }
}