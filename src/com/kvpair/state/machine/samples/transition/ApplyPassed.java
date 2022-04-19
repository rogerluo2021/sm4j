package com.kvpair.state.machine.samples.transition;

import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.core.StateTransition;
import com.kvpair.state.machine.samples.ApplyState;
import com.kvpair.state.machine.samples.OtherTypeInput;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ApplyPassed implements StateTransition<OtherTypeInput, Void> {

    @Override
    public State getPreState() {
        return ApplyState.TO_AUDIT;
    }

    @Override
    public State getNextState() {
        return ApplyState.PASSED;
    }

    @Override
    public Void transfer(OtherTypeInput context) {
        System.out.println("[" + getPreState() + "->" + getNextState() + "] ApplyFallback...");
        return null;
    }

    @Override
    public void before(OtherTypeInput context) {
        System.out.println("check any other preconditions...");
    }

    @Override
    public void after(OtherTypeInput context, Void transitionResult) {
        System.out.println("after ApplyFallback executed...");
    }

}
