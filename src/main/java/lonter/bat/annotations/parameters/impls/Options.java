package lonter.bat.annotations.parameters.impls;

import lombok.val;

import lonter.bat.annotations.parameters.CommandArg;
import lonter.bat.annotations.parameters.ImplParam;
import lonter.bat.annotations.parameters.impls.auxiliary.Option;
import lonter.bat.batobjs.BatMRE;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

@ImplParam
public class Options extends CommandArg {
  @Override public @NotNull Class<? extends Annotation> getAnnotationType() {
    return lonter.bat.annotations.parameters.ats.Options.class;
  }

  @Override public @NotNull Object value(@NotNull BatMRE e, final @NotNull Annotation at) {
    if(!(at instanceof lonter.bat.annotations.parameters.ats.Options options)) {
      System.err.println("An error occurred while injecting @Options.");
      return new String[] { };
    }

    val input = e.message.text;
    val opts = new ArrayList<Option>();

    for(val raw: removeCommand(options.value() ? input : input.toLowerCase().replaceAll("\\s+", " "))) {
      if(raw.startsWith(options.separator()))
        opts.add(new Option(raw.substring(2).toLowerCase(), ""));

      else if(!opts.isEmpty()) {
        val option = opts.getLast();
        val value = option.value();
        val opt = option.opt();

        opts.set(opts.size()-1, new Option(opt, value.isBlank() ? raw : value + " " + raw));
      }
    }

    return opts;
  }
}