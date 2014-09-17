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
package io.datafx.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.datafx.io.DataReader;
import io.datafx.io.WritableDataReader;
import io.datafx.io.WriteBackHandler;
import io.datafx.io.converter.JdbcConverter;
import io.datafx.provider.ListDataProvider;
import io.datafx.io.JdbcSource;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author johan
 */
public class JdbcSample {

    private static Connection conn;
    private static final String dbURL = "jdbc:derby:memory:myDB;create=true";

    public JdbcSample() {
    }

    public Node getContent(Scene scene) {
        createDatabase();
        // TabPane
        final TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefWidth(scene.getWidth());
        tabPane.setPrefHeight(scene.getHeight());

        tabPane.prefWidthProperty().bind(scene.widthProperty());
        tabPane.prefHeightProperty().bind(scene.heightProperty());

        Tab localTab = new Tab("local");
        buildLocalTab(localTab);
        tabPane.getTabs().add(localTab);


        return tabPane;
    }

    private void buildLocalTab(Tab tab) {
        try {
            JdbcConverter<Person> converter = new JdbcConverter<Person>() {
                    public Person convertOneRow (ResultSet resultSet){
                    try {
                        Person answer = new Person();
                        answer.setFirstName(resultSet.getString("firstName"));
                        answer.setLastName(resultSet.getString("lastName"));
                        answer.setCountry(resultSet.getString("country"));
                        return answer;
                    } catch (SQLException ex) {
                        Logger.getLogger(JdbcSample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }
                
            };
            DataReader<Person> dr = new JdbcSource(dbURL, converter, "PERSON", "firstName", "lastName", "country");
            ListDataProvider<Person> lodp = new ListDataProvider(dr);
            ObservableList<Person> myList = FXCollections.observableArrayList();
            lodp.setResultObservableList(myList);
            lodp.setWriteBackHandler(new WriteBackHandler<Person>() {
                @Override
                public WritableDataReader createDataSource(Person me) {
                    String statement = "UPDATE PERSON SET lastName=\'" + me.getLastName() + "\' WHERE firstName=\'" + me.getFirstName() + "\'";
                    JdbcSource<Person> dr = new JdbcSource(dbURL, statement, null);
                    dr.setUpdateQuery(true);
                    System.out.println("Writeback called with statement "+statement);
                    return dr;
                }
            });
            lodp.retrieve();

            final ListProperty<Person> op = lodp.getData();
            TableView<Person> tv = new TableView(myList);
            tv.setEditable(true);
            TableColumn<Person, String> firstNameCol = new TableColumn<Person, String>("First ViewColumn");
            firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
            TableColumn<Person, String> lastNameCol = new TableColumn<Person, String>("Last ViewColumn");
            lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
            lastNameCol.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>(){

                @Override
                public TableCell<Person, String> call(TableColumn<Person, String> p) {
                    return new TextFieldTableCell(new StringConverter<String>() {

                        @Override
                        public String toString(String t) {
                            return t;
                        }

                        @Override
                        public String fromString(String string) {
                            return string;
                        }
                    });
                }
            });
            lastNameCol.setEditable(true);
            lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Person,String>>() {

                @Override
                public void handle(CellEditEvent<Person, String> t) {
                    Person person = t.getRowValue();
                    String nv = t.getNewValue();
                    person.setLastName(nv); // this will trigger the writeback handler
                }
            });
            TableColumn<Person, String> countryCol = new TableColumn<Person, String>("Country");
            countryCol.setCellValueFactory(new PropertyValueFactory("country"));
            tv.getColumns().setAll(firstNameCol, lastNameCol, countryCol);
            tab.setContent(tv);
        } catch (Exception ex) {
            Logger.getLogger(JdbcSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static void createDatabase() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL);
            conn.createStatement().execute("create table PERSON (FIRSTNAME varchar(255), LASTNAME varchar(255), COUNTRY varchar(255))");
            conn.createStatement().execute("INSERT INTO PERSON values ('Jonathan', 'Giles', 'New Zealand')");
            conn.createStatement().execute("INSERT INTO PERSON values ('Johan', 'Vos', 'Belgium')");
            conn.createStatement().execute("INSERT INTO PERSON values ('Hendrik', 'Ebbers', 'Germany')");

        } catch (Exception except) {
            except.printStackTrace();
        }

    }

    public class Person {

        private StringProperty firstName = new SimpleStringProperty();
        private StringProperty lastName = new SimpleStringProperty();

        public void setFirstName(String value) {
            firstNameProperty().set(value);
        }

        public String getFirstName() {
            return firstNameProperty().get();
        }

        public StringProperty firstNameProperty() {
            if (firstName == null) {
                firstName = new SimpleStringProperty(this, "firstName");
            }
            return firstName;
        }
        private String country;

        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName.get();
        }

        /**
         * @param lastName the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        /**
         * @return the country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @param country the country to set
         */
        public void setCountry(String country) {
            this.country = country;
        }
    }
    
}
