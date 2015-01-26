package net.indiespot.struct;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class StructAgent {
    static final String AGENT_DELEGATE_FQN = "net.indiespot.struct.StructAgentDelegate";

    public static void premain(String args, Instrumentation inst) {
        System.out.println("StructAgent: initiating...");

        try {
            invokeAgentDelegate(args, inst);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    static void invokeAgentDelegate(String args, Instrumentation instrumentation) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> cls = Class.forName(AGENT_DELEGATE_FQN);

        if (!AgentDelegate.class.isAssignableFrom(cls)) {
            throw new ClassCastException(cls + " not an instanceof AgentDelegate");
        }
        Constructor<AgentDelegate> ctor = ((Class<AgentDelegate>) cls).getConstructor();

        AgentDelegate delegate = ctor.newInstance();

        delegate.premain(args, instrumentation);

    }
}