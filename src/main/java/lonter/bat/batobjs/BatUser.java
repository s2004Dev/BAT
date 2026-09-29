package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BatUser {
  public final long id;
  public final @NotNull String handle;
  public final @NotNull String globalName; // gives handle if not set
  public final @NotNull String localName; // gives globalName if not set
  public final @Nullable String globalPfpUrl; // might be null if no pfp is set
  public final @Nullable String localPfpUrl; // gives globalPfpUrl if not set

  public BatUser(final long id, final @NotNull String handle, final @Nullable String globalName,
                 final @Nullable String localName, final @Nullable String globalPfpUrl,
                 final @Nullable String localPfpUrl) {
    this.id = id;
    this.handle = handle;
    this.globalName = globalName == null ? handle : globalName;
    this.localName = localName == null ? this.globalName : localName;
    this.globalPfpUrl = globalPfpUrl;
    this.localPfpUrl = localPfpUrl == null ? this.globalPfpUrl : localPfpUrl;
  }

  public abstract boolean hasLocalPfp();
}