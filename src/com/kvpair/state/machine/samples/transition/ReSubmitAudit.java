package com.kvpair.state.machine.samples.transition;

import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.core.StateTransition;
import com.kvpair.state.machine.samples.Apply;
import com.kvpair.state.machine.samples.ApplyState;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ReSubmitAudit implements StateTransition<Apply, Long> {

    @Override
    public State getPreState() {
        return ApplyState.FALLBACK;
    }

    @Override
    public State getNextState() {
        return ApplyState.TO_AUDIT;
    }

    @Override
    public Long transfer(Apply context) {
        System.out.println("[" + getPreState() + "->" + getNextState() + "] ReSubmitAudit...");
        return null;
    }

}
