package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private OrderState currentState;
    private final List<OrderState> history;

    public Order(OrderState initialState) {
        this.currentState = initialState;
        this.history = new ArrayList<>();
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (currentState.isFinalState()) {
            throw new OrderIsAlreadyFinalException();
        }

        history.add(currentState);
        currentState = currentState.getNextState();
        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (currentState.isFinalState()) {
            throw new CannotCancelFinalOrderException();
        }

        history.add(currentState);
        currentState = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }

        currentState = history.remove(history.size() - 1);
        System.out.println("Order state reverted to: " + currentState);
    }
}
