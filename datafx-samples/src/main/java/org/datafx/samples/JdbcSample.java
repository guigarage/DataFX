/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.datafx.provider.ListObjectDataProvider;
import org.datafx.reader.DataReader;
import org.datafx.reader.JdbcSource;
import org.datafx.reader.converter.JdbcConverter;

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
            // We can create a DefaultJdbcConverter that does what we do below
            JdbcConverter<Person> converter = new JdbcConverter<Person>() {
                private ResultSet resultSet;
                private boolean last = false;
                
                @Override public void initialize(ResultSet input) {
                    this.resultSet = input;
                }
                
                @Override public Person get() {
                    try {
                        Person answer = new Person();
                        answer.setFirstName(resultSet.getString("firstName"));
                        answer.setLastName(resultSet.getString("lastName"));
                        answer.setCountry(resultSet.getString("country"));
                        resultSet.next();
                        return answer;
                    } catch (SQLException ex) {
                        Logger.getLogger(JdbcSample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }
                
                @Override public boolean next() {
                    try {
                        return !resultSet.isAfterLast();
//                    
                    } catch (SQLException ex) {
                        Logger.getLogger(JdbcSample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;
                }
            };
            DataReader<Book> dr = new JdbcSource(dbURL, converter, "PERSON", "firstName", "lastName", "country");
            ListObjectDataProvider<Book> lodp = new ListObjectDataProvider(dr);
            ObservableList<Book> myList = FXCollections.observableArrayList();
            lodp.setResultObservableList(myList);
            lodp.retrieve();

            final ListProperty<Book> op = lodp.getData();
            System.out.println("data from lodp = "+lodp.getData());
            //TableView tv = new TableView(op);
            TableView tv = new TableView(myList);
            TableColumn<Person, String> firstNameCol = new TableColumn<Person, String>("First Name");
            firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
            TableColumn<Person, String> lastNameCol = new TableColumn<Person, String>("Last Name");
            lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
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

        private StringProperty firstName;

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
        private String lastName;
        private String country;

        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
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
