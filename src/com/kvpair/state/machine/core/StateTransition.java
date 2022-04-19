package com.kvpair.state.machine.core;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public interface StateTransition<T, R> {

    /**
     * Gets the previous state
     */
    State getPreState();

    /**
     * Gets the next state
     */
    State getNextState();

    /**
     * Gets the key of the state transition
     */
    default String getKey() {
        return StateMachine.buildStateTransferRelationshipKey(getPreState(), getNextState());
    }

    /**
     * Do the transition and return the result of the transition
     */
    R transfer(T context);

    /**
     * If you wanna do anything before the state transition,
     * you can override the mehtod, for example,you can check any other preconditions in it,
     * do nothing by default
     */
    default void before(T context) {
    }

    /**
     * If you wanna do anything after the state transition,
     * you can override the mehtod, for example,
     * you can logging it or notify other components who care the state transition, etc
     * do nothing by default
     */
    default void after(T context, R transitionResult) {
    }

}
