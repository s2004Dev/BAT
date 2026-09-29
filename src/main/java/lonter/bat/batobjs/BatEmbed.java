package lonter.bat.batobjs;

import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public class BatEmbed {
  public @Nullable String title;
  public @Nullable String description;
  public @Nullable String footer;
  public @Nullable String color;
  public @Nullable String imageUrl;
  public @Nullable String thumbnailUrl;

  public BatEmbed(final @NotNull String title, final @NotNull String description, final @NotNull String footer,
                  final @NotNull String color) {
    this.title = title;

    try {
      this.color = color;
    } catch (final @NotNull Exception _) { }

    this.description = description;
    this.footer = footer;
  }

  public BatEmbed(final @NotNull String title, final @NotNull String description, final @NotNull String color) {
    this(title, description, "", color);
  }
}