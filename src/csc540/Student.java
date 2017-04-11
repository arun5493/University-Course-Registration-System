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
 * @author Arun
 */
public class Student {
    private String semester;
    private String username;
    private String id;
    private Scanner scan_in;
    private Connection DBConnection;

    public void get_student_id() {
        String student_check_string = "select * from student where student_username=?";
        try{
            PreparedStatement student_check_statement = DBConnection.prepareStatement(student_check_string);
            student_check_statement.setString(1,this.username);
            ResultSet student_check_result = student_check_statement.executeQuery();
            
            if(student_check_result.next()) {
                this.id = student_check_result.getString("id");
            }
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }
            
    public Student(){
        username = null;
        id = null;
        semester = null;
        scan_in = new Scanner(System.in);
    }
    public Student(String username,String semester, Connection DBConnection){
        this.username = username;
        this.semester = semester;
        scan_in = new Scanner(System.in);
        this.DBConnection = DBConnection;
        get_student_id();
        
    }
    
    public void view_edit_profile() {
        int choice = 7;
        do {
            this.clear_screen();
            this.display_header();
            String first_name,edit_field,last_name,email,phone,student_level,residency;
            String student_check_string = "select * from student where id=?";

            try{
                PreparedStatement student_check_statement = DBConnection.prepareStatement(student_check_string);
                student_check_statement.setString(1,this.id);
                ResultSet student_check_result = student_check_statement.executeQuery();
            
                if(student_check_result.next()){
                    System.out.println("Press 0 to go back");
                    first_name = student_check_result.getString("first_name");
                    last_name = student_check_result.getString("last_name");
                    email = student_check_result.getString("email");
                    phone = student_check_result.getString("phone");
                    student_level = student_check_result.getString("student_level");
                    residency = student_check_result.getString("residency");
                    System.out.println("1.First Name\t:\t"+first_name);
                    System.out.println("2.Last Name\t:\t"+last_name);
                    System.out.println("3.Email\t:\t"+email);
                    System.out.println("4.Phone\t\t:\t"+phone);
                    System.out.println("5.Level\t\t:\t"+student_level);
                    System.out.println("6.Status\t\t:\t"+residency);
                
                    System.out.println("Choose a field to edit (1-4) or 0 to go back : ");
                    choice = scan_in.nextInt();
                    if(choice==0){
                        return;
                    }
                    else if(choice == 1) {
                        System.out.println("Enter the First Name : \n");
                        scan_in.nextLine();
                        first_name = scan_in.nextLine();
                        //not_supported();
                    }
                    else if(choice == 2) {
                        System.out.println("Enter the Last Name : \n");
                        scan_in.nextLine();
                        last_name = scan_in.nextLine();
                    }
                    else if(choice == 3) {
                        System.out.println("Enter Email : \n");
                        scan_in.nextLine();
                        email = scan_in.nextLine();
                    }
                    else if(choice == 4) {
                        System.out.println("Enter Phone Number : \n");
                        scan_in.nextLine();
                        phone = scan_in.nextLine();
                    }
                    else if(choice==5 || choice==6){
                        System.out.println("Cannot Modify. Choose options from 1-4 onlyPress 0 to Go Back.");
                    }
                    else {
                        System.out.println("Invalid choice. Press 0 to Go Back.");
                    }
                    if(choice == 1 || choice== 2 || choice == 3 || choice == 4) {
                        String student_insert_string = "update student set first_name=?,last_name=?,email=?,phone=? where student_username=?";
                        try{
                            PreparedStatement student_insert_statement = DBConnection.prepareStatement(student_insert_string);
                            student_insert_statement.setString(1,first_name);
                            student_insert_statement.setString(2,last_name);
                            student_insert_statement.setString(3,email);
                            student_insert_statement.setString(4,phone);
                            student_insert_statement.setString(5,username);
                            
                            student_insert_statement.executeUpdate();
                        }
                        catch(Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }    
            catch(Exception e){
                System.out.println(e);
            }
        }while(choice != 0);
        return;
        //not_supported();
    }
    
    public void view_available_courses() {
        this.clear_screen();
        this.display_header();
        
        String available_courses_string = "select * from offering where semester_name=?";

            try{
                PreparedStatement available_courses_statement = DBConnection.prepareStatement(available_courses_string);
                available_courses_statement.setString(1,this.semester);             // Hardcoded for spring 2017 for now
                ResultSet available_courses_result = available_courses_statement.executeQuery();
                
                while(available_courses_result.next()) {
                    String course_name_string = "select name from course where id=? ";
                    try{
                        PreparedStatement course_name_statement = DBConnection.prepareStatement(course_name_string);
                        course_name_statement.setString(1,available_courses_result.getString("course_id"));
                        ResultSet course_name_result = course_name_statement.executeQuery();
                        
                        if(course_name_result.next()) {
                            System.out.println("\nCourse ID : "+ available_courses_result.getString("course_id")+ "\t\tCourse Name : "+ course_name_result.getString("name"));
                            //System.out.println("Course Name \t:\t"+ course_name_result.getString("name"));
                        }
                        
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }
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
    
    public void enroll() {
        this.clear_screen();
        this.display_header();
        boolean fixed_credit = false;
        String course_id,no_of_credits="",min_credit,max_credit;
        System.out.print("1.Enter Course ID\t:\t");
        course_id = scan_in.next();

        String check_course_credits_string = "select * from course where id = ?";
        try{
            PreparedStatement check_course_credits_statement = DBConnection.prepareStatement(check_course_credits_string);
            check_course_credits_statement.setString(1,course_id);

            ResultSet check_course_credits_result = check_course_credits_statement.executeQuery();
            if(check_course_credits_result.next()) {
                max_credit = check_course_credits_result.getString("max_credit");
                min_credit = check_course_credits_result.getString("min_credit");
                if(min_credit.equals(max_credit)) {
                    //System.out.println("The credits are equal");
                    fixed_credit = true;
                    no_of_credits = max_credit;
                }
                if(!fixed_credit) {
                    System.out.print("1.Enter Number of credits \t:\t");
                    no_of_credits = scan_in.next();
                }
                String enroll_string = "insert into dummy(student_id,course_id,semester_name,status,credits) values(?,?,?,?,?)";
                try{

                    PreparedStatement enroll_statement = DBConnection.prepareStatement(enroll_string);
                    enroll_statement.setString(1,this.id);              
                    enroll_statement.setString(2,course_id);
                    enroll_statement.setString(3,this.semester);
                    enroll_statement.setString(4,"enrolled");       // Hardcoding to enrolled. Will be checked in the backend and changed appropriately.
                    enroll_statement.setString(5,no_of_credits);
                    enroll_statement.execute();
                    System.out.println("\nEnrolled!"); 
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
            else{
                System.out.println("\n\nSorry, Course not available");
            }
        }
        catch(Exception e){
           System.out.println(e); 
        }
        System.out.println("\nPress 0 to go back: ");
        int choice = 1;
        while(choice!=0) {
            choice = scan_in.nextInt();
        }   
                
    }
   
    public void view_my_course() {
        this.clear_screen();
        this.display_header();
        
        String view_courses_string = "select * from enrollment where semester_name=? and student_id=? and (status=? or status=?)";

        try{
            PreparedStatement view_courses_statement = DBConnection.prepareStatement(view_courses_string);
            view_courses_statement.setString(1,this.semester);
            view_courses_statement.setString(2,this.id);
            view_courses_statement.setString(3,"enrolled");
            view_courses_statement.setString(4,"waitlisted");
            ResultSet view_courses_result = view_courses_statement.executeQuery();

            while(view_courses_result.next()) {
                System.out.println("\nCourse ID : "+ view_courses_result.getString("course_id")+"\t\tStatus : "+ view_courses_result.getString("status"));
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
    
    public void drop_my_course() {
        this.clear_screen();
        this.display_header();
        String course_id;
        System.out.print("1.Enter Course ID\t:\t");
        course_id = scan_in.next();
        
        String drop_course_string = "insert into dummy(student_id,course_id,semester_name,status,credits) values(?,?,?,?,?)";
        try{
            PreparedStatement drop_course_statement = DBConnection.prepareStatement(drop_course_string);
            drop_course_statement.setString(1,this.id);
            drop_course_statement.setString(2,course_id);
            drop_course_statement.setString(3,this.semester);

            drop_course_statement.setString(4,"drop");                     // Does not make any significnce.
            drop_course_statement.setString(5,"3");
            
            drop_course_statement.execute();            


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
    
    public void view_pending_course() {
        this.clear_screen();
        this.display_header();
        String view_courses_string = "select * from enrollment where semester_name=? and student_id=? and (status=?)";

        try{
            PreparedStatement view_courses_statement = DBConnection.prepareStatement(view_courses_string);
            view_courses_statement.setString(1,this.semester);
            view_courses_statement.setString(2,this.id);
            view_courses_statement.setString(3,"spperm");
            ResultSet view_courses_result = view_courses_statement.executeQuery();

            while(view_courses_result.next()) {
                System.out.println("\nCourse ID : "+ view_courses_result.getString("course_id"));// "\t\tCourse Name : "+ course_name_result.getString("name"));
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
    
    public void view_grades() {
        this.clear_screen();
        this.display_header();
        
        String view_grades_string = "select * from enrollment where student_id=? and (status=?)";

        try{
            PreparedStatement view_grades_statement = DBConnection.prepareStatement(view_grades_string);
            //view_grades_statement.setString(1,this.semester);
            view_grades_statement.setString(1,this.id);
            view_grades_statement.setString(2,"completed");
            ResultSet view_grades_result = view_grades_statement.executeQuery();

            while(view_grades_result.next()) {
                System.out.println("\nCourse ID : "+ view_grades_result.getString("course_id")+"\t\tLetter Grade : "+ view_grades_result.getString("grade"));
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
    
    public void view_gpa() {
        this.clear_screen();
        this.display_header();
        
        String view_gpa_string = "select gpa from student where id=?";

        try{
            PreparedStatement view_gpa_statement = DBConnection.prepareStatement(view_gpa_string);
            view_gpa_statement.setString(1,this.id);
            ResultSet view_gpa_result = view_gpa_statement.executeQuery();

            while(view_gpa_result.next()) {
                System.out.println("\nStudent ID : "+this.id +"\t\tGPA : "+ view_gpa_result.getString("gpa"));
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
    

    public void view_pay_bill() {
        this.clear_screen();
        this.display_header();
        String amount = "0";
        
        String view_bill_string = "select * from student where id=?";
        try{
            PreparedStatement view_bill_statement = DBConnection.prepareStatement(view_bill_string);
            view_bill_statement.setString(1,this.id);
            ResultSet view_bill_result = view_bill_statement.executeQuery();

            while(view_bill_result.next()) {
                System.out.println("\nStudent ID : "+this.id +"\t\tAmount Pending : "+ view_bill_result.getString("amount"));
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        System.out.println("Enter the amount (Entering 0 will go back): ");
        amount = scan_in.next();
        if(amount.equals("0")){
            return;
        }
        else{
            String pay_bill_string = "update student set amount=amount-? where id=?";
            try{
                PreparedStatement pay_bill_statement = DBConnection.prepareStatement(pay_bill_string);
                pay_bill_statement.setString(1,amount);
                pay_bill_statement.setString(2,this.id);

                pay_bill_statement.executeUpdate();
            }
            catch(Exception e){
                System.out.println(e);
            }    
        }
        return;

    }
    
    public void home() {
        int choice = 0;
        int inner_choice=10;
        while(true){
            this.clear_screen();
            this.display_header();
            System.out.println("1.View/Edit Profile");
            System.out.println("2.View Courses/Enroll/Drop Courses");
            System.out.println("3.View pending courses");
            System.out.println("4.View Grades");
            System.out.println("5.View/Pay Bill");
            System.out.println("6.Enter/Edit Replacement Course (for waitlisted courses)");
            System.out.println("7.Logout");
            
            choice = scan_in.nextInt();
            switch(choice) {
                case 1: {
                    System.out.println();
                    this.view_edit_profile();
                    break;
                }
                case 2: {
                    do {
                        System.out.println();
                        this.clear_screen();
                        this.display_header();
                        System.out.println("0. Go back to Previous Menu");
                        System.out.println("1. View Available Courses");
                        System.out.println("2. Enroll");
                        System.out.println("3. View My Courses");
                        System.out.println("4. Drop Course");
                        inner_choice = scan_in.nextInt();
                        switch(inner_choice) {
                            case 0 : {
                                break;
                            }
                            case 1 : {
                                this.view_available_courses();
                                break;
                            }
                            case 2 : {
                                this.enroll();
                                break;
                            }
                            case 3: {
                                this.view_my_course();
                                break;
                            }
                            case 4 : {
                                this.drop_my_course();
                                break;
                            }
                            default : {
                                System.out.println("Incorrect Choice.");
                            }
                        }
                    }while(inner_choice != 0);
                    break;
                }
                case 3: {
                    this.view_pending_course();
                    break;
                }
                case 4 : {
                    do{
                        System.out.println();
                        this.clear_screen();
                        this.display_header();
                        System.out.println("0. Go back to Previous Menu");
                        System.out.println("1. View Letter Grades");
                        System.out.println("2. View GPA");
                        inner_choice = scan_in.nextInt();
                        switch(inner_choice) {
                            case 0: {
                                break;
                            }
                            case 1: {
                                this.view_grades();
                                break;
                            }
                            case 2 : {
                                this.view_gpa();
                                break;
                            }
                            default : {
                                System.out.println("Incorrect Choice.");
                            }
                        }
                    }while(inner_choice != 0);
                    break;
                }
                case 5 : {
                    //do{
                        System.out.println();
                        this.clear_screen();
                        this.display_header();
                        this.view_pay_bill();
//                        System.out.println("0. Go back to Previous Menu");
//                        System.out.println("1. View Bill");
//                        System.out.println("2. Pay Bills");
//                        inner_choice = scan_in.nextInt();
//                        switch(inner_choice) {
//                            case 0: {
//                                break;
//                            }
//                            case 1: {
//                                //this.view_bill();
//                                break;
//                            }
//                            case 2 : {
//                                this.pay_bill();
//                                break;
//                            }
//                            default : {
//                                System.out.println("Incorrect Choice.");
//                            }
//                        }
//                    }while(inner_choice != 0);
                    break;
                }
                case 6: {
                    String course_id;
                    System.out.print("Enter Replacement Course ID:");
                    course_id=scan_in.next();
                    
                    String replacement_string = "update student set replacement_course_id=? where id=?";
                    
                    try{
                        PreparedStatement replacement_statement = DBConnection.prepareStatement(replacement_string);
                        replacement_statement.setString(1,course_id);
                        replacement_statement.setString(2,this.id);
                        replacement_statement.executeUpdate();
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    break;
                }
                case 7: {
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
        System.out.println("\t\tCOURSEBOOK ( ID : "+this.id+" )");
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

