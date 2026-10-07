package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class BatChannel {
  public final long id;
  public final @NotNull String name;
  public final @NotNull String asMention;

  public abstract void sendMessage(final @NotNull String text);
  public abstract void sendEmbed(final @NotNull BatEmbed embed);
}