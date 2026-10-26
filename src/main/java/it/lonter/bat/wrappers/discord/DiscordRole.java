package it.lonter.bat.wrappers.discord;

import it.lonter.bat.batobjs.BatRole;

import net.dv8tion.jda.api.entities.Role;

import org.jetbrains.annotations.NotNull;

public final class DiscordRole extends BatRole {
  public DiscordRole(final @NotNull Role role) {
    super(role.getIdLong());
  }
}