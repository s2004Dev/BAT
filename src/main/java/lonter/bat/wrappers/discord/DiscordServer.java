package lonter.bat.wrappers.discord;

import lombok.val;

import lonter.bat.batobjs.BatChannel;
import lonter.bat.batobjs.BatRole;
import lonter.bat.batobjs.BatServer;
import lonter.bat.batobjs.BatUser;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DiscordServer extends BatServer {
  public final @NotNull Guild guild;

  public DiscordServer(final @NotNull Guild guild) {
    super(guild.getIdLong(), guild.getName());
    this.guild = guild;
  }

  @Override public @Nullable BatUser getMemberById(final long id) {
    val member = guild.getMemberById(id);
    return member == null ? null : new DiscordUser(member);
  }

  @Override public @Nullable BatChannel getChannelById(final long id) {
    val channel = guild.getTextChannelById(id);
    return channel == null ? null : new DiscordChannel(channel);
  }

  @Override public @Nullable BatRole getRoleById(final long id) {
    val role = guild.getRoleById(id);
    return role == null ? null : new DiscordRole(role);
  }

  @Override public void addRoleToMember(final long memberId, final long roleId) {
    guild.addRoleToMember(getMember(memberId), getRole(roleId)).queue();
  }

  @Override public void removeRoleFromMember(final long memberId, final long roleId) {
    guild.removeRoleFromMember(getMember(memberId), getRole(roleId)).queue();
  }

  private @NotNull Member getMember(final long memberId) {
    val member = guild.getMemberById(memberId);

    if(member == null)
      throw new IllegalArgumentException("Member with ID " + memberId + " was not found in discord guild with ID " +
        id + ".");

    return member;
  }

  private @NotNull Role getRole(final long roleId) {
    val role = guild.getRoleById(roleId);

    if(role == null)
      throw new IllegalArgumentException("Role with ID " + roleId + " was not found in discord guild with ID " +
        id + ".");

    return role;
  }
}