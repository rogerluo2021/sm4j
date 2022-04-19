package com.kvpair.state.machine.core;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public abstract class AbstractStateTransition<T, R> implements StateTransition<T, R> {

    protected State preState;
    protected State nextState;

    public AbstractStateTransition() {
    }

    public AbstractStateTransition(State preState, State nextState) {
        this.preState = preState;
        this.nextState = nextState;
    }

    @Override
    public String getKey() {
        return StateMachine.buildStateTransferRelationshipKey(preState, nextState);
    }

    @Override
    public R transfer(T context) {
        return doIt(context);
    }

    protected abstract R doIt(T context);

}
