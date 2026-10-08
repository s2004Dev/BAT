package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@AllArgsConstructor
public abstract class BatChannel {
  public final long id;
  public final @NotNull String name;
  public final @NotNull String asMention;

  public void sendMessage(final @NotNull String text) {
    sendMessage(text, null);
  }

  public abstract void sendMessage(final @NotNull String text, final @Nullable Consumer<BatMessage> success);

  public void sendEmbed(final @NotNull BatEmbed embed) {
    sendEmbed(embed, null);
  }

  public abstract void sendEmbed(final @NotNull BatEmbed embed, final @Nullable Consumer<BatMessage> success);

  public abstract void editMessageById(final long id, final @NotNull String text,
                                       final @Nullable Consumer<BatMessage> success);

  public abstract void editMessageEmbedsById(final long id, final @NotNull BatEmbed embed,
                                             final @Nullable Consumer<BatMessage> success);
}