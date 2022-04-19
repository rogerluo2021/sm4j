package com.kvpair.state.machine.core;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class StateMachine {

    private Map<String, StateTransition> transitionMapping;
    private StateTransitionDefinition stateTransitionDefinition;

    private StateMachine(Map<String, StateTransition> transitionMapping, StateTransitionDefinition stateTransitionDefinition) {
        this.transitionMapping = transitionMapping;
        this.stateTransitionDefinition = stateTransitionDefinition;
    }

    public <T, R> R start(T stateTransitionContext, State preState, State nextState) {
        if (preState == null || preState.getValue() == null) {
            throw new IllegalArgumentException("Neither the pre state and the value of the pre state should be null");
        }
        if (nextState == null || nextState.getValue() == null) {
            throw new IllegalArgumentException("Neither the next state and the value of the next state should be null");
        }
        StateTransition<T, R> stateTransition = transitionMapping.get(buildStateTransferRelationshipKey(preState, nextState));
        if (stateTransition == null) {
            throw new IllegalArgumentException(String.format("can't find the matched state transition instance with the pre state '%s' and the next '%s'", preState, nextState));
        }
        boolean canTransfer = stateTransitionDefinition.canTransfer(preState, nextState);
        if (!canTransfer) {
            throw new IllegalArgumentException(String.format("can't transfer from '%s' to '%s'", preState, nextState));
        }
        stateTransition.before(stateTransitionContext);
        R result;
        result = stateTransition.transfer(stateTransitionContext);
        stateTransition.after(stateTransitionContext, result);
        return result;
    }

    public static String buildStateTransferRelationshipKey(State preState, State nextState) {
        return preState + "->" + nextState;
    }

    public static class Builder {
        private Vector<State> stateVector;
        private byte[][] stateTransferMatrix;
        private List<StateTransition> stateTransitions;

        public Builder() {
        }

        public Builder stateVector(Vector<State> stateVector) {
            this.stateVector = stateVector;
            return this;
        }

        public Builder stateTransferMatrix(byte[][] stateTransferMatrix) {
            this.stateTransferMatrix = stateTransferMatrix;
            return this;
        }

        public Builder stateTransitions(List<StateTransition> stateTransitions) {
            this.stateTransitions = stateTransitions;
            return this;
        }

        public StateMachine build() {
            checkParameters();
            StateTransitionDefinition stateTransitionDefinition = new StateTransitionDefinition(stateVector, stateTransferMatrix);
            Map<String, StateTransition> transitionMapping = stateTransitions.stream().collect(Collectors.toMap(StateTransition::getKey, Function.identity()));
            return new StateMachine(transitionMapping, stateTransitionDefinition);
        }

        private void checkParameters() {
            if (stateVector == null) {
                throw new IllegalArgumentException("the state vector must not be null");
            }
            if (stateTransferMatrix == null) {
                throw new IllegalArgumentException("the state transfer matrix must not be null");
            }
            if (stateTransitions == null) {
                throw new IllegalArgumentException("the state transitions must not be null");
            }
            if (stateVector.size() != stateTransferMatrix.length
                    || stateVector.size() != stateTransferMatrix[0].length) {
                throw new IllegalArgumentException("the number of states must match the width and height of the state transfer matrix");
            }
            int stateTransitionCount = countStateTransition(stateTransferMatrix);
            if (stateTransitionCount != stateTransitions.size()) {
                throw new IllegalArgumentException("the number of state transition in matrix must equals the size of the state transition instances");
            }
        }

        private int countStateTransition(byte[][] stateTransferMatrix) {
            int stateTransitionCount = 0;
            for (int i = 0; i < stateTransferMatrix.length; i++) {
                for (int j = 0; j < stateTransferMatrix[i].length; j++) {
                    if (stateTransferMatrix[i][j] == 1) {
                        stateTransitionCount++;
                    }
                }
            }
            return stateTransitionCount;
        }

    }

}
