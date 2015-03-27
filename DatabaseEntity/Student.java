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
public class Student extends User implements Serializable{
    private String matricNo;
    private ArrayList<Course> courseList;

    
    public Student(String matricNo, String username, String password, String first_name, String last_name, Date dateOfBirth) {
        super(username, password, first_name, last_name, dateOfBirth);
        this.matricNo = matricNo;
        this.courseList = new ArrayList();
    }
    public Student(String matricNo, ArrayList<Course> courseList, String username, String password, String first_name, String last_name, Date dateOfBirth) {
        super(username, password, first_name, last_name, dateOfBirth);
        this.matricNo = matricNo;
        this.courseList = courseList;
    }
    public Student()
    {
        super();
    }
    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
    }
    
    public void addCourseList(ArrayList<Course> courses){
        courseList = courses;
    }
    
    public void addCourse(Course course){
        courseList.add(course);
    }
    
    public ArrayList<Course> getRegisteredCourses(){
        return this.courseList;
    }
    
    public void copy(Student student){
        super.copy(student);
        this.courseList=student.courseList;
        this.matricNo=student.matricNo;
    }
}
