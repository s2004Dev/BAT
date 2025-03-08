package lonter.bat.annotations.rets.impls;

import lonter.bat.annotations.rets.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@ImplRet
public final class Reply extends ReturnType {
  @Override
  public void action(final @NotNull MessageReceivedEvent e, final @NotNull Object output,
                     final @NotNull Annotation at) {
    if(output instanceof String s && at instanceof lonter.bat.annotations.rets.ats.Reply reply) {
      e.getMessage().reply(s).mentionRepliedUser(reply.value()).queue();
    }
  }
}