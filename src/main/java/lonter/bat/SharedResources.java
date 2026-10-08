package lonter.bat;

import lombok.val;

import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatShard;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.HashMap;

/**
* Utility class for easier management of shared resources.
*/
public abstract class SharedResources {
  @Value("${app.prefix}")
  public String prefix;

  @Value("${app.embedColor}")
  public String color;

  private final HashMap<String, BatServer> servers = new HashMap<>();
  private final HashMap<String, BatShard> shards = new HashMap<>();

  private final Environment env;

  public SharedResources(final @NotNull Environment env) {
    this.env = env;
  }

  public @NotNull String getValue(final @NotNull String source, final @NotNull String name) {
    val property = "app." + source + "." + name;
    val value = env.getProperty(property);

    if(value == null)
      throw new IllegalStateException(property + " is null");

    return value;
  }

  public void setServer(final @NotNull String source, final @NotNull BatServer server) {
    servers.put(source, server);
  }

  public @NotNull BatServer getServer(final @NotNull String source) {
    val server = servers.get(source);

    if(server == null)
      throw new IllegalStateException(source + " server is null");

    return server;
  }

  public void setShard(final @NotNull String source, final @NotNull BatShard shard) {
    shards.put(source, shard);
  }

  public @NotNull BatShard getShard(final @NotNull String source) {
    val shard = shards.get(source);

    if(shard == null)
      throw new IllegalStateException(source + " shard is null");

    return shard;
  }

  public boolean getReady(final @NotNull String source) {
    return servers.get(source) != null;
  }
}