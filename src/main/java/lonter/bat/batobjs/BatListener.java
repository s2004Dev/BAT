package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;

public interface BatListener  {
  void onMessageReceived(final @NotNull BatMRE e);
  void onGuildReady(final @NotNull String source);
  void onMessageReaction(final @NotNull BatGRE e);
  void onMemberJoinLeave(final @NotNull BatGGE e);
  void onGuildMemberRoleAdd(final @NotNull BatRCE e);
}