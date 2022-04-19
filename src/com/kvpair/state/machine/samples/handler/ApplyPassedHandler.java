package com.kvpair.state.machine.samples.handler;

import com.kvpair.state.machine.core.AbstractStateTransferHandler;
import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.samples.OtherTypeInput;

import java.util.function.Function;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class ApplyPassedHandler extends AbstractStateTransferHandler<OtherTypeInput, Void> {

    public ApplyPassedHandler(State preState, State nextState) {
        super(preState, nextState);
    }

    @Override
    protected Function<OtherTypeInput, Void> buildHandler() {
        return orderId -> {
            System.out.println("["+preState + "->"+ nextState + "] ApplyPassedHandler executing...");
            return null;
        };
    }

}
