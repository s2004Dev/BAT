package lonter.bat.batobjs;

import lombok.AllArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class BatServer {
  public final long id;
  public final @NotNull String name;

  public abstract @Nullable BatUser getMemberById(final long id);
  public abstract @Nullable BatChannel getChannelById(final long id);
  public abstract @Nullable BatRole getRoleById(final long id);
  public abstract void addRoleToMember(final long memberId, final long roleId);

  public void addRoleToMember(final @NotNull BatUser member, final @NotNull BatRole role) {
    addRoleToMember(member.id, role.id);
  }

  public abstract void removeRoleFromMember(final long memberId, final long roleId);

  public void removeRoleFromMember(final @NotNull BatUser member, final @NotNull BatRole role) {
    removeRoleFromMember(member.id, role.id);
  }
}