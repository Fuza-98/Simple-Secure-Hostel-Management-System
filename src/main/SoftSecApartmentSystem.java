/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import ui.Login;
import java.sql.Connection;
import db.DBConnection;
/**
 *
 * @author Acer
 */
public class SoftSecApartmentSystem {
    /*public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/ //Test database connection
    
     
    public static void main(String[] args) {
        new Login();
    } 
    
    
    
}
