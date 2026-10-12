package lonter.bat.wrappers.discord;

import lombok.AllArgsConstructor;
import lombok.val;

import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatShard;

import net.dv8tion.jda.api.sharding.ShardManager;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@AllArgsConstructor
public final class DiscordShard extends BatShard {
  public final @NotNull ShardManager shard;

  @Override public @NotNull Optional<BatServer> getServerById(final long id) {
    val guild = shard.getGuildById(id);
    return Optional.ofNullable(guild == null ? null : new DiscordServer(guild));
  }
}