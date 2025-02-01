package com.mycompany.databasehandlerlab;

/**
 *
 * @author Niko
 */
public class Student {
    private String studentID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String birthDate;
    private int startYear;
    private String department;
    private int units;
    private String address;
    
    public Student(String student_id, String firstName, String middleName, String lastName,
                   String sex, String birthDate, int startYear, String department, int units, String address) {
        this.studentID = student_id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
        this.startYear = startYear;
        this.department = department;
        this.units = units;
        this.address = address;
    }
    
    public String getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getStartYear() {
        return startYear;
    }

    public String getDepartment() {
        return department;
    }

    public int getUnits() {
        return units;
    }

    public String getAddress() {
        return address;
    }
    
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        String studentInfo = "Student Number: " + this.studentID +
                            "\nFirst Name: " + this.firstName +
                            "\nMiddle Name: " + this.middleName +
                            "\nLast Name: " + this.lastName +
                            "\nSex: " + this.sex +
                            "\nBirth Date: " + this.birthDate +
                            "\nStart Year: " + this.startYear +
                            "\nDepartment: " + this.department +
                            "\nUnits: " + this.units;
        
        if (this.address != null && !this.address.isEmpty()) {
            studentInfo += "\nAddress: " + this.address;
        }
        
        return studentInfo;
    }
}
