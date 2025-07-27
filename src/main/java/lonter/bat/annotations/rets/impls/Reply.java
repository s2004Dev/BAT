package lonter.bat.annotations.rets.impls;

import lonter.bat.annotations.rets.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.lang.annotation.Annotation;

@ImplRet @Component
public final class Reply extends ReturnType {
  @Value("${app.embedColor:#{null}}")
  private String color;

  @Override
  public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.rets.ats.Reply.class;
  }

  @Override
  public void action(final @NotNull MessageReceivedEvent e, final @NotNull Object output,
                     final @NotNull Annotation at) {
    if(!(at instanceof lonter.bat.annotations.rets.ats.Reply reply)) {
      System.err.println("An error occurred in Reply action.");
      return;
    }

    switch(output) {
      case String s -> e.getMessage().reply(s).mentionRepliedUser(reply.value()).queue();

      case EmbedBuilder embed -> {
        if(embed.build().getColor() == null) {
          try {
            embed.setColor(Color.decode(color));
          }

          catch(final @NotNull Exception _) { }
        }

        e.getMessage().replyEmbeds(embed.build()).queue();
      }

      default -> {
        System.err.println("The output type was not recognized.");
        System.err.println(output);
        System.err.println(at);
      }
    }
  }
}