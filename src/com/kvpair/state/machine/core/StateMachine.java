package com.kvpair.state.machine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class StateMachine {

    private Map<String, Function> handlerMapping = null;
    private StateTransferDefinition stateTransferDefinition = null;

    public StateMachine(Map<String, Function> handleMappings, StateTransferDefinition stateTransferDefinition) {
        this.handlerMapping = handleMappings;
        this.stateTransferDefinition = stateTransferDefinition;
    }

    public <T, R> R start(T inputParameter, State preState, State nextState) {
        if (preState == null || preState.getValue() == null) {
            throw new IllegalArgumentException("Neither the pre state and the value of the pre state should be null");
        }
        if (nextState == null || nextState.getValue() == null) {
            throw new IllegalArgumentException("Neither the next state and the value of the next state should be null");
        }
        Function<T, R> function = handlerMapping.get(buildStateTransferRelationshipKey(preState, nextState));
        if (function == null) {
            throw new IllegalArgumentException(String.format("can't find the matched handler with the '%s' and '%s'", preState, nextState));
        }
        boolean canTransfer = stateTransferDefinition.canTransfer(preState, nextState);
        if (!canTransfer) {
            throw new IllegalArgumentException(String.format("can't transfer from '%s' to '%s'", preState, nextState));
        }
        return function.apply(inputParameter);
    }

    public static String buildStateTransferRelationshipKey(State preState, State nextState) {
        return preState + "->" + nextState;
    }

    public static class Builder {
        private Vector<State> stateVector;
        private int[][] stateTransferMatrix;
        private List<StateTransferHandler> stateTransferHandlers;

        public Builder() {
        }

        public Builder stateVector(Vector<State> stateVector) {
            this.stateVector = stateVector;
            return this;
        }

        public Builder stateTransferMatrix(int[][] stateTransferMatrix) {
            this.stateTransferMatrix = stateTransferMatrix;
            return this;
        }

        public Builder stateTransferHandlers(List<StateTransferHandler> stateTransferHandlers) {
            this.stateTransferHandlers = stateTransferHandlers;
            return this;
        }

        public StateMachine build() {
            checkParameters();
            StateTransferDefinition stateTransferDefinition = new StateTransferDefinition(stateVector, stateTransferMatrix);
            Map<String, Function> handleMappings = new HashMap<>();
            for (StateTransferHandler stateTransferHandler : stateTransferHandlers) {
                handleMappings.put(stateTransferHandler.getKey(), stateTransferHandler.getHandler());
            }
            return new StateMachine(handleMappings, stateTransferDefinition);
        }

        private void checkParameters() {
            if (stateVector == null) {
                throw new IllegalArgumentException("the state vector must not be null");
            }
            if (stateTransferMatrix == null) {
                throw new IllegalArgumentException("the state transfer matrix must not be null");
            }
            if (stateTransferHandlers == null) {
                throw new IllegalArgumentException("the state transfer handlers must not be null");
            }
            if (stateVector.size() != stateTransferMatrix.length
                    || stateVector.size() != stateTransferMatrix[0].length) {
                throw new IllegalArgumentException("the number of states must match the width and height of the state transfer matrix");
            }
            int stateTransferRelationshipCount = countStateTransferRelationship(stateTransferMatrix);
            if (stateTransferRelationshipCount != stateTransferHandlers.size()) {
                throw new IllegalArgumentException("the number of state transfer relationship must equals the size of the state transfer handlers");
            }
        }

        private int countStateTransferRelationship(int[][] stateTransferMatrix) {
            int stateTransferRelationshipCount = 0;
            for (int i = 0; i < stateTransferMatrix.length; i++) {
                for (int j = 0; j < stateTransferMatrix[i].length; j++) {
                    if (stateTransferMatrix[i][j] == 1) {
                        stateTransferRelationshipCount++;
                    }
                }
            }
            return stateTransferRelationshipCount;
        }

    }

}
