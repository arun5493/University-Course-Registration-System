/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc540;

import java.io.Console;
import java.sql.*;
import java.util.*;

/**
 *
 * @author mmukundram
 */
public class Session {
    private String username;
    private String password;
    private String semester;
    private String id;                      // Student's ID
    private String type;
    private Connection DBConnection;
    private Scanner scan_in;
    
    public Session(){
        scan_in = new Scanner(System.in);
    }
    
    public String get_type(){
        return this.type;
    }
    
    public boolean init_session(){
        boolean return_value = true;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch(Exception e){
            System.out.println(e);
            return_value = false;
        }
        String url = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";
        try{
            DBConnection = (Connection)DriverManager.getConnection(url,"mmurali5","200158869");
        }
        catch(Exception e){
            System.out.println(e);
            return_value = false;
        }
        return return_value;
    }
    
    public void start(){
        int choice=3;
        String sem,year;
        while(true){
            this.clear_screen();
            this.display_header();
            //System.out.print("\nEnter Semester(fall/spring? & Year(YYYY) in the format semester+ +YYYY : ");            
            //scan_in.next();
            System.out.print("Enter Semester[fall/spring]\t:\t");
            sem = scan_in.next().toLowerCase();
            sem = sem + " ";
            System.out.print("Enter Year\t:\t");
            year = scan_in.next();
            this.semester = sem + year;
            
            System.out.println("Start Menu:");
            System.out.println("1.Login");
            System.out.println("2.Exit");
            choice = scan_in.nextInt();
            
            switch(choice){
                case 1:{
                    if(login()){
                        this.clear_screen();
                        this.display_header();
                        System.out.println("Login Successful.");
                       
                        if(this.type.equals("admin")){
                            System.out.println("Current Semester : "+this.semester);
                            Admin admin = new Admin(username,semester,DBConnection);
                            admin.home();
                        }
                        else{                   
                            System.out.println("Student Name : \t"+this.username);
                            System.out.println("Semester : \t"+this.semester);
                            Student student = new Student(username,semester,DBConnection);
                            student.home();                            
                        }
                    }
                    else{
                        System.out.println("Login Incorrect.");
                    }
                    break;
                }
                case 2:{
                    try{
                        DBConnection.close();
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    return;
                }
                default:{
                    System.out.println("Incorrect choice.");
                }
            }
        }
    }
    
    public boolean login(){
        //char[] pass = new char[50];
        boolean return_value = false;
        System.out.print("Username : ");
        username = scan_in.next();
        System.out.print("Password : ");
        password = scan_in.next();
//        Console console = System.console();
//        if (console == null) {
//            System.out.println("Couldn't get Console instance");
//            System.exit(0);
//        }
//        char[] pass_temp = console.readPassword("");
//        password = new String(pass_temp);
        String login_string = "select * from userbase where username=? and password=?";
        
        try{
            PreparedStatement login_statement = DBConnection.prepareStatement(login_string);
            login_statement.setString(1,username);
            login_statement.setString(2,password);
            
            ResultSet login_result = login_statement.executeQuery();
            
            if(login_result.next())
            {
                this.type = login_result.getString(3);                      
                return_value = true;
            }
                
        }
        catch(Exception e){
            System.out.println(e);
            return_value = false;
        }
        return return_value;
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
