package org.datafx.controller.util;

public class VetoException extends Exception {

	private static final long serialVersionUID = 1L;

	private Veto veto;

    public VetoException(Veto veto) {
        this.veto = veto;
    }

    public Veto getVeto() {
        return veto;
    }
}
