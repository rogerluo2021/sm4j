package com.kvpair.state.machine.core;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public interface StateTransition<T, R> {

    /**
     * Gets the key of the state transition
     */
    String getKey();

    /**
     * Do the transition and return the result of the transition
     */
    R transfer(T context);

    /**
     * Do something before the state transition, do nothing by default
     */
    default void before(T context) {
    }

    /**
     * Do something after the state transition, do nothing by default
     */
    default void after(T context, R transitionResult) {
    }

}
