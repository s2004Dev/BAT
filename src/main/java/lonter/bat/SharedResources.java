package lonter.bat;

import lombok.val;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatShard;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
* Utility class for easier management of shared resources.
*/
public abstract class SharedResources {
  private static final int MSG_LENGTH = 1_000;

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Value("${app.prefix}")
  public String prefix;

  @Value("${app.embedColor}")
  public String color;

  @Value("${app.logger:#{false}}")
  public boolean logger;

  private final ConcurrentHashMap<String, BatShard> shards = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, BatServer> servers = new ConcurrentHashMap<>();
  private final Set<String> sources = ConcurrentHashMap.newKeySet();

  private CopyOnWriteArrayList<BatChannel> broadcast = new CopyOnWriteArrayList<>();

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

  public void setShard(final @NotNull String source, final @NotNull BatShard shard) {
    shards.put(source, shard);
  }

  public @NotNull BatShard getShard(final @NotNull String source) {
    val shard = shards.get(source);

    if(shard == null)
      throw new IllegalStateException(source + " shard is null");

    return shard;
  }

  public void initServer(final @NotNull String source) {
    sources.add(source);
  }

  public void serverReady(final @NotNull String source) {
    val server = getShard(source).getServerById(Long.parseLong(getValue(source, "mainServer")));

    if(server == null) {
      log.error("{} main server is null.", source);
      System.exit(-1);
    }

    setServer(source, server);
  }

  public void setServer(final @NotNull String source, final @NotNull BatServer server) {
    if(!sources.contains(source))
      throw new IllegalStateException("`" + source + "` was not initialized: use initServer() first.");

    servers.put(source, server);
  }

  public @NotNull BatServer getServer(final @NotNull String source) {
    val server = servers.get(source);

    if(server == null)
      throw new IllegalStateException(source + " server is null");

    return server;
  }

  public boolean getReady(final @NotNull String source) {
    return servers.get(source) != null;
  }

  public boolean isAwaiting(final @NotNull String source) {
    return sources.contains(source) && !getReady(source);
  }

  public boolean allReady() {
    return !sources.isEmpty() && sources.stream().allMatch(servers::containsKey);
  }

  protected void initBroadcast() {
    if(!allReady())
      return;

    broadcast = new CopyOnWriteArrayList<>(servers.entrySet().stream().map(i -> {
      val source = i.getKey();
      val logger = i.getValue().getChannelById(Long.parseLong(getValue(source, "logger")));

      if(logger == null)
        throw new IllegalStateException("Logger channel for " + source + " not found; it must be in the " +
          "initialized server.");

      return logger;
    }).toList());
  }

  protected void broadcastLog(final @NotNull String log) {
    if(broadcast == null || broadcast.isEmpty())
      return;

    for(val chunk: splitIntoChunks(log))
      broadcast.forEach(i -> i.sendMessage("```ansi\n" + chunk + "\n```"));
  }

  private @NotNull ArrayList<String> splitIntoChunks(final @NotNull String text) {
    val chunks = new ArrayList<String>();
    val chunk = new StringBuilder();

    for(val line: text.split("\n")) {
      if(line.length() > MSG_LENGTH) {
        if(!chunk.isEmpty()) {
          chunks.add(chunk.toString());
          chunk.setLength(0);
        }

        for(var i = 0; i < line.length(); i += MSG_LENGTH)
          chunks.add(line.substring(i, Math.min(line.length(), i + MSG_LENGTH)));

        continue;
      }

      if(chunk.length()+line.length()+1 > MSG_LENGTH) {
        chunks.add(chunk.toString());
        chunk.setLength(0);
      }

      if(!chunk.isEmpty())
        chunk.append("\n");

      chunk.append(line);
    }

    if(!chunk.isEmpty())
      chunks.add(chunk.toString());

    return chunks;
  }
}