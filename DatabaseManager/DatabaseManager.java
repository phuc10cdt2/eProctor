/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;
import ApplicationLayer.Message;
import DatabaseEntity.Course;
import DatabaseEntity.ExamRecord;
import DatabaseEntity.Proctor;
import DatabaseEntity.Student;
import DatabaseEntity.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 *
 * @author Phuc
 */
public class DatabaseManager {
    private AccountInterface studentMgr;
    private AccountInterface proctorMgr;
    public static ExamRecordInterface examRecordMgr;
    public static StudentHasCourseManager SHCMgr;
    public static Connection connection;
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/serverdb";
    static final String USERNAME = "root";
    static final String PASSWORD = "root";
    private Student studentAcc;
    private Proctor proctorAcc;
    private Message msg;
    private Message returnedMsg;
    private boolean result;
    public DatabaseManager()
    {
        connect();
        studentMgr = new StudentManager();
        proctorMgr = new ProctorManager();
        examRecordMgr = new ExamRecordManager();
        SHCMgr = new StudentHasCourseManager();
    }
    
    public void connect()
    {
        try{
            connection = DriverManager.getConnection(DATABASE_URL,USERNAME, PASSWORD);
        }
        catch(SQLException e)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
        }
    }
    public Message serveRequest(Message msg)
    {
        this.msg = msg;
        return null;
    }
    //debugging purpose
    public void printInfo()
    {
        System.out.println(studentAcc.getFirstName());
        System.out.println(studentAcc.getLastName());
        System.out.println(studentAcc.getMatricNo());
        System.out.println(studentAcc.getUsername());
        System.out.println(studentAcc.getDateOfBirth());
        System.out.println(studentAcc.getPassword());
        
        ArrayList <ExamRecord> examRecord= studentAcc.getExamRecord();
        for(ExamRecord record : examRecord)
        {
            System.out.printf("%-8s\t%-8s\t%-8s\t%-8s\t%-8s\t%-8s\n",record.getRecordID(), record.getExamDate(), record.getProctorID(), record.getStudentMatric(),record.getRemark(), record.getCourseCode());
        }
        
        ArrayList<Course> courses = studentAcc.getRegisteredCourses();
        for(Course course:courses)
        {
            System.out.printf("%-8s\t%-8s\t\n", course.getCourseCode(), course.getCourseName());
        }
    }
    //test method
    public void test()
    {
//        String username = "phucvl";
//        String password = "12345";
//        String first_name = "phuc";
//        String last_name = "nguyen dinh";
//        String date = "1992-11-02";
//        Date dob = java.sql.Date.valueOf(date);
//        studentAcc = new Student();
//        studentAcc.setMatricNo("u1220143g");
//        studentAcc.setFirst_name(first_name);
//        studentAcc.setLast_name(last_name);
//        studentAcc.setUsername(username);
//        studentAcc.setPassword(password);
//        studentAcc.setDateOfBirth(dob);
//        studentLogin(studentAcc);
//        printInfo();
    
//        String date = "1992-01-04";
//        DateTime examDate = java.sql.Date.valueOf(date);
//        ExamRecord record = new ExamRecord(2, examDate, null, );
    }
    //tested
    public boolean studentLogin(User user, String ipAddress)
    {
        result = studentMgr.authenticateAccount(user, ipAddress);
        if(result)
        {
            System.out.println("Login successfully");
        }
        else
        {
            System.out.println("Fail to login as a student");
        }
        return result;
    }
    
    public boolean proctorLogin(User user, String ipAddress)
    {
        result = proctorMgr.authenticateAccount(user, ipAddress);
        if(result)
            System.out.println("Login successfully");
        else
            System.out.println("Fail to login as a proctor");
        return result;
    }
    //only student can book, proctor can edit only
    public boolean bookExam(ExamRecord record)
    {
        result = examRecordMgr.addExamRecord(record);
        return result;
    }
    
    public boolean getListOfCandidate(ArrayList<ExamRecord> list)
    {
        result = examRecordMgr.getListCandidate(list);
        return result;
    }
    
    public boolean updateStudentProfile(User student)
    {
        result = studentMgr.updateProfile(student);
        return result;
    }
    
    public boolean updateProctorProfile(User proctor)
    {
        result = proctorMgr.updateProfile(proctor);
        return result;
    }
    
    public boolean bookInvigilation(ExamRecord record)
    {
        result = examRecordMgr.bookInvigilation(record);
        return result;
    }
    public boolean cancelInvigilation(ExamRecord record)
    {
        return examRecordMgr.cancelInvigilation(record);
    }
    //only student can drop exam record
    public boolean deleteExam(ExamRecord record)
    {
        result = examRecordMgr.deleteExamRecord(record);
        return result;
    }
    
    public boolean logOutProctor(User user)
    {
        result = proctorMgr.setStatusOffline(user);
        return result;
    }
    public boolean logOutStudent(User user)
    {
        return studentMgr.setStatusOffline(user);
    }
    public String getProctorIP(ExamRecord record){
        String proctorIP = null;
        proctorIP = examRecordMgr.getProctorIP(record);
        return proctorIP;
    }
    public boolean setAuthorized(ExamRecord record)
    {
        return examRecordMgr.setAuthorized(record);
    }
    
    public boolean checkAuthorized(ExamRecord record)
    {
        return examRecordMgr.checkAuthorized(record);
    }
}
