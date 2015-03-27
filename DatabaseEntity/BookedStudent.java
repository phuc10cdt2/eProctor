/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseEntity;
import java.sql.Date;
/**
 *
 * @author ASUS User
 */
public class BookedStudent {
    private Date examDate;
    private String studentName;
    private String studentMatricNo;
    private String studentInfo;
    public BookedStudent() {
    }
    public BookedStudent(Date bookDate, String studentName, String studentMatricNo, String studentInfo) {
        this.examDate = bookDate;
        this.studentName = studentName;
        this.studentMatricNo = studentMatricNo;
        this.studentInfo = studentInfo;
    }
    public void setBookDate(Date bookDate) {
        this.examDate = bookDate;
    }
    public Date getBookDate() {
        return this.examDate;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public void setStudentMatricNo(String studentMatricNo) {
        this.studentMatricNo = studentMatricNo;
    }
    public String getStudentMatricNo() {
        return this.studentMatricNo;
    }
    public void setStudentInfo(String studentInfo) {
        this.studentInfo = studentInfo;
    }
    public String getStudentInfo() {
        return this.studentInfo;
    }
}
