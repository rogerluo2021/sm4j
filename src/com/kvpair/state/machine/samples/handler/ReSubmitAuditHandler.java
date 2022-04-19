package com.kvpair.state.machine.samples.handler;

import com.kvpair.state.machine.core.AbstractStateTransferHandler;
import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.samples.Apply;

import java.util.function.Function;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ReSubmitAuditHandler extends AbstractStateTransferHandler<Apply, Long> {

    public ReSubmitAuditHandler(State preState, State nextState) {
        super(preState, nextState);
    }

    @Override
    protected Function<Apply, Long> buildHandler() {
        return order -> {
            System.out.println("["+preState + "->"+ nextState + "] ReSubmitAuditHandler executing...");
            return null;
        };
    }

}
