/*
LABORATORY EXAM 2
DALISAY, NIKOLAS JOSEF P. - 2CSD

================================
NOTES:
================================
- Please change the file path of the database inside the static void method to the file path of the database in your device.
- The program is a user-input program. 
- In my case, some methods will output a [SQLITE_BUSY] exception if DBeaver is open (and database is open) while running the program. If you get the same error,
    please try to close your DBeaver if it's open.
- Check constraints are added in initializeStudents for validation; java validation methods are not implemented.
THANK YOU!
================================
*/
package com.mycompany.databasehandlerlab;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Niko
 */
public class DatabaseHandler {
    private Connection conn;
    private static Scanner in = new Scanner(System.in);
    
    public DatabaseHandler(String username, String password, String database) {
        String connStr = "jdbc:sqlite:" + database;
        System.out.println(connStr);
        try {
            conn = DriverManager.getConnection(connStr);
        } 
        catch (SQLException e) {
            System.err.println("Failed to create connection");
            System.err.println(e.toString());
        }
    }
    
    //modified createQuery according to instructions in canvas
    void initializeStudents() {
        String dropQuery = "DROP TABLE IF EXISTS Students";
        String createQuery = "CREATE TABLE Students (\n" +
                                    //changed from student_number to student_id according to columns stated in canvas
                                    "student_id TEXT NOT NULL CHECK (student_id GLOB '[0-9][0-9][0-9][0-9]010[0-9][0-9][0-9][0-9]'),\n" +
                                    "student_fname TEXT NOT NULL,\n" +
                                    "student_mname TEXT NOT NULL,\n" + //changed to not null according to columns stated in canvas
                                    "student_lname TEXT NOT NULL,\n" +
                                    "student_sex TEXT NOT NULL CHECK (student_sex IN ('M', 'F')),\n" +
                                    "student_birth TEXT NOT NULL CHECK (student_birth GLOB '[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]'),\n" +
                                    "student_start INTEGER NOT NULL CHECK (student_start BETWEEN 0 AND 2025),\n" +
                                    "student_department TEXT NOT NULL,\n" +
                                    "student_units INTEGER NOT NULL,\n" +
                                    "student_address TEXT,\n" +
                                    "CONSTRAINT Students_PK PRIMARY KEY (student_id)\n" +
                                    ");";
        
        try {
            PreparedStatement dropStmt = conn.prepareStatement(dropQuery);
            dropStmt.executeUpdate();
            
            PreparedStatement createStmt = conn.prepareStatement(createQuery);
            createStmt.executeUpdate();
            System.out.println("Created Student table successfully!");
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
    }
    
    Student getStudent(String studentID) {
        String getStudentQuery = "SELECT * FROM Students s\n" +
                                    "WHERE s.student_id = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(getStudentQuery);
            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("student_fname"),
                    rs.getString("student_mname"),
                    rs.getString("student_lname"),
                    rs.getString("student_sex"),
                    rs.getString("student_birth"),
                    rs.getInt("student_start"),
                    rs.getString("student_department"),
                    rs.getInt("student_units"),
                    rs.getString("student_address")
                );
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return null;
    }
    
    Student getStudent(String studentFname, String studentMname, String studentLname) {
        String getStudentQuery = "SELECT * FROM Students s\n" +
                                    "WHERE s.student_fname = ?\n" +
                                    "AND s.student_mname = ?\n" +
                                    "AND s.student_lname = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(getStudentQuery);
            stmt.setString(1, studentFname);
            stmt.setString(2, studentMname);
            stmt.setString(3, studentLname);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("student_fname"),
                    rs.getString("student_mname"),
                    rs.getString("student_lname"),
                    rs.getString("student_sex"),
                    rs.getString("student_birth"),
                    rs.getInt("student_start"),
                    rs.getString("student_department"),
                    rs.getInt("student_units"),
                    rs.getString("student_address")
                );
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return null;
    }
    
    //Array<Student> does not work; either Student[] or ArrayList<Student> will i think
    ArrayList<Student> getStudents() {
        String getStudentsQuery = "SELECT * FROM Students";
        ArrayList<Student> students = new ArrayList<>();
        
        try {
            PreparedStatement stmt = conn.prepareStatement(getStudentsQuery);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("student_id"),
                    rs.getString("student_fname"),
                    rs.getString("student_mname"),
                    rs.getString("student_lname"),
                    rs.getString("student_sex"),
                    rs.getString("student_birth"),
                    rs.getInt("student_start"),
                    rs.getString("student_department"),
                    rs.getInt("student_units"),
                    rs.getString("student_address")
                ));
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return students;
    }
    
    //removed alias s of Students
    Boolean removeStudent(String studentID) {
        String removeStudentQuery = "DELETE FROM Students\n" +
                                    "WHERE student_id = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(removeStudentQuery);
            stmt.setString(1, studentID);
            int rows = stmt.executeUpdate();
            
            return rows > 0;
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return false;
    }
    
    Boolean getStudentsByYear(int year) {
        String getByYearQuery = "SELECT * FROM Students s\n" +
                                "WHERE s.student_start LIKE ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(getByYearQuery);
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            
            int rows = 0;
            while(rs.next()) {
                rows++;
            }
            
            return rows > 0;
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return false;
    }
    
    //removed alias s of Students
    Boolean updateStudentInfo(String studentID, Student studentInfo) {
        String updateInfoQuery = "UPDATE Students\n" +
                                    "SET student_fname = ?\n" +
                                    ", student_mname = ?\n" +
                                    ", student_lname = ?\n" +
                                    ", student_department = ?\n" +
                                    ", student_address = ?\n" +
                                    "WHERE student_id = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(updateInfoQuery);
            stmt.setString(1, studentInfo.getFirstName());
            stmt.setString(2, studentInfo.getMiddleName());
            stmt.setString(3, studentInfo.getLastName());
            stmt.setString(4, studentInfo.getDepartment());
            stmt.setString(5, studentInfo.getAddress());
            stmt.setString(6, studentID);
            int rows = stmt.executeUpdate();
            
            return rows > 0;
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return false;
    }
    
    //added a studentID parameter as there are 2 ? in the query given in canvas, removed alias s
    Boolean updateStudentUnits(String studentID, int subtractedUnits) {
        String updateUnitsQuery = "UPDATE Students\n" +
                                    "SET student_units = ?\n" +
                                    "WHERE student_id = ?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(updateUnitsQuery);
            stmt.setInt(1, subtractedUnits);
            stmt.setString(2, studentID);
            int rows = stmt.executeUpdate();
            
            return rows > 0;
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return false;
    }
    
    Boolean insertStudent(Student newStudent) {
        String insertQuery = "INSERT INTO Students (\n" +
                            "student_id\n" +
                            ", student_fname\n" +
                            ", student_mname\n" +
                            ", student_lname\n" +
                            ", student_sex\n" +
                            ", student_birth\n" +
                            ", student_start\n" +
                            ", student_department\n" +
                            ", student_units\n" +
                            ", student_address\n" +
                            ") values (\n" +
                            "?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ", ?\n" +
                            ")";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, newStudent.getStudentID());
            stmt.setString(2, newStudent.getFirstName());
            stmt.setString(3, newStudent.getMiddleName());
            stmt.setString(4, newStudent.getLastName());
            stmt.setString(5, newStudent.getSex());
            stmt.setString(6, newStudent.getBirthDate());
            stmt.setInt(7, newStudent.getStartYear());
            stmt.setString(8, newStudent.getDepartment());
            stmt.setInt(9, newStudent.getUnits());
            stmt.setString(10, newStudent.getAddress());
            int rows = stmt.executeUpdate();
            
            return rows > 0;
        }
        catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        String directPathToDb = "C:\\Users\\Niko\\Documents\\2CSD\\2ND SEM\\BACKEND\\DatabaseHandlerLab\\students";
        DatabaseHandler jdbc = new DatabaseHandler("root", "root", directPathToDb);
        
        System.out.println("Initialize Student table? (If the table exists, this will reset the records inside.)\nY - Yes\nN - No");
        char initializeOrNot = in.next().charAt(0);
        initializeOrNot = Character.toUpperCase(initializeOrNot);
        
        if(initializeOrNot == 'Y')
            jdbc.initializeStudents();
        else if(initializeOrNot != 'N')
            System.out.println("Since your input is not Y or N, the program will assume that you will not initialize the student table.");
        
        boolean flag = true;
        
        while(flag) {
            System.out.println("\nMETHODS:\n1 - getStudent by Student ID\n2 - getStudent by Name\n3 - getStudents\n4 - removeStudent\n5 - getStudentsByYear\n"
                    + "6 - updateStudentInfo\n7 - updateStudentUnits\n8 - insertStudent\n9 - Exit the Program");
            System.out.print("Choose from the given methods (1-9): ");
            char choice = in.next().charAt(0);
            in.nextLine();
            
            switch (choice) {
                case '1': 
                    System.out.print("Please enter the student's student ID (XXXX010YYYY): ");
                    String studentIDCaseOne = in.nextLine();
                    
                    Student studCaseOne = jdbc.getStudent(studentIDCaseOne);
                    
                    if (studCaseOne != null) {
                        System.out.println("Information regarding the student with student ID " + studentIDCaseOne + ":");
                        System.out.println(studCaseOne.toString());
                    } 
                    else {
                        System.out.println("No student found with the student ID " + studentIDCaseOne + ".");
                    }
                    break;
                case '2': 
                    System.out.println("Please enter the student's:");
                    System.out.print("First Name: ");
                    String firstNameCaseTwo = in.nextLine();
                    System.out.print("Middle Name: ");
                    String middleNameCaseTwo = in.nextLine();
                    System.out.print("Last Name: ");
                    String lastNameCaseTwo = in.nextLine();
                    
                    Student studCaseTwo = jdbc.getStudent(firstNameCaseTwo, middleNameCaseTwo, lastNameCaseTwo);
                    
                    if (studCaseTwo != null) {
                        System.out.println("Information regarding the student you entered:");
                        System.out.println(studCaseTwo.toString());
                    } 
                    else {
                        System.out.println("No student found with the information you entered.");
                    }
                    break;
                case '3': 
                    ArrayList<Student> al = jdbc.getStudents();
                    
                    if(al.isEmpty()) {
                        System.out.println("There are no records in the student table.");
                    }
                    else {
                        System.out.println("Here are all the students listed in the table:");
                        for(Student stud : al) {
                            System.out.println(stud.toString());
                            System.out.println("");
                        }
                    }
                    break;
                case '4': 
                    System.out.print("Please enter the student's student ID (XXXX010YYYY): ");
                    String studentIDCaseFour = in.nextLine();
                    
                    if(jdbc.removeStudent(studentIDCaseFour))
                        System.out.println("Student " + studentIDCaseFour + " is successfully deleted from the table.");
                    else
                        System.out.println("Student " + studentIDCaseFour + " does not exist in the table.");
                    break;
                case '5': 
                    System.out.print("Please enter the year you're trying to look for: ");
                    int year = in.nextInt();
                    in.nextLine();
                    
                    if(jdbc.getStudentsByYear(year))
                        System.out.println("There are student/s that started in the year " + year + ".");
                    else
                        System.out.println("There are no existing students that started in the year " + year + ".");
                    break;
                case '6': 
                    System.out.println("Please enter the student's:");
                    System.out.print("Student ID (XXXX010YYYY): ");
                    String studentIDCaseSix = in.nextLine();
                    System.out.print("First Name: ");
                    String firstNameCaseSix = in.nextLine();
                    System.out.print("Middle Name: ");
                    String middleNameCaseSix = in.nextLine();
                    System.out.print("Last Name: ");
                    String lastNameCaseSix = in.nextLine();
                    System.out.print("Deparment: ");
                    String departmentCaseSix = in.nextLine();
                    System.out.print("Address: ");
                    String addressCaseSix = in.nextLine();
                    
                    Student studCaseSix = new Student(studentIDCaseSix, firstNameCaseSix, middleNameCaseSix, lastNameCaseSix, "", "",
                                                        2025, departmentCaseSix, 0, addressCaseSix); //default values - year:2025, units:0
                    
                    if(jdbc.updateStudentInfo(studentIDCaseSix, studCaseSix))
                        System.out.println("Student " + studentIDCaseSix + "'s information has been updated.");
                    else
                        System.out.println("Student " + studentIDCaseSix + " is not found in the table.");
                    break;
                case '7': 
                    System.out.println("Please enter the student's:");
                    System.out.print("Student ID (XXXX010YYYY): ");
                    String studentIDCaseSeven = in.nextLine();
                    System.out.print("Subtracted Units: ");
                    int unitsCaseSeven = in.nextInt();
                    in.nextLine();
                    
                    if(jdbc.updateStudentUnits(studentIDCaseSeven, unitsCaseSeven))
                        System.out.println("Student " + studentIDCaseSeven +"'s units has been updated.");
                    else
                        System.out.println("Student " + studentIDCaseSeven + " is not found in the table.");
                    break;
                case '8': 
                    System.out.println("Please enter the student's:");
                    System.out.print("Student ID (XXXX010YYYY): ");
                    String studentID = in.nextLine();
                    System.out.print("First Name: ");
                    String firstName = in.nextLine();
                    System.out.print("Middle Name: ");
                    String middleName = in.nextLine();
                    System.out.print("Last Name: ");
                    String lastName = in.nextLine();
                    System.out.print("Sex (M/F): ");
                    String sex = in.nextLine();
                    System.out.print("Birth Date (YYYY-MM-DD): ");
                    String birthDate = in.nextLine();
                    System.out.print("Start Year (Valid Year): ");
                    int startYear = in.nextInt();
                    in.nextLine();
                    System.out.print("Deparment (e.g. CICS): ");
                    String department = in.nextLine();
                    System.out.print("Units: ");
                    int units = in.nextInt();
                    in.nextLine();
                    System.out.print("Address: ");
                    String address = in.nextLine();
                    
                    Student stud = new Student(studentID, firstName, middleName, lastName, sex, birthDate, startYear, department, units, address);
                    
                    if(jdbc.insertStudent(stud))
                        System.out.println("The new student has been inserted in the table.");
                    else
                        System.out.println("The new student has not been inserted in the table.");
                    break;
                case '9': 
                    System.out.println("Thank you for using the user-input in the program!");
                    flag = false;
                    break;
                default:
                    System.out.println("Since your input is not from 1-9, the program will assume that you want to exit the program.");
                    System.out.println("Thank you for using the user-input in the program!");
                    flag = false;
            }
        }
    }
}
