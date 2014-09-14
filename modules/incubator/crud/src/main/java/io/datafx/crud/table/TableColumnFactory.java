package io.datafx.crud.table;

import io.datafx.core.DataFXUtils;
import io.datafx.core.ExceptionHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableColumnFactory {

    public static <T> List<TableColumn<T, ?>> createColumns(Class<T> entityType) {
        return createColumns(entityType, ExceptionHandler.getDefaultInstance());
    }

    public static <T> List<TableColumn<T, ?>> createColumns(Class<T> entityType, ExceptionHandler exceptionHandler) {
        List<TableColumn<T, ?>> columns = new ArrayList<>();
        for (Field field : entityType.getDeclaredFields()) {

            ViewColumn columnAnnotation = field.getAnnotation(ViewColumn.class);

            if (columnAnnotation != null) {
                TableColumn<T, ?> column = new TableColumn<>();
                column.setText(columnAnnotation.value());
                column.setEditable(columnAnnotation.editable());
                column.setSortable(columnAnnotation.sortable());
                column.setResizable(columnAnnotation.resizeable());
                column.setCellValueFactory(e -> {
                    try {
                        return new SimpleObjectProperty(DataFXUtils.getPrivileged(field, e.getValue()));
                    } catch (Exception exception) {
                        exceptionHandler.setException(exception);
                        return null;
                    }
                });
                columns.add(column);
            }
        }
        return columns;
    }
}
