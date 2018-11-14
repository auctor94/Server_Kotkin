package Personnel;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Personnel {

    private SimpleIntegerProperty tabNumber;
    private SimpleStringProperty surname;
    private SimpleStringProperty name;
    private SimpleStringProperty lastName;
    private LocalDate birthday;
    private SimpleStringProperty education;
    private LocalDate hireDate;
    private SimpleIntegerProperty idPosition;

    public Personnel(SimpleIntegerProperty tabNumber, SimpleStringProperty surname, SimpleStringProperty name, SimpleStringProperty lastName, LocalDate birthday, SimpleStringProperty education, LocalDate hireDate, SimpleIntegerProperty idPosition) {
        this.tabNumber = tabNumber;
        this.surname = surname;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.education = education;
        this.hireDate = hireDate;
        this.idPosition = idPosition;
    }

    public Personnel(int tabNumber, String surname, String name, String lastName, LocalDate birthday, String education, LocalDate hireDate, int idPosition) {
        this.tabNumber = new SimpleIntegerProperty(tabNumber);
        this.surname = new SimpleStringProperty(surname);
        this.name = new SimpleStringProperty(name);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthday = birthday;
        this.education = new SimpleStringProperty(education);
        this.hireDate = hireDate;
        this.idPosition = new SimpleIntegerProperty(idPosition);
    }

    public int getTabNumber() {
        return tabNumber.get();
    }

    public SimpleIntegerProperty tabNumberProperty() {
        return tabNumber;
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEducation() {
        return education.get();
    }

    public SimpleStringProperty educationProperty() {
        return education;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public int getIdPosition() {
        return idPosition.get();
    }

    public SimpleIntegerProperty idPositionProperty() {
        return idPosition;
    }

    public void setTabNumber(int tabNumber) {
        this.tabNumber.set(tabNumber);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setEducation(String education) {
        this.education.set(education);
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition.set(idPosition);
    }
}
