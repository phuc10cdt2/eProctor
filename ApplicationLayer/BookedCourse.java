/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ApplicationLayer;

import DatabaseEntity.Course;
import DatabaseEntity.ExamRecord;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author phongnt
 */

public class BookedCourse extends Course{
    private Timestamp examDate;
    private boolean enableStartExam;
    public BookedCourse(String courseCode, String courseName){
        super(courseCode, courseName);
        examDate=null;
        enableStartExam = true;
    }

    public boolean isEnableStartExam() {
        return enableStartExam;
    }

    public void setEnableStartExam(boolean enableStartExam) {
        this.enableStartExam = enableStartExam;
    }

    public BookedCourse(String courseCode, String courseName, List<ExamRecord> examRecord){
        super(courseCode, courseName);
        for(ExamRecord record: examRecord){
            if( record.getCourseCode().equalsIgnoreCase(courseCode))
            {
//                this.examDate=new Date(record.getExamDate().getTime());
                if(record.getExamDate()==null)
                    examDate = null;
                else
                    this.examDate = record.getExamDate();
            }
        }
        enableStartExam = true;
    }
    
    public boolean isBooked(){
        return (examDate!=null);
    }
    public Timestamp getExamDate(){
        return examDate;
    }
    public void setExamDate(Timestamp date){
        this.examDate=date;
    }
    
    
}
