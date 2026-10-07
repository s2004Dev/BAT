package lonter.bat.wrappers.discord;

import lonter.bat.batobjs.BatRole;

import net.dv8tion.jda.api.entities.Role;

import org.jetbrains.annotations.NotNull;

public class DiscordRole extends BatRole {
  public DiscordRole(final @NotNull Role role) {
    super(role.getIdLong());
  }
}