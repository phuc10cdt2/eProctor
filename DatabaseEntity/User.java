/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseEntity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


/**
 *
 * @author Phuc
 */
public class User implements Serializable{
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date dateOfBirth;
    private boolean status;
    private ArrayList<ExamRecord> examRecord;
    
    public boolean getStatus() {
        return status;
    }

    public ArrayList<ExamRecord> getExamRecord() {
        return examRecord;
    }

    public void setExamRecord(ArrayList<ExamRecord> examRecord) {
        this.examRecord = examRecord;
    }
    
    
    public void addExamRecord(ExamRecord record){
        this.examRecord.add(record);
    }
    
    public void removeExamRecord(ExamRecord record){
        this.examRecord.remove(record);
    }


    public void setStatus(boolean status) {
        this.status = status;
    }

    public User(String username, String password, String first_name, String last_name, Date dateOfBirth) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dateOfBirth = dateOfBirth;
        examRecord = new ArrayList<ExamRecord>();
        status = false;
    }
    public User()
    {
        examRecord = new ArrayList<ExamRecord>();
        status = false;
    };
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void copy(User user){
        this.username=user.username;
        this.password=user.password;
        this.first_name=user.first_name;
        this.last_name=user.last_name;
        this.dateOfBirth=user.dateOfBirth;
        this.examRecord=user.examRecord;
        this.status=user.status;
    }
    
}
