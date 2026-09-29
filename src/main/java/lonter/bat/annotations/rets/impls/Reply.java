package lonter.bat.annotations.rets.impls;

import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMessageReceivedEvent;
import lonter.bat.annotations.rets.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ImplRet @Component
public final class Reply extends ReturnType {
  @Value("${app.embedColor:#{null}}")
  private String color;

  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.rets.ats.Reply.class;
  }

  @Override public void action(final @NotNull BatMessageReceivedEvent e, final @NotNull Object output,
                               final @NotNull Annotation at) {
    if(!(at instanceof lonter.bat.annotations.rets.ats.Reply reply)) {
      System.err.println("An error occurred in Reply action.");
      return;
    }

    switch(output) {
      case String s -> e.reply(s, reply.value());

      case BatEmbed embed -> {
        if(embed.color == null) {
          try {
            embed.color = color;
          }

          catch(final @NotNull Exception _) { }
        }

        e.replyEmbed(embed, reply.value());
      }

      default -> {
        System.err.println("The output type was not recognized.");
        System.err.println(output);
        System.err.println(at);
      }
    }
  }
}