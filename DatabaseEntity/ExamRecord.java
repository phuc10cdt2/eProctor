/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Phuc
 */
public class ExamRecord implements Serializable{
    private int recordID;
    private Timestamp examDate;
    private String proctorID;
    private String studentMatric;
    private String remark;
    private String courseCode;

    public ExamRecord(int recordID, Timestamp examDate, String proctorID, String studentMatric, String remark, String courseCode) {
        this.recordID = recordID;
        this.examDate = examDate;
        this.proctorID = proctorID;
        this.studentMatric = studentMatric;
        this.remark = remark;
        this.courseCode = courseCode;
    }
    
    public ExamRecord(String studentMatric, String courseCode, Timestamp examDate){
        this.recordID = 0;
        this.examDate = examDate;
        this.proctorID = null;
        this.studentMatric = studentMatric;
        this.remark = null;
        this.courseCode = courseCode;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public Timestamp getExamDate() {
        return examDate;
    }

    public void setExamDate(Timestamp examDate) {
        this.examDate = examDate;
    }

    public String getProctorID() {
        return proctorID;
    }

    public void setProctorID(String proctorID) {
        this.proctorID = proctorID;
    }

    public String getStudentMatric() {
        return studentMatric;
    }

    public void setStudentMatric(String studentMatric) {
        this.studentMatric = studentMatric;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
