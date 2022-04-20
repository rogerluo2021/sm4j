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
        byte[][] stateTransferMatrix = new byte[states.length][states.length];
        stateTransferMatrix[0][1] = 1;
        stateTransferMatrix[1][2] = 1;
        stateTransferMatrix[1][3] = 1;
        stateTransferMatrix[1][4] = 1;
        stateTransferMatrix[2][1] = 1;

        /**
         * Initializes the state transition instances
         */
        List<StateTransition> stateTransitions = new ArrayList<>();
        stateTransitions.add(new SubmitAudit());
        stateTransitions.add(new ApplyFallback());
        stateTransitions.add(new RejectApply());
        stateTransitions.add(new ApplyPassed());
        stateTransitions.add(new ReSubmitAudit());

        /**
         * Initializes a state machine
         */
        StateMachine stateMachine = new StateMachine.Builder()
                .stateTransferMatrix(stateTransferMatrix)
                .stateVector(stateVector)
                .stateTransitions(stateTransitions)
                .build();

        /**
         * the cases of the state transition success
         */
        Long result = stateMachine.doTransfer(new Apply(), Long.class, ApplyState.UN_COMMIT, ApplyState.TO_AUDIT);
        System.out.println("result:"+result);
        stateMachine.doTransfer(new Apply(), Long.class, ApplyState.TO_AUDIT, ApplyState.FALLBACK);
        stateMachine.doTransfer(new Apply(), Long.class, ApplyState.TO_AUDIT, ApplyState.REJECTED);
        stateMachine.doTransfer(new OtherTypeInput(), Void.class, ApplyState.TO_AUDIT, ApplyState.PASSED);
        stateMachine.doTransfer(new Apply(), Long.class, ApplyState.FALLBACK, ApplyState.TO_AUDIT);

        /**
         * the case of the state transition failed
         */
        stateMachine.doTransfer(null, Long.class, ApplyState.PASSED, ApplyState.TO_AUDIT);
    }
}
