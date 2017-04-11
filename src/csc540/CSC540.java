/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc540;

import java.sql.*;
import java.util.*;

/**
 *
 * @author mmukundram
 */
public class CSC540 {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        Session session = new Session();
        
        session.display_header();
        if(session.init_session()){
            session.start();
        }
        else{
            System.out.println("Not connected to Database.");
        }
    }
}
