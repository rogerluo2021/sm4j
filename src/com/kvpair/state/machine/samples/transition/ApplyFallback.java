package com.kvpair.state.machine.samples.transition;

import com.kvpair.state.machine.core.AbstractStateTransition;
import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.samples.Apply;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ApplyFallback extends AbstractStateTransition<Apply, Long> {

    public ApplyFallback(State preState, State nextState) {
        super(preState, nextState);
    }

    @Override
    protected Long doIt(Apply context) {
        System.out.println("["+preState + "->"+ nextState + "] ApplyFallbackHandler executing...");
        return null;
    }
}
