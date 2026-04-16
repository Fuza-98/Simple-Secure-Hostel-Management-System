/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SessionTimeout {
    private static Timer timer;
    private static AWTEventListener activityListener;

    // 5 minutes = 300000 ms
    private static final int TIMEOUT_DURATION = 300000;

    public static void start(JFrame currentFrame) {
        stop();

        timer = new Timer(TIMEOUT_DURATION, e -> {
            JOptionPane.showMessageDialog(
                currentFrame,
                "Session expired due to inactivity. Please log in again.",
                "Session Timeout",
                JOptionPane.WARNING_MESSAGE
            );
            currentFrame.dispose();
            new ui.Login();
        });

        timer.setRepeats(false);
        timer.start();

        activityListener = event -> {
            if (event instanceof MouseEvent || event instanceof KeyEvent) {
                reset();
            }
        };

        Toolkit.getDefaultToolkit().addAWTEventListener(
            activityListener,
            AWTEvent.MOUSE_EVENT_MASK |
            AWTEvent.KEY_EVENT_MASK
        );
    }

    public static void reset() {
        if (timer != null) {
            timer.restart();
        }
    }

    public static void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        if (activityListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(activityListener);
            activityListener = null;
        }
    }
}
