package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class BatUser {
  public final long id;
  public final @NotNull String handle;
  public final @NotNull String globalName; // gives handle if not set
  public final @NotNull String localName; // gives globalName if not set
  public final @NotNull String asMention;
  public final @NotNull String globalPfpUrl;
  public final @Nullable String localPfpUrl; // gives globalPfpUrl if not set

  public BatUser(final long id, final @NotNull String handle, final @Nullable String globalName,
                 final @Nullable String localName, final @NotNull String asMention, final @NotNull String globalPfpUrl,
                 final @Nullable String localPfpUrl) {
    this.id = id;
    this.handle = handle;
    this.globalName = globalName == null ? handle : globalName;
    this.localName = localName == null ? this.globalName : localName;
    this.asMention = asMention;
    this.globalPfpUrl = globalPfpUrl;
    this.localPfpUrl = localPfpUrl == null ? this.globalPfpUrl : localPfpUrl;
  }

  public abstract boolean hasLocalPfp();
  public abstract @NotNull ArrayList<BatRole> getRoles();

  public boolean hasRole(final long id) {
    return getRoles().stream().anyMatch(i -> i.id == id);
  }

  public boolean hasRole(final @NotNull BatRole role) {
    return hasRole(role.id);
  }

  public abstract boolean isBot();
}