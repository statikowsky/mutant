package org.droba.mutantEye.advice;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class BasicAdvice {

    @Advice.OnMethodEnter
    static void enter(@Advice.Origin("#t.#m") String signature) {
        // enter
    }

    @Advice.OnMethodExit
    static void exit() {
        // exit
    }
}
