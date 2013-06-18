/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.control;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * A convenience class intended to make creating a TableView easier. In
 * particular, this class can create a TableView, as well as default TableColumn
 * instances which are preconfigured with {@link TableColumn#cellValueFactoryProperty() cell
 * value factories} that are able to extract the relevant data from a given
 * Class type.
 *
 * <p>In addition to this, the TableViewFactory is able to refine the
 * automatically generated TableColumns by reducing the columns added to the
 * TableView, as well as renaming and reordering.
 *
 * <p>An example of using this API to create a TableView is shown below:
 *
 * <pre>
 * <code>
 * TableView<Person> tableView = TableViewFactory.
 *     create(Person.class, personsList).
 *     selectColumns("First Name", "Last Name", "Telecommuter", "Employee Type", "Balance", "Earnings").
 *     renameColumn("Employee Type", "Type").
 *     renameColumn("Telecommuter", "Remote").
 *     buildTableView();
 * </code>
 * </pre>
 *
 * @author Jonathan Giles
 */
public class TableViewFactory<S> {
    private TableViewFactory() {
    }

    public static <S> TableView<S> create(Class<? extends S> dataType) {
        List<TableColumn<S, ?>> columns = createColumns(dataType);

        TableView<S> table = new TableView<S>();
        table.getColumns().setAll(columns);

        return table;
    }

    public static <S> TableViewFactory<S> create(List<? extends S> items) {
        return create(FXCollections.observableArrayList(items));
    }

    public static <S> TableViewFactory<S> create(Class<? extends S> dataType, List<S> items) {
        return create(dataType, FXCollections.observableArrayList(items));
    }

    public static <S> TableViewFactory<S> create(ObservableList<? extends S> items) {
        return create(null, FXCollections.observableArrayList(items));
    }

    public static <S> TableViewFactory<S> create(Class<? extends S> dataType, final ObservableList<S> items) {
        if (items == null) {
            throw new NullPointerException("items can not be null");
        }

        final TableView<S> table = new TableView<S>();
        table.setItems(items);
        table.setEditable(true);

        if (dataType == null && table.getItems().isEmpty()) {
            // we'll have to create the columns the first time the items list
            // changes, so let's hook in a listener
            InvalidationListener listener = new InvalidationListener() {
                @Override
                public void invalidated(Observable o) {
                    if (!table.getItems().isEmpty()) {
                        createColumns(table);

                        // remove listener
                        items.removeListener(this);
                    }
                }
            };
            table.getItems().addListener(listener);
        } else {
            createColumns(table);
        }

        return TableViewFactory.configure(table);
    }

    private static <S> void createColumns(TableView<S> table) {
        @SuppressWarnings("unchecked")
        Class<? extends S> actualDataType = (Class<? extends S>)table.getItems().get(0).getClass();
        
        if (actualDataType != null) {
            List<TableColumn<S, ?>> columns = createColumns(actualDataType);
            table.getColumns().setAll(columns);
        }
    }

    private static <S> List<TableColumn<S, ?>> createColumns(Class<? extends S> dataType) {
        List<TableColumn<S, ?>> columns = new ArrayList<TableColumn<S, ?>>();
        try {
            // TODO inspect the class, create columns for all public properties
            BeanInfo beanInfo = Introspector.getBeanInfo(dataType);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (int i = 0; i < properties.length; i++) {
                PropertyDescriptor pd = properties[i];
                if ("class".equals(pd.getName())) {
                    continue;
                }
                if (pd.getReadMethod() == null) {
                    continue;
                }

                Class<?> propertyDataType = pd.getPropertyType();
//                System.out.println("Name: " + pd.getName() + ", class: " + propertyDataType);

                String displayName = makePrettyDisplayName(pd.getDisplayName());

                TableColumn column = new TableColumn();
                column.setText(displayName);
                column.setCellValueFactory(new PropertyValueFactory(pd.getName()));
                columns.add(column);

                // TODO set property name in the TableColumn properties map

                // install custom cell factory
                if (propertyDataType.isEnum()) {
                    Object[] enumConstants = propertyDataType.getEnumConstants();
                    column.setCellFactory(ChoiceBoxTableCell.forTableColumn(enumConstants));
                } else if (propertyDataType == boolean.class) {
                    column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
                } else if (propertyDataType == String.class) {
                    column.setCellFactory(TextFieldTableCell.forTableColumn());
                }
            }
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        }

        return columns;
    }

    private static String makePrettyDisplayName(String name) {
        // split at each capital letter
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
                && Character.isUpperCase(name.charAt(0))) {
            return name;
        }

        char chars[] = name.toCharArray();

        List<Character> charList = new ArrayList<Character>(chars.length);
        for (int i = 0; i < chars.length; i++) {
            char c0 = chars[i];
            char c1 = i > 0 ? chars[i - 1] : ' ';

            if (i == 0) {
                charList.add(Character.toUpperCase(c0));
                continue;
            }

            if (Character.isUpperCase(c0) && !Character.isUpperCase(c1)) {
                // insert space
                charList.add(' ');
            }
            charList.add(c0);
        }

        chars = new char[charList.size()];
        for (int i = 0; i < charList.size(); i++) {
            chars[i] = charList.get(i);
        }
        return new String(chars);
    }
    private TableView<S> table;
    private ObservableList<TableColumn<S, ?>> columns;
    private boolean columnSelectPerformed = false;
    private ObservableList<TableColumn<S, ?>> finalColumns;

    public static <S> TableViewFactory<S> configure(TableView<S> table) {
        return new TableViewFactory<S>(table);
    }

    public static <S> void print(TableView<S> table) {
        print(table.getColumns());
    }

    public static <S> void print(ObservableList<TableColumn<S, ?>> columns) {
        System.out.println("Columns:");
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<S,?> tc = columns.get(i);
            System.out.println("    Text: " + tc.getText());
        }
    }

    private TableViewFactory(TableView<S> table/*, ObservableList<TableColumn> columns*/) {
        this.table = table;
        this.columns = table.getColumns(); //columns == null ? table.getColumns() : columns;
        this.finalColumns = FXCollections.observableArrayList();

        if (this.columns == null) {
            throw new NullPointerException("Columns can not be null");
        }
    }

    public TableViewFactory<S> selectColumns(String... names) {
        if (names == null || names.length == 0) {
            return this;
        }
        
        TableColumn<S,?> tc;
        for (int i = 0; i < names.length; i++) {
            String name = names[i];

            for (int j = 0; j < columns.size(); j++) {
                tc = columns.get(j);

                if (name == null) continue;
                if (name.equals(tc.getText())) {
                    finalColumns.add(tc);
                    columnSelectPerformed = true;
                }
            }
        }
        return this;
    } 

    public TableViewFactory<S> renameColumn(String oldName, String newName) {
        if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            return this;
        }
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<S,?> tc = columns.get(i);
            if (oldName.equals(tc.getText())) {
                tc.setText(newName);
                break;
            }
        }
        return this;
    }

    public ObservableList<TableColumn<S, ?>> buildColumns() {
        return finalColumns;
    }

    public TableView<S> buildTableView() {
        if (table == null) {
            table = new TableView<S>();
        }

        if (columnSelectPerformed) {
            table.getColumns().setAll(finalColumns);
        } else if (!table.getColumns().equals(columns)) {
            table.getColumns().setAll(columns);
        }

        return table;
    }
//    public ObservableList<TableColumn<S,?>> buildAndSetInTableView() {
//        if (table == null) {
//            throw new IllegalStateException("Can not set columns in TableView, as TableView instance is null");
//        }
//        table.getColumns().setAll(finalColumns);
//        return finalColumns;
//    }
}
