/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Acer
 */
public class Session {
    private static String role;
    private static String studentId;
    private static String studentName;
    private static String studentGender;

    public static void startSession(String role, String studentId, String studentName, String studentGender) {
        Session.role = role;
        Session.studentId = studentId;
        Session.studentName = studentName;
        Session.studentGender = studentGender;
    }

    public static void clearSession() {
        role = null;
        studentId = null;
        studentName = null;
        studentGender = null;
    }

    public static String getRole() {
        return role;
    }

    public static String getStudentId() {
        return studentId;
    }

    public static String getStudentName() {
        return studentName;
    }

    public static String getStudentGender() {
        return studentGender;
    }

    public static boolean isLoggedIn() {
        return role != null;
    }

    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public static boolean isStudent() {
        return "student".equalsIgnoreCase(role);
    }
}
