package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

public class SessionTimeout {
    private static final Logger logger = Logger.getLogger(SessionTimeout.class.getName());
    private static Timer timer;
    private static AWTEventListener activityListener;

    // 5 minutes = 300000 ms (1 second for testing purposes)
    private static final int TIMEOUT_DURATION = 30000;

    public static void start(JFrame currentFrame) {
        // Ensure we only run this if the user is logged in (session is active)
        if (!Session.isLoggedIn()) {
            return; // Don't start timer if no session is active
        }

        try {
            // Create a FileHandler to write logs to a file
            FileHandler fileHandler = new FileHandler("appLogs.log", true); // 'true' to append to the file
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stop(); // Stop any previous timers (important to reset)

        timer = new Timer(TIMEOUT_DURATION, e -> {
            JOptionPane.showMessageDialog(
                currentFrame,
                "Session expired due to inactivity. Please log in again.",
                "Session Timeout",
                JOptionPane.WARNING_MESSAGE
            );
            logger.warning("User " + Session.getStudentId() + " timed out");
            currentFrame.dispose();
            new ui.Login();

            // Ensure the timer is stopped after session timeout
            stop();  // Make sure to stop the timer when timeout occurs
        });

        timer.setRepeats(false);  // Timer should only run once after the timeout duration
        timer.start();

        activityListener = event -> {
            if (event instanceof MouseEvent || event instanceof KeyEvent) {
                reset(); // Reset the timer on any activity
            }
        };

        // Add listener for mouse and key events
        Toolkit.getDefaultToolkit().addAWTEventListener(
            activityListener,
            AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK
        );
    }

    public static void reset() {
        if (timer != null) {
            timer.restart(); // Restart the timer if there is any activity
        }
    }

    public static void stop() {
    if (timer != null) {
        timer.stop();  // Stop the timer when session times out
        timer = null;
    }

    if (activityListener != null) {
        Toolkit.getDefaultToolkit().removeAWTEventListener(activityListener);
        activityListener = null;
    }
}
}