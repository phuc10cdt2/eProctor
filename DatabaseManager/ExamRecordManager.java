/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import DatabaseEntity.ExamRecord;
import DatabaseEntity.Proctor;
import DatabaseEntity.Student;
import DatabaseEntity.User;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;

/**
 *
 * @author Phuc
 */
public class ExamRecordManager implements ExamRecordInterface {

    @Override
    public boolean bookInvigilation(ExamRecord record){
        PreparedStatement ps = null;
        try
        {
            ps = DatabaseManager.connection.prepareStatement("UPDATE " + Table.listOfTable.EXAM_RECORD + " SET proctor_id = ? WHERE record_id = ?");
            {
                ps.setString(1, record.getProctorID());
                ps.setInt(2, record.getRecordID());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });             
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }   
        
    }

    @Override
    public boolean deleteExamRecord(ExamRecord record) {
        PreparedStatement ps = null;
        try{
            ps = DatabaseManager.connection.prepareStatement("DELETE FROM " + Table.listOfTable.EXAM_RECORD + " WHERE student_matric_no = ? AND course_code = ?");
            ps.setString(1, record.getStudentMatric());
            ps.setString(2, record.getCourseCode());
           
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public boolean getExamRecord(User userAccount, int n) {
        PreparedStatement ps = null;
        ResultSet result = null;
        Student student = null;
        Proctor proctor = null;
        
        //n = 0 if user is student, 1 if proctor
        if(n == 0)
        {
            student = (Student)userAccount;
            
        }
        else
        {
            proctor = (Proctor)userAccount;
        }
        try{
            if(n==0)
            {
                ps = DatabaseManager.connection.prepareStatement("SELECT * from " + Table.listOfTable.EXAM_RECORD + " where student_matric_no = ?");
                ps.setString(1, student.getMatricNo());
            }
            else{
                ps = DatabaseManager.connection.prepareStatement("SELECT * from " + Table.listOfTable.EXAM_RECORD + " where proctor_id = ?");
                ps.setString(1, proctor.getProctorID());
            }
            result = ps.executeQuery();
            while(result.next())
            {
                int record_id = result.getInt("record_id");
                Timestamp examDate = result.getTimestamp("exam_date");
                String studentMatric = result.getString("student_matric_no");
                String proctor_id = result.getString("proctor_id");
                String remark = result.getString("remark");
                String course_code = result.getString("course_code");
                ExamRecord examRecord = new ExamRecord(record_id, examDate,proctor_id,studentMatric, remark, course_code);
                if(n == 0)
                    student.getExamRecord().add(examRecord);
                else
                    proctor.getExamRecord().add(examRecord);
            }
            return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean addExamRecord(ExamRecord record) {
        PreparedStatement ps = null;
        ResultSet result = null;
    
        try{
            ps = DatabaseManager.connection.prepareStatement("INSERT into " + Table.listOfTable.EXAM_RECORD + " (exam_date, student_matric_no, course_code) VALUES" + "(?,?,?)");
            ps.setTimestamp(1, record.getExamDate());
            ps.setString(2, record.getStudentMatric());
            ps.setString(3, record.getCourseCode());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean getListCandidate(ArrayList<ExamRecord> list) {
        PreparedStatement ps = null;
        ResultSet result = null;
        
        try{
            ps = DatabaseManager.connection.prepareStatement("SELECT * FROM " + Table.listOfTable.EXAM_RECORD + " WHERE proctor_id is null");
            result = ps.executeQuery();
            while(result.next())
            {
                int id = result.getInt("record_id");
                Timestamp examDate = result.getTimestamp("exam_date");
                String studentMatric = result.getString("student_matric_no");
                String proctor_id = result.getString("proctor_id");
                String remark = result.getString("remark");
                String course_code = result.getString("course_code");
                ExamRecord examRecord = new ExamRecord(id, examDate,proctor_id,studentMatric, remark, course_code);
                list.add(examRecord);
            }
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public String getProctorIP(ExamRecord record) {
        PreparedStatement ps = null;
        ResultSet result = null;
        
        try{
            ps = DatabaseManager.connection.prepareStatement("SELECT ip_address FROM " + Table.listOfTable.EXAM_RECORD + 
                                                                " e JOIN " + Table.listOfTable.PROCTOR + " p ON e.proctor_id = p.proctor_id" + " WHERE student_matric_no = ? AND course_code = ?");
            ps.setString(1, record.getStudentMatric()); 
            ps.setString(2, record.getCourseCode());
            result = ps.executeQuery();
            while(result.next())
            {
            
                return result.getString("ip_address");
            }
        } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    @Override
    public boolean cancelInvigilation(ExamRecord record) {
       PreparedStatement ps = null;
       ResultSet result = null;
       try
       {
           ps = DatabaseManager.connection.prepareStatement("UPDATE " + Table.listOfTable.EXAM_RECORD + " SET proctor_id = NULL WHERE record_id = ?");
           ps.setInt(1, record.getRecordID());
           int rows = ps.executeUpdate();
           if(rows>0)
               return true;
       } catch (SQLException ex) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.showInformationDialog(null, "Database error");
                    }	
                }); 
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       return false;
    }

    @Override
    public boolean setAuthorized(ExamRecord record) {
        PreparedStatement ps = null;
        ResultSet result = null;
        try
       {
           ps = DatabaseManager.connection.prepareStatement("UPDATE " + Table.listOfTable.EXAM_RECORD + " SET status = ? WHERE student_matric_no = ? AND course_code = ?");
           ps.setBoolean(1, true);
           ps.setString(2, record.getStudentMatric());
           ps.setString(3, record.getCourseCode());
           int rows = ps.executeUpdate();
           if(rows>0)
               return true;
       } catch (SQLException ex) {
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    @Override
    public boolean checkAuthorized(ExamRecord record) {
        PreparedStatement ps = null;
        ResultSet result = null;
        boolean status;
        try
       {
           ps = DatabaseManager.connection.prepareStatement("SELECT status FROM " + Table.listOfTable.EXAM_RECORD + " WHERE student_matric_no = ? AND course_code = ?");
           ps.setString(1, record.getStudentMatric());
           ps.setString(2, record.getCourseCode());
           result = ps.executeQuery();
           while(result.next())
           {
               status = result.getBoolean("status");
               if(status)
                   return true;
               else
                   return false;
           }
       } catch (SQLException ex) {
            Logger.getLogger(ExamRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }
    
}
