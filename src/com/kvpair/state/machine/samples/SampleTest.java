package com.kvpair.state.machine.samples;

import com.kvpair.state.machine.core.State;
import com.kvpair.state.machine.core.StateMachine;
import com.kvpair.state.machine.core.StateTransferHandler;
import com.kvpair.state.machine.samples.handler.*;

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
         * Initializes the state vector
         */
        ApplyState[] arr = {ApplyState.UN_COMMIT, ApplyState.TO_AUDIT, ApplyState.FALLBACK, ApplyState.REJECTED, ApplyState.PASSED};
        Vector<State> stateVector = new Vector<>(Arrays.asList(arr));

        /**
         * Initializes the state transfer matrix
         */
        int[][] matrix = new int[arr.length][arr.length];
        matrix[0][1] = 1;
        matrix[1][2] = 1;
        matrix[1][3] = 1;
        matrix[1][4] = 1;
        matrix[2][1] = 1;

        /**
         * Initializes the state transfer handlers
         */
        List<StateTransferHandler> stateTransferHandlers = new ArrayList<>();
        stateTransferHandlers.add(new SubmitAuditHandler(ApplyState.UN_COMMIT, ApplyState.TO_AUDIT));
        stateTransferHandlers.add(new ApplyFallbackHandler(ApplyState.TO_AUDIT, ApplyState.FALLBACK));
        stateTransferHandlers.add(new RejectApplyHandler(ApplyState.TO_AUDIT, ApplyState.REJECTED));
        stateTransferHandlers.add(new ApplyPassedHandler(ApplyState.TO_AUDIT, ApplyState.PASSED));
        stateTransferHandlers.add(new ReSubmitAuditHandler(ApplyState.FALLBACK, ApplyState.TO_AUDIT));

        /**
         * Initializes the state transfer handlers
         */
        StateMachine stateMachine = new StateMachine.Builder()
                .stateTransferMatrix(matrix)
                .stateVector(stateVector)
                .stateTransferHandlers(stateTransferHandlers)
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
