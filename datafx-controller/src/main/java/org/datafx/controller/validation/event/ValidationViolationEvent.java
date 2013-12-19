package org.datafx.controller.validation.event;

import javafx.event.Event;
import javafx.event.EventType;
import org.datafx.controller.flow.event.AfterFlowActionEvent;

import javax.validation.ConstraintViolation;

public class ValidationViolationEvent<T> extends Event {

	private static final long serialVersionUID = 1L;

	public static final EventType<AfterFlowActionEvent> DEFAULT_EVENT_TYPE =
            new EventType<>("ValidationViolationEvent");

    private ConstraintViolation<T> violation;

    public ValidationViolationEvent(ConstraintViolation<T> violation) {
        super(DEFAULT_EVENT_TYPE);
        this.violation = violation;
    }

    public ConstraintViolation<T> getViolation() {
        return violation;
    }
}