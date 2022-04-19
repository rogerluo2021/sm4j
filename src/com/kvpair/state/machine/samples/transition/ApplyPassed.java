package com.kvpair.state.machine.samples.transition;

import com.kvpair.state.machine.core.AbstractStateTransition;
import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.samples.OtherTypeInput;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ApplyPassed extends AbstractStateTransition<OtherTypeInput, Void> {

    public ApplyPassed(State preState, State nextState) {
        super(preState, nextState);
    }

    @Override
    public void before(OtherTypeInput context) {
        System.out.println("before ApplyPassedHandler executing...");
    }

    @Override
    public void after(OtherTypeInput context, Void transitionResult) {
        System.out.println("after ApplyPassedHandler executing...");
    }

    @Override
    protected Void doIt(OtherTypeInput context) {
        System.out.println("[" + preState + "->" + nextState + "] ApplyPassedHandler executing...");
        return null;
    }

}
