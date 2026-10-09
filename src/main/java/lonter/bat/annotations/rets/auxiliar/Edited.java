package lonter.bat.annotations.rets.auxiliar;

import lombok.AllArgsConstructor;

import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@AllArgsConstructor
public final class Edited {
  public final @Nullable Long id;
  public final @NotNull BatEmbed embed;
  public final @Nullable Consumer<BatMessage> action;
}