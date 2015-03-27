/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseManager;

import java.util.Date;

/**
 *
 * @author Phuc
 */
public class Table {
    public enum listOfTable{
        STUDENT("student"),
        PROCTOR("proctor"), 
        EXAM_RECORD("exam_record"), 
        COURSE("course"), 
        STUDENT_HAS_COURSE("student_has_course");
        
        private String tableName;
        private listOfTable(String name)
        {
            tableName = name;
        }
        public String getTableName()
        {
            return tableName;
        }
    }
    public static java.sql.Date convertDate(Date date)
    {
        return new java.sql.Date(date.getTime());
    }
}
