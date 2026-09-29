package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class BatMessageReceivedEvent {
  public final @NotNull BatMessage message;
  public final @NotNull BatUser author;
  public final @NotNull BatUser self;
  public final @Nullable BatServer server;
  public final @NotNull Bat bat;

  public abstract void sendMessage(final @NotNull String text);
  public abstract void sendEmbed(final @NotNull BatEmbed embed);
  public abstract void reply(final @NotNull String text, final boolean ping);
  public abstract void replyEmbed(final @NotNull BatEmbed embed, final boolean ping);
}