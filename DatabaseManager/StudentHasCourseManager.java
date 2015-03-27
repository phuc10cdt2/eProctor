/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import DatabaseEntity.Course;
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
 * @author VAN00_000
 */
public class StudentHasCourseManager {
    public StudentHasCourseManager()
    {
        
    }
   public ArrayList<Course> getCourseList(String matric)
   {
       ArrayList<Course> courses = new ArrayList<Course>();
       PreparedStatement ps = null;
       String courseCode, courseName;
       try {
           String courseTb = Table.listOfTable.COURSE.toString();
           String shcTb = Table.listOfTable.STUDENT_HAS_COURSE.toString();
           ps = DatabaseManager.connection.prepareStatement("SELECT " + courseTb + ".course_code, " + courseTb + ".course_name" + " FROM "+courseTb + " INNER JOIN " + shcTb + " ON " + courseTb + ".course_code = " + shcTb + ".course_code " + " WHERE " + shcTb + ".student_matric_no =?");
           ps.setString(1, matric);
           ResultSet rs = ps.executeQuery();
           while(rs.next())
           {
               courseCode = rs.getString("course_code");
               courseName = rs.getString("course_name");
               courses.add(new Course(courseCode, courseName));
           }
       } catch (SQLException ex) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showInformationDialog(null, "Database error");
                }	
            });
           Logger.getLogger(StudentHasCourseManager.class.getName()).log(Level.SEVERE, null, ex);
       }
       return courses;
   }
}
