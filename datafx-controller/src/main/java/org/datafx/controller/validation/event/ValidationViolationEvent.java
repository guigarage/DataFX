package org.datafx.controller.validation.event;

import javafx.event.Event;
import javafx.event.EventType;
import org.datafx.controller.flow.event.AfterFlowActionEvent;

import javax.validation.ConstraintViolation;

public class ValidationViolationEvent extends Event {

    public static final EventType<AfterFlowActionEvent> DEFAULT_EVENT_TYPE =
            new EventType<>("ValidationViolationEvent");

    private ConstraintViolation violation;

    public ValidationViolationEvent(ConstraintViolation violation) {
        super(DEFAULT_EVENT_TYPE);
        this.violation = violation;
    }

    public ConstraintViolation getViolation() {
        return violation;
    }
}