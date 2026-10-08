package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@AllArgsConstructor
public abstract class BatMessage {
  public long id;
  public @NotNull String text;

  public abstract boolean isSystemPinned();
  public abstract void delete();

  public void reply(final @NotNull String text, final boolean ping) {
    reply(text, ping, null);
  }

  public abstract void reply(final @NotNull String text, final boolean ping,
                             final @Nullable Consumer<BatMessage> success);

  public void replyEmbed(final @NotNull BatEmbed embed, final boolean ping) {
    replyEmbed(embed, ping, null);
  }

  public abstract void replyEmbed(final @NotNull BatEmbed embed, final boolean ping,
                                  final @Nullable Consumer<BatMessage> success);
}