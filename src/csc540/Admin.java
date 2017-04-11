/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc540;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author mmukundram
 */
public class Admin {
    private String username;
    private String semester;
    private Scanner scan_in;
    private Connection DBConnection;
    
    public Admin(){
        username = null;
        semester = null;
        scan_in = new Scanner(System.in);
    }
    public Admin(String username, String semester, Connection DBConnection){
        this.username = username;
        this.semester = semester;
        scan_in = new Scanner(System.in);
        this.DBConnection = DBConnection;
    }
    public void set_username(String username){
        this.username = username;
    }
    public String get_username(){
        return this.username;
    }
    public void view_profile(){
        this.clear_screen();
        this.display_header();
        System.out.println("Press 0 to Go Back.");
        String admin_profile_string = "select * from admin where admin_username=?";
        int choice=1;
        try{
            PreparedStatement admin_profile_statement = DBConnection.prepareStatement(admin_profile_string);
            admin_profile_statement.setString(1,username);
            
            ResultSet admin_profile_result = admin_profile_statement.executeQuery();
            
            if(admin_profile_result.next()){
                System.out.println("1.First Name\t:\t"+admin_profile_result.getString(2));
                System.out.println("2.Last Name\t:\t"+admin_profile_result.getString(3));
                System.out.println("3.D.O.B\t\t:\t"+admin_profile_result.getString(4));
                System.out.println("4.Employee ID\t:\t"+admin_profile_result.getString(1));
                do{
                    choice = scan_in.nextInt();
                    if(choice==0){
                        return;
                    }
                    else{
                        System.out.println("Invalid choice. Press 0 to Go Back.");
                    }
                }while(true);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void view_course(){
        String course_id;
        this.clear_screen();
        this.display_header();
        System.out.print("Please Enter Course ID\t\t\t:\t");
        course_id = scan_in.next();
        String course_check_string = "select * from course where id=?";
        
        try{
            PreparedStatement course_check_statement = DBConnection.prepareStatement(course_check_string);
            course_check_statement.setString(1,course_id);
            
            ResultSet course_check_result = course_check_statement.executeQuery();
            
            if(course_check_result.next()){
                System.out.println("1.Course Name\t\t\t\t:\t"+course_check_result.getString("name"));
                System.out.println("2.Department Name\t\t\t:\t"+course_check_result.getString("department"));
                System.out.println("3.Level\t\t\t\t\t:\t"+course_check_result.getString("course_level"));
                System.out.println("4.GPA Requirement(if any)\t\t:\t"+course_check_result.getString("min_gpa_requirement"));
                System.out.println("5.List Of Prerequisite Courses(if any)\t:\t");
                PreparedStatement course_prerequisite_statement = DBConnection.prepareStatement("select * from course_prerequisite where course_id=?");
                course_prerequisite_statement.setString(1,course_id);
                ResultSet course_prerequisite_result = course_prerequisite_statement.executeQuery();
                int i=1;
                if(course_prerequisite_result.next()){
                    System.out.println("\t\t\t\t\t\t"+i+"."+course_prerequisite_result.getString("prerequisite_course_id"));
                    ++i;
                }
                System.out.println("6.Special Approval Required\t\t:\t"+course_check_result.getString("special_permission"));
                System.out.print("7.Number Of Credits\t\t\t:\t");
                if(course_check_result.getString("min_credit").equals(course_check_result.getString("max_credit"))){
                    System.out.println(course_check_result.getString("min_credit"));
                }
                else{
                    System.out.println(course_check_result.getString("min_credit")+"-"+course_check_result.getString("max_credit"));
                }
            }
            else{
                System.out.println("Invalid Course ID.");
                
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        System.out.print("\n\nPress 0 To Go Back\t:\t");
        int temp = scan_in.nextInt();
        while(temp!=0) {
            temp = scan_in.nextInt();
        }
        return;
    }
    public void add_course(){
        String course_id;
        this.clear_screen();
        this.display_header();
        System.out.print("Please Enter Course ID\t:\t");
        course_id = scan_in.next();
        String course_check_string = "select * from course where id=?";
        
        try{
            PreparedStatement course_check_statement = DBConnection.prepareStatement(course_check_string);
            course_check_statement.setString(1,course_id);
            
            ResultSet course_check_result = course_check_statement.executeQuery();
            
            if(course_check_result.next()){
                System.out.println("Course ID Already Exists.");
                System.out.println("Press 0 To Go Back\t:\t");
                int temp = scan_in.nextInt();
                return;
            }
            else{
                String name,department,course_level,credit,class_size,waitlist_size,min_credit,max_credit,special_permission;
                int min_gpa_requirement;
                scan_in.nextLine();
                System.out.print("1.Course Name\t:\t");
                name = scan_in.nextLine();
                System.out.print("2.Department Name\t:\t");
                department = scan_in.next();
                System.out.print("3.Level\t\t:\t");
                course_level = scan_in.next();
                System.out.print("4.GPA Requirement(type any negative number if none)\t:\t");
                min_gpa_requirement = scan_in.nextInt();
                System.out.print("5.Number of Credits\t:\t");
                credit = scan_in.next();
                if(credit.contains("-")){
                    String[] parts = credit.split("-");
                    min_credit = parts[0];
                    max_credit = parts[1];
                }
                else{
                    min_credit = credit;
                    max_credit = credit;
                }
                System.out.print("6.Special Permission[yes/no]\t:\t");
                special_permission = scan_in.next();
                String course_insert_string = "insert into course values(?,?,?,?,?,?,?,?)";
                try{
                    PreparedStatement course_insert_statement = DBConnection.prepareStatement(course_insert_string);
                    course_insert_statement.setString(1,course_id);
                    course_insert_statement.setString(2,name);
                    course_insert_statement.setString(3,min_credit);
                    course_insert_statement.setString(4,max_credit);
                    course_insert_statement.setString(5,department);
                    course_insert_statement.setString(6,course_level.toLowerCase());
                    if(min_gpa_requirement >= 0){
                        course_insert_statement.setString(7,Integer.toString(min_gpa_requirement));
                    }
                    else{
                        course_insert_statement.setString(7,Integer.toString(0));
                    }
                    course_insert_statement.setString(8,special_permission);
                    course_insert_statement.execute();
                    System.out.println("Done"); 
                }
                catch(Exception e){
                    System.out.println(e);
                }
                System.out.println("8.List Of Prerequisite Courses(type 'done')\t:\t");
                String temp;
                while(true){
                    temp = scan_in.next().toLowerCase();
                    if(temp.equals("none")){
                        break;
                    }
                    try{
                        PreparedStatement prerequisite_insert_statement = DBConnection.prepareStatement("insert into course_prerequisite values(?,?)");
                        prerequisite_insert_statement.setString(1,course_id);
                        prerequisite_insert_statement.setString(2,temp);
                        prerequisite_insert_statement.execute();
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void view_course_offering(){
        this.clear_screen();
        this.display_header();
        System.out.print("Please Enter Course ID\t:\t");
        String course_id = scan_in.next();
        String course_check_string = "select * from offering where course_id=?";
        try{
            PreparedStatement course_check_statement = DBConnection.prepareStatement(course_check_string);
            course_check_statement.setString(1,course_id);
            ResultSet course_check_result = course_check_statement.executeQuery();

            if(course_check_result.next()){ 
                System.out.println("1.Semester\t:\t"+course_check_result.getString("semester_name"));
                System.out.println("2.Faculty\t:");
                PreparedStatement faculty_statement = DBConnection.prepareStatement("select * from faculty where course_id=?");
                faculty_statement.setString(1,course_id);
                ResultSet faculty_result = faculty_statement.executeQuery();
                while(faculty_result.next()){
                    System.out.println(faculty_result.getString("faculty_name"));
                }
                PreparedStatement schedule_statement = DBConnection.prepareStatement("select * from schedule where course_id=?");
                schedule_statement.setString(1,course_id);
                ResultSet schedule_result = schedule_statement.executeQuery();
                System.out.println("3.Schedule\t:\t");
                while(schedule_result.next()){
                    System.out.println("\t\t\t"+schedule_result.getString("day_of_week")+"\t"+schedule_result.getString("start_time")+"-"+schedule_result.getString("end_time"));                          
                }
                System.out.println("4.Location\t:\t"+course_check_result.getString("location"));
                System.out.println("5.Class Size\t:\t"+course_check_result.getString("class_size"));
                System.out.println("6.Waitlist Size\t:\t"+course_check_result.getString("waitlist_size"));
                System.out.println("0.Press 0 To Go Back");
                int temp = scan_in.nextInt();
                return;
            }
            else{
                System.out.println("Invalid Course ID.");
                System.out.print("Press 0 To Go Back\t:\t");
                int temp = scan_in.nextInt();
                return;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void add_course_offering(){
        String course_id,semester_name,faculty,location,class_size,waitlist_size,year;
        System.out.print("1.Enter Course ID\t:\t");
        course_id = scan_in.next();
        System.out.print("2.Enter Semester[fall/spring]\t:\t");
        semester_name = scan_in.next();
        semester_name = semester_name+" ";
        System.out.print("3.Enter Year[yyyy]\t:\t");
        year = scan_in.next();
        semester_name = semester_name+year;
        System.out.println("4.Enter Faculty Name(multiple; type 'done')\t:\t");
        while(true){
            scan_in.nextLine();
            faculty = scan_in.nextLine();
            if(faculty.toLowerCase().equals("done")){
                break;
            }
            try{            
                PreparedStatement faculty_insert_statement = DBConnection.prepareStatement("insert into faculty values(?,?,?)");
                faculty_insert_statement.setString(1,course_id);
                faculty_insert_statement.setString(2,semester_name);
                faculty_insert_statement.setString(3,faculty);
                faculty_insert_statement.execute();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        System.out.println("5.Enter days of the week(type 'done')\t:\t");
        ArrayList<String> days = new ArrayList<String>();
        String day;
        while(true){
            day = scan_in.next();
            if(day.equals("done")){
                break;
            }
            days.add(day);
        }
        System.out.println("6.Enter start time and end time of classes on respective days in order [Format - 'HHMM HHMM']\t:\t");
        int i;
        String start_time,end_time; 
        for(i=0;i<days.size();++i){
            start_time = scan_in.next();
            end_time = scan_in.next();
            try{
                PreparedStatement schedule_statement = DBConnection.prepareStatement("insert into schedule values(?,?,?,?,?)");
                schedule_statement.setString(1,course_id);
                schedule_statement.setString(2,semester_name);
                schedule_statement.setString(3,days.get(i));
                schedule_statement.setString(4,start_time);
                schedule_statement.setString(5,end_time);
                schedule_statement.execute();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        scan_in.nextLine();
        System.out.print("7.Enter Location\t:\t");
        location = scan_in.nextLine();
        System.out.print("8.Enter Class Size\t:\t");
        class_size = scan_in.next();
        System.out.print("9.Enter Waitlist Size\t:\t");
        waitlist_size = scan_in.next();
        
        try{
            PreparedStatement offering_statement = DBConnection.prepareStatement("insert into offering values(?,?,?,?,?,?)");
            offering_statement.setString(1,course_id);
            offering_statement.setString(2,semester_name);
            offering_statement.setString(3,location);
            offering_statement.setString(4,class_size);
            offering_statement.setString(5,waitlist_size);
            offering_statement.setString(6,"0");                      // Hard-coding to 0 when a course offering is created
            offering_statement.execute();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void enroll_student(){
        String id,department,first_name,last_name,date_of_birth,student_level,residency,phone,email,address,amount,student_username;
        System.out.print("Enter Student ID\t:\t");
        id = scan_in.next();
        System.out.print("Enter Department\t:\t");
        department = scan_in.next();
        System.out.print("Enter First Name\t:\t");
        first_name = scan_in.next();
        System.out.print("Enter Last Name\t:\t");
        //scan_in.nextLine();
        last_name = scan_in.next();
        System.out.print("Enter Date of Birth\t:\t");
        date_of_birth = scan_in.next();
        System.out.print("Enter Student Level\t:\t");
        student_level = scan_in.next().toLowerCase();
        System.out.print("Enter Student Residency\t:\t");
        scan_in.nextLine();
        residency = scan_in.nextLine().toLowerCase();
        System.out.print("Enter Student Phone Number\t:\t");
        phone = scan_in.next();
        System.out.print("Enter Student Email ID\t:\t");
        email = scan_in.next();
        System.out.print("Enter Student Address\t:\t");
        scan_in.nextLine();
        address = scan_in.nextLine();
        System.out.print("Enter Amount Owed(enter 'none' if none)\t:\t");
        amount = scan_in.next();
        if(amount.equals("none")){
            amount = "0";
        }
        System.out.print("Enter Student Username\t:\t");
        student_username = scan_in.next();
        
        try{
            PreparedStatement student_insert_statement = DBConnection.prepareStatement("insert into student(id,department,first_name,last_name,date_of_birth,student_level,residency,gpa,phone,email,address,amount,student_username) values (?,?,?,?,TO_DATE(?,'yyyy/mm/dd'),?,?,?,?,?,?,?,?)");
            student_insert_statement.setString(1,id);
            student_insert_statement.setString(2,department);
            student_insert_statement.setString(3,first_name);
            student_insert_statement.setString(4,last_name);
            student_insert_statement.setString(5,date_of_birth);
            student_insert_statement.setString(6,student_level);
            student_insert_statement.setString(7,residency);
            student_insert_statement.setString(8,"0");
            student_insert_statement.setString(9,phone);
            student_insert_statement.setString(10,email);
            student_insert_statement.setString(11,address);
            student_insert_statement.setString(12,amount);
            student_insert_statement.setString(13,student_username);
            
            student_insert_statement.execute();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void view_grades(String student_id) {
        try{
            PreparedStatement grade_view_statement = DBConnection.prepareStatement("select * from enrollment where student_id=? and status=?");
            grade_view_statement.setString(1,student_id);
            grade_view_statement.setString(2,"completed");
            //grade_view_statement.setString(2,this.semester);
            ResultSet grade_view_result = grade_view_statement.executeQuery();
            while(grade_view_result.next()){
                System.out.println("\nCourse ID : "+ grade_view_result.getString("course_id")+"\t\tLetter Grade : "+ grade_view_result.getString("grade"));
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        System.out.println("\nPress 0 to go back");
        int choice = 1;
        while(choice!=0) {
            choice = scan_in.nextInt();
        }
    }
    public void enter_grades(String student_id) {
        boolean found=false;
        String credits="";
        try{
            PreparedStatement grade_view_statement = DBConnection.prepareStatement("select * from enrollment where student_id=? and status=?");
            grade_view_statement.setString(1,student_id);
            grade_view_statement.setString(2,"enrolled");
            
            ResultSet grade_view_result = grade_view_statement.executeQuery();
            while(grade_view_result.next()){
                System.out.println("\nCourse ID : "+ grade_view_result.getString("course_id")+"\t\tStatus : "+ grade_view_result.getString("status"));
                credits = grade_view_result.getString("credits");
                found = true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        if(found) {
            String course_id,grade;
            System.out.print("Enter the Course_ID: ");
            course_id = scan_in.next();
            System.out.print("Enter the Grade (A to C-): ");
            grade = scan_in.next();
            String enter_grade_string = "insert into dummy(student_id,course_id,semester_name,grade,status,credits) values(?,?,?,?,?,?)";//grade=?,status=? where student_id=? and course_id=?";// and status=?";
            
            try{
                PreparedStatement enter_grade_statement = DBConnection.prepareStatement(enter_grade_string);
                enter_grade_statement.setString(1,student_id);
                enter_grade_statement.setString(2,course_id);
                enter_grade_statement.setString(3,this.semester);
                enter_grade_statement.setString(4,grade);
                enter_grade_statement.setString(5,"completed");
                enter_grade_statement.setString(6,credits);

                enter_grade_statement.executeUpdate();
                
            }
            catch(Exception e){
                System.out.println(e);
            }
            
        }
        else{
            System.out.println("No courses to be graded");
        }
        System.out.println("\nPress 0 to go back");
        int choice = 1;
        while(choice!=0) {
            choice = scan_in.nextInt();
        }
    }
    public void view_student(){
        String id;
        boolean found=false;
        System.out.print("Please Enter Student ID\t\t:\t");
        id = scan_in.next();
        try{
            PreparedStatement student_view_statement = DBConnection.prepareStatement("select * from student where id=?");
            student_view_statement.setString(1,id);
            ResultSet student_view_result = student_view_statement.executeQuery();
            if(student_view_result.next()){
                found=true;
                System.out.println("1.First Name\t\t\t:\t"+student_view_result.getString("first_name"));
                System.out.println("2.Last Name\t\t\t:\t"+student_view_result.getString("last_name"));
                System.out.println("3.Date Of Birth\t\t\t:\t"+student_view_result.getString("date_of_birth"));
                System.out.println("4.Student's Department\t\t:\t"+student_view_result.getString("department"));
                System.out.println("5.Student's Level\t\t:\t"+student_view_result.getString("student_level"));
                System.out.println("6.Student's Residency Status\t:\t"+student_view_result.getString("residency"));
                System.out.println("7.GPA\t\t\t\t:\t"+student_view_result.getString("gpa"));
                System.out.println("8.Amount Owed\t\t\t:\t"+student_view_result.getString("amount"));
                System.out.println("9.Phone\t\t\t\t:\t"+student_view_result.getString("phone"));
                System.out.println("10.Email\t\t\t:\t"+student_view_result.getString("email"));
                System.out.println("11.Address\t\t\t:\t"+student_view_result.getString("address"));
            }
            else{
                System.out.println("Incorrect Student ID.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        if(found) {
            int choice;
            do{
                System.out.println("\nPress 0 To Go Back.");
                System.out.println("1. View Grades.");
                System.out.println("2. Enter Grades.");
                
                choice = scan_in.nextInt();
                switch(choice){
                    case 0:
                        break;
                    case 1: {
                        this.view_grades(id);
                        break;
                    }    
                    case 2: {
                        this.enter_grades(id);
                        break;
                    }
                    default:
                        System.out.println("Incorrect Choice! Try again");     
                }
            
                //System.out.println("Please Enter 0 - 2 only");            
            }while(choice!=0);
        }
        return;

    }
    public void special_enrollment(){
        System.out.println("List Of Pending Requests:");
        this.clear_screen();
        this.display_header();
        boolean found=false;
        try{
            ArrayList<String> course_id = new ArrayList<String>();
            ArrayList<String> student_id = new ArrayList<String>();
            ArrayList<String> credits = new ArrayList<String>();
            PreparedStatement special_enrollment_statement = DBConnection.prepareStatement("select * from enrollment where status='spperm'");
            ResultSet special_enrollment_result = special_enrollment_statement.executeQuery();
            int i=1;
            while(special_enrollment_result.next()){
                found = true;  
                if(i==1){
                    System.out.println("  COURSE_ID\t\tSTUDENT_ID");
                }
                System.out.println(Integer.toString(i) + ". " + special_enrollment_result.getString("course_id") + "\t\t" + special_enrollment_result.getString("student_id"));
                course_id.add(special_enrollment_result.getString("course_id"));
                student_id.add(special_enrollment_result.getString("student_id"));
                credits.add(special_enrollment_result.getString("credits"));
                
                ++i;
            }
            System.out.println("Press 0 To Go Back.");
            System.out.println("Enter the number to be approved");
            int choice = scan_in.nextInt();
            // choice-1 is the index to use
            //PreparedStatement enrolled_statement = DBConnection.prepareStatement("update enrollment set status='enrolled' where course_id=? and student_id=?");
            PreparedStatement enrolled_statement = DBConnection.prepareStatement("insert into dummy(status,course_id,student_id,semester_name,credits) values(?,?,?,?,?)");
            enrolled_statement.setString(1,"spperm");
            enrolled_statement.setString(2,course_id.get(choice-1));
            enrolled_statement.setString(3,student_id.get(choice-1));
            enrolled_statement.setString(4,this.semester);
            enrolled_statement.setString(5,credits.get(choice-1));
            enrolled_statement.execute();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    public void enforce_deadline(){
        String semester, year, choice;
        System.out.print("Enter Semester[fall/spring]\t:\t");
        semester = scan_in.next().toLowerCase();
        semester = semester + " ";
        System.out.print("Enter Year\t:\t");
        year = scan_in.next();
        semester = semester + year;
        System.out.print("Are you sure you want to enforce deadlines for " + semester + "?[yes/no]\t:\t");
        choice = scan_in.next().toLowerCase();
        try{
            PreparedStatement enforce_deadline_statement = DBConnection.prepareStatement("insert into enforce_deadline values(?,?)");
            enforce_deadline_statement.setString(1,semester);
            enforce_deadline_statement.setString(2,this.username);
            enforce_deadline_statement.execute();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void home(){
        int choice = 9;
        int inner_choice;
        while(true){
            this.clear_screen();
            this.display_header();
            System.out.println("1.View Profile");
            System.out.println("2.Enroll A New Student");
            System.out.println("3.View Student's Details");
            System.out.println("4.View/Add Courses");
            System.out.println("5.View/Add Course Offering");
            System.out.println("6.View/Approve Special Enrollment Requests");
            System.out.println("7.Enforce Add/Drop Deadline");
            System.out.println("8.Logout");
            
            choice = scan_in.nextInt();
            
            switch(choice){
                case 1:{
                    this.view_profile();
                    break;
                }
                case 2:{
                    this.enroll_student();
                    break;
                }
                case 3:{
                    this.view_student();
                    break;
                }
                case 4:{
                    System.out.println();
                    this.clear_screen();
                    this.display_header();
                    System.out.println("0.Go Back To Previous Menu");
                    System.out.println("1.View Course");
                    System.out.println("2.Add Course");
                    inner_choice = scan_in.nextInt();
                    if(inner_choice == 0){
                        break;
                    }
                    else if(inner_choice == 1){
                        this.view_course();
                    }
                    else if(inner_choice == 2){
                        this.add_course();
                    }
                    else{
                        System.out.println("Incorrect Choice.");
                    }
                    break;
                }
                case 5:{
                    System.out.println();
                    System.out.println("0.Press 0 To Go Back");
                    System.out.println("1.View Course Offering");
                    System.out.println("2.Add Course Offering");
                    inner_choice = scan_in.nextInt();
                    if(inner_choice == 0){
                        break;
                    }
                    if(inner_choice == 1){
                        this.view_course_offering();
                    }
                    else if(inner_choice == 2){
                        this.add_course_offering();
                    }
                    else{
                        System.out.println("Incorrect Choice.");
                    }
                    break;
                }
                case 6:{
                    this.special_enrollment();
                    break;
                }
                case 7:{
                    this.enforce_deadline();
                    break;
                }
                case 8:{
                    return;
                }
                default:{
                    System.out.println("Incorrect Choice.");
                }
            }
            
        }
    }
    
    public void display_header(){
        System.out.println("**********************************************************");
        System.out.println("\t\tCOURSEBOOK");
        System.out.println("**********************************************************");
    }
    
    public void clear_screen(){
        for(int i=0;i<20;++i){
            System.out.println();
        }
    }
    
    public void not_supported(){
        System.out.println("Sorry. But this functionality is not supported yet.");
        System.out.println("Please come back and check later.");
    }
}
