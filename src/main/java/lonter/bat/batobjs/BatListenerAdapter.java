package lonter.bat.batobjs;

import org.jetbrains.annotations.NotNull;

public class BatListenerAdapter implements BatListener {
  @Override public void onMessageReceived(final @NotNull BatMRE e) { }
  @Override public void onServerReady(final @NotNull String source) { }
  @Override public void onMessageReaction(final @NotNull BatGRE e) { }
  @Override public void onMemberJoinLeave(final @NotNull BatGGE e) { }
  @Override public void onServerMemberRoleChange(final @NotNull BatRCE e) { }
}