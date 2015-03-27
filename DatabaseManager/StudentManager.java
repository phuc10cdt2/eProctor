/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import DatabaseEntity.Proctor;
import DatabaseEntity.Student;
import DatabaseEntity.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 *
 * @author Phuc
 */
public class StudentManager implements AccountInterface {
    public StudentManager(){
        
    }
    @Override
    public boolean updateProfile(User userAccount) {
        Student student = (Student)userAccount;
        PreparedStatement update = null;
        try {
            update = DatabaseManager.connection.prepareStatement("UPDATE " + Table.listOfTable.STUDENT + " SET first_name = ?, last_name = ?, date_of_birth = ?, username = ?, password = ? WHERE student_matric_no = ?");
            update.setString(1, student.getFirstName());
            update.setString(2, student.getLastName());
            update.setDate(3, Table.convertDate(student.getDateOfBirth()));
            update.setString(4, student.getUsername());
            update.setString(5, student.getPassword());
            update.setString(6, student.getMatricNo());
            update.executeUpdate();
            System.out.println("Successfully Updated profile");
            return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean authenticateAccount(User userAccount, String ipAddress) {
        int valid;
        boolean getRecord ;
        boolean check = false;
        PreparedStatement getInfo;
        ResultSet result;
        Student student = (Student)userAccount;
        
        try
        {
            getInfo = DatabaseManager.connection.prepareStatement("SELECT * from " + Table.listOfTable.STUDENT + " where username =? ");
            getInfo.setString(1, student.getUsername());
            result = getInfo.executeQuery();
            //if result set contains record
            while(result.next())
            {
                //check password
                if(result.getString("password").equals(student.getPassword()))
                {
                    student.setFirstName(result.getString("first_name"));
                    student.setLastName(result.getString("last_name"));
                    student.setMatricNo(result.getString("student_matric_no"));
                    student.setDateOfBirth(result.getDate("date_of_birth"));
                    student.setPassword(result.getString("password"));
                    student.addCourseList(DatabaseManager.SHCMgr.getCourseList(student.getMatricNo()));
                    getRecord = DatabaseManager.examRecordMgr.getExamRecord(student, 0);
                    valid = checkStatus(student);
                    if(valid==1)
                    {
                        student.setStatus(true);
                        check = true;
                    }
                    else if(valid == 0)
                    {
                        setStatusOnline(student);
                        check = true;
                    }
                    else
                        check = false;
                }
                else
                    break;
            }
            userAccount = (User)student;
            return check;
        }
        catch(SQLException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });
            System.out.println("Can not query student's info");
        }
        return false;
    }

    @Override
    public boolean setStatusOnline(User user) {
        PreparedStatement ps = null;
        int result = 0;
        Student student = (Student)user;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("UPDATE "+ Table.listOfTable.STUDENT + " SET status = ? WHERE student_matric_no = ?" );
            ps.setBoolean(1, true);
            ps.setString(2, student.getMatricNo());
            result = ps.executeUpdate();
            if(result >0)
                return true;
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public boolean setStatusOffline(User user) {
        PreparedStatement ps = null;
        Student student = (Student)user;
        int result = 0;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("UPDATE "+ Table.listOfTable.STUDENT + " SET status = ? WHERE student_matric_no = ?" );
            ps.setBoolean(1, false);
            ps.setString(2, student.getMatricNo());
            result = ps.executeUpdate();
            if(result >0)
            {
                return true;   
            }
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });            
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }
    
    @Override
    public int checkStatus(User user) {
        PreparedStatement ps = null;
        ResultSet result = null;
        Student student = (Student)user;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("SELECT status FROM "+ Table.listOfTable.STUDENT + " WHERE student_matric_no = ?" );
            ps.setString(1, student.getMatricNo());
            result = ps.executeQuery();
            
            while(result.next())
            {
                if(result.getBoolean("status"))
                    return 1;
                else
                    return 0;
                
            }
        }   catch (SQLException ex) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.showInformationDialog(null, "Database error");
                    }	
                });
                Logger.getLogger(ProctorManager.class.getName()).log(Level.SEVERE, null, ex);
                return 2;
            }
        return 2;
    }
    
    
}
