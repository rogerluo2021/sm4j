package com.kvpair.state.machine.samples;

import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.core.StateMachine;
import com.kvpair.state.machine.core.StateTransition;
import com.kvpair.state.machine.samples.transition.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author Houfeng Luo
 * @since 1.0.0
 */
public class SampleTest {
    /*
                UN_COMMIT	TO_AUDIT	FALLBACK	REJECTED	PASSED
    UN_COMMIT		           1
    TO_AUDIT			                  1	           1	      1
    FALLBACK		           1
    REJECTED
    PASSED
     */
    public static void main(String[] args) {
        /**
         * Initializes a state vector
         */
        ApplyState[] states = {ApplyState.UN_COMMIT, ApplyState.TO_AUDIT, ApplyState.FALLBACK, ApplyState.REJECTED, ApplyState.PASSED};
        Vector<State> stateVector = new Vector<>(Arrays.asList(states));

        /**
         * Initializes a state transfer matrix
         */
        byte[][] matrix = new byte[states.length][states.length];
        matrix[0][1] = 1;
        matrix[1][2] = 1;
        matrix[1][3] = 1;
        matrix[1][4] = 1;
        matrix[2][1] = 1;

        /**
         * Initializes the state transition instances
         */
        List<StateTransition> stateTransitions = new ArrayList<>();
        stateTransitions.add(new SubmitAudit(ApplyState.UN_COMMIT, ApplyState.TO_AUDIT));
        stateTransitions.add(new ApplyFallback(ApplyState.TO_AUDIT, ApplyState.FALLBACK));
        stateTransitions.add(new RejectApply(ApplyState.TO_AUDIT, ApplyState.REJECTED));
        stateTransitions.add(new ApplyPassed(ApplyState.TO_AUDIT, ApplyState.PASSED));
        stateTransitions.add(new ReSubmitAudit(ApplyState.FALLBACK, ApplyState.TO_AUDIT));

        /**
         * Initializes a state machine
         */
        StateMachine stateMachine = new StateMachine.Builder()
                .stateTransferMatrix(matrix)
                .stateVector(stateVector)
                .stateTransitions(stateTransitions)
                .build();

        /**
         * the cases of the state transition success
         */
        stateMachine.start(new Apply(), ApplyState.UN_COMMIT, ApplyState.TO_AUDIT);
        stateMachine.start(new Apply(), ApplyState.TO_AUDIT, ApplyState.FALLBACK);
        stateMachine.start(new Apply(), ApplyState.TO_AUDIT, ApplyState.REJECTED);
        stateMachine.start(new OtherTypeInput(), ApplyState.TO_AUDIT, ApplyState.PASSED);
        stateMachine.start(new Apply(), ApplyState.FALLBACK, ApplyState.TO_AUDIT);

        /**
         * the case of the state transition failed
         */
        stateMachine.start(null, ApplyState.PASSED, ApplyState.TO_AUDIT);
    }
}
