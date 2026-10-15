package lonter.bat;

import lombok.val;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatShard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
  private final Logger log = LoggerFactory.getLogger(getClass());

  /**
  * This mandatory property contains the prefix of the bot.
  */
  @Value("${app.prefix}")
  public @NotNull String prefix;

  /**
   * This optional property contains the default color for embeds, it is set as `null` by default. Must be a
   * hexadecimal.
   */
  @Value("${app.embedColor:#{null}}")
  public @Nullable String color;

  /**
   * This optional property toggles the framework logger, it is set as `false` by default.
   */
  @Value("${app.logger:#{false}}")
  public boolean logger;

  /**
   * This optional property tells the framework whether the commands should ignore bots or not, it is set as `true`
   * by default.
   */
  @Value("${app.ignoreBots:#{true}}")
  public boolean ignoreBots;

  /**
   * This optional property tells the framework how long the message of a log can be, it is set as `1000` by default.
   */
  @Value("${app.logLength:#{1000}}")
  public int logLength;

  private final ConcurrentHashMap<String, BatShard> shards = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, BatServer> servers = new ConcurrentHashMap<>();
  private final Set<String> sources = ConcurrentHashMap.newKeySet();

  private CopyOnWriteArrayList<BatChannel> broadcast = new CopyOnWriteArrayList<>();

  private final Environment env;

  public SharedResources(final @NotNull Environment env) {
    this.env = env;
  }

  /**
   * @param source the source of the property
   * @param name the name of the property
   * @return the value of the property
   * @throws IllegalStateException if the property is null
   */
  public @NotNull String getValue(final @NotNull String source, final @NotNull String name) {
    val property = "app." + source + "." + name;
    val value = env.getProperty(property);

    if(value == null)
      throw new IllegalStateException(property + " is null");

    return value;
  }

  /**
   * @param source the source of the property
   * @param name the name of the property
   * @return the value of the property or null
   */
  public @Nullable String getValueOrNull(final @NotNull String source, final @NotNull String name) {
    return env.getProperty("app." + source + "." + name);
  }

  /**
   * @param source the source of the shard
   * @param shard the shard to put into memory
   */
  public void setShard(final @NotNull String source, final @NotNull BatShard shard) {
    shards.put(source, shard);
  }

  /**
   * @param source the source of the shard
   * @return the shard for that specific source
   * @throws IllegalStateException if the shard is not found
   */
  public @NotNull BatShard getShard(final @NotNull String source) {
    val shard = getShardOrNull(source);

    if(shard == null)
      throw new IllegalStateException(source + " shard is null");

    return shard;
  }

  /**
   * @param source the source of the shard
   * @return the shard for that specific source or null
   */
  public @Nullable BatShard getShardOrNull(final @NotNull String source) {
    return shards.get(source);
  }

  /**
   * Use this method to tell the framework it should expect a server for this source to be initialized.
   * Remember to define an `app.source.mainServer` and an `app.source.logger`.
   * @param source the source of the server that is initializing
   */
  public void initServer(final @NotNull String source) {
    sources.add(source);
  }

  public void serverReady(final @NotNull String source) {
    val server = getShard(source).getServerById(Long.parseLong(getValue(source, "mainServer")));

    if(server.isEmpty()) {
      log.error("{} main server is null.", source);
      System.exit(-1);
    }

    setServer(source, server.get());
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

      return i.getValue().getChannelById(Long.parseLong(getValue(source, "logger"))).orElseThrow(( ) ->
        new IllegalStateException("Logger channel for " + source + " not found; it must be in the initialized " +
          "server."));
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
      if(line.length() > logLength) {
        if(!chunk.isEmpty()) {
          chunks.add(chunk.toString());
          chunk.setLength(0);
        }

        for(var i = 0; i < line.length(); i += logLength)
          chunks.add(line.substring(i, Math.min(line.length(), i + logLength)));

        continue;
      }

      if(chunk.length()+line.length()+1 > logLength) {
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