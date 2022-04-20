package com.kvpair.state.machine.core;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kvpair.state.machine.core.StateTransferDefinition.YES;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class StateMachine {

    private Map<String, StateTransition> transitionMapping;
    private StateTransferDefinition stateTransferDefinition;

    private StateMachine(Map<String, StateTransition> transitionMapping, StateTransferDefinition stateTransferDefinition) {
        this.transitionMapping = transitionMapping;
        this.stateTransferDefinition = stateTransferDefinition;
    }

    public <T, R> R doTransfer(T context, Class<R> returnType, State preState, State nextState) {
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
        boolean canTransfer = stateTransferDefinition.canTransfer(preState, nextState);
        if (!canTransfer) {
            throw new IllegalArgumentException(String.format("can't transfer from '%s' to '%s'", preState, nextState));
        }

        R result;
        stateTransition.before(context);
        result = stateTransition.transfer(context);
        if(result != null && !returnType.equals(result.getClass())) {
            throw new IllegalArgumentException("return type not matched");
        }
        stateTransition.after(context, result);

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

            StateTransferDefinition stateTransferDefinition = new StateTransferDefinition(stateVector, stateTransferMatrix);
            Map<String, StateTransition> transitionMapping = stateTransitions.stream().collect(Collectors.toMap(StateTransition::getKey, Function.identity()));

            /**
             * Finally, we need check the position of the state transition instance in the matrix once again
             */
            for (StateTransition stateTransition : stateTransitions) {
                int preStateIndex = stateTransferDefinition.getIndex(stateTransition.getPreState());
                int nextStateIndex = stateTransferDefinition.getIndex(stateTransition.getNextState());
                if (stateTransferMatrix[preStateIndex][nextStateIndex] != YES) {
                    throw new IllegalArgumentException(String.format("can't find the state transition(%s -> %s) in the matrix", stateTransition.getPreState(), stateTransition.getNextState()));
                }
            }
            return new StateMachine(transitionMapping, stateTransferDefinition);
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
                    if (stateTransferMatrix[i][j] == YES) {
                        stateTransitionCount++;
                    }
                }
            }
            return stateTransitionCount;
        }

    }

}
