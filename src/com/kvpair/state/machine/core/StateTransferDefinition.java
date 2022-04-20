package com.kvpair.state.machine.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class StateTransferDefinition {
    private Map<State, Integer> stateIndexes;
    private byte[][] matrix;
    private static final byte YES = 1;

    public StateTransferDefinition(Vector<State> states, byte[][] matrix) {
        stateIndexes = new HashMap<>();
        for (int i = 0; i < states.size(); i++) {
            stateIndexes.put(states.get(i), i);
        }
        this.matrix = matrix;
    }

    public boolean canTransfer(State preState, State nextState) {
        int preIndex = stateIndexes.get(preState);
        int nextIndex = stateIndexes.get(nextState);
        return matrix[preIndex][nextIndex] == YES;
    }

}
