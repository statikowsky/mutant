package org.droba.mutantEye;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.droba.mutantEye.advice.BasicAdvice;

import javax.servlet.http.HttpServlet;

public class startEyeAgentBuddy {

    public startEyeAgentBuddy() {

        ByteBuddyAgent.install();

        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader) -> builder
                .visit(Advice.to(BasicAdvice.class).on(ElementMatchers.any()));

        new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.isSubTypeOf(HttpServlet.class))
                .transform(transformer)
                .installOnByteBuddyAgent();
    }

}
