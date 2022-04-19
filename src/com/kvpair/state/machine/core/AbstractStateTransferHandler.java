package com.kvpair.state.machine.core;

import java.util.function.Function;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public abstract class AbstractStateTransferHandler<T, R> implements StateTransferHandler<T, R> {

    protected State preState;
    protected State nextState;
    protected Function<T, R> handler;

    public AbstractStateTransferHandler() {
    }

    public AbstractStateTransferHandler(State preState, State nextState) {
        this.preState = preState;
        this.nextState = nextState;
    }

    @Override
    public String getKey() {
        return StateMachine.buildStateTransferRelationshipKey(preState, nextState);
    }

    @Override
    public Function<T, R> getHandler() {
        return buildHandler();
    }

    protected abstract Function<T, R> buildHandler();

}
