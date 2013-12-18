package org.datafx.controller.util;

import javafx.event.Event;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: hendrikebbers
 * Date: 18.12.13
 * Time: 00:19
 * To change this template use File | Settings | File Templates.
 */
public interface VetoableEventHandler<T extends Event> extends EventListener {

    void handle(T event) throws Veto;
}
