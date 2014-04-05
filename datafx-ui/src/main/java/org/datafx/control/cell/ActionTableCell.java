package org.datafx.control.cell;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;

import java.util.function.Consumer;

public class ActionTableCell<S, T, U extends ButtonBase> extends TableCell<S,T> {

    private Consumer<S> consumer;

    private U button;

    public ActionTableCell(Consumer<S> consumer, U button) {
        this.consumer = consumer;
        this.button = button;
        button.setOnAction((e) -> consumer.accept((S) getTableRow().getItem()));
        setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        if(empty) {
           setGraphic(null);
        } else {
            setGraphic(button);
        }
    }

    public static <S, T> ActionTableCell<S, T, Button> createWithButton(Consumer<S> consumer, String text) {
        return new ActionTableCell<S, T, Button>(consumer, new Button(text));
    }

    public static <S, T> ActionTableCell<S, T, Hyperlink> createWithHyperlink(Consumer<S> consumer, String text) {
        return new ActionTableCell<S, T, Hyperlink>(consumer, new Hyperlink(text));
    }
}
