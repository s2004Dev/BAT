package lonter.bat.annotations.rets.impls;

import lonter.bat.batobjs.BatEmbed;
import lonter.bat.batobjs.BatMRE;
import lonter.bat.annotations.rets.*;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ImplRet @Component
public final class Reply extends ReturnType {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Value("${app.embedColor:#{null}}")
  private String color;

  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.rets.ats.Reply.class;
  }

  @Override public void action(final @NotNull BatMRE e, final @NotNull Object output,
                               final @NotNull Annotation at) {
    if(!(at instanceof lonter.bat.annotations.rets.ats.Reply reply)) {
      log.error("An error occurred in Reply action.");
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
        log.error("The output type was not recognized.");
        log.error("{}", output);
        log.error("{}", at);
      }
    }
  }
}