package demos.common;

import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * @author Pepijn Van Eeckhoudt
 */
public class GLDisplay {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static final int DONT_CARE = -1;

    private JFrame frame;
    private GLCanvas glCanvas;
    private FPSAnimator animator;
    private boolean fullscreen;
    private int width;
    private int height;
    private GraphicsDevice usedDevice;

    private MyHelpOverlayGLEventListener helpOverlayGLEventListener = new MyHelpOverlayGLEventListener();
    private MyExceptionHandler exceptionHandler = new MyExceptionHandler();

    public static GLDisplay createGLDisplay(String title) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        boolean fullscreen = false;
//        if ( device.isFullScreenSupported() ) {
//            int selectedOption = JOptionPane.showOptionDialog(
//                    null,
//                    "How would you like to run this demo?",
//                    title,
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null,
//                    new Object[]{"Fullscreen", "Windowed"},
//                    "Windowed" );
//            fullscreen = selectedOption == 0;
//        }
        return new GLDisplay(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, fullscreen);
    }

    private GLDisplay(String title, int width, int height, boolean fullscreen) {
        glCanvas = new GLCanvas(new GLCapabilities());
        glCanvas.setSize(width, height);
        glCanvas.setIgnoreRepaint(true);
        glCanvas.addGLEventListener(helpOverlayGLEventListener);

        frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(glCanvas, BorderLayout.CENTER);

        addKeyListener(new MyKeyAdapter());

        this.fullscreen = fullscreen;
        this.width = width;
        this.height = height;

        animator = new FPSAnimator(glCanvas, 60);
        animator.setRunAsFastAsPossible(false);
    }

    public void start() {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setUndecorated(fullscreen);

            frame.addWindowListener(new MyWindowAdapter());

            if (fullscreen) {
                usedDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                usedDevice.setFullScreenWindow(frame);
                usedDevice.setDisplayMode(
                        findDisplayMode(
                                usedDevice.getDisplayModes(),
                                width, height,
                                usedDevice.getDisplayMode().getBitDepth(),
                                usedDevice.getDisplayMode().getRefreshRate()
                        )
                );
            } else {
                frame.setSize(frame.getContentPane().getPreferredSize());
                frame.setLocation(
                        (screenSize.width - frame.getWidth()) / 2,
                        (screenSize.height - frame.getHeight()) / 2
                );
                frame.setVisible(true);
            }

            glCanvas.requestFocus();

            animator.start();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    public void stop() {
        try {
            animator.stop();
            if (fullscreen) {
                usedDevice.setFullScreenWindow(null);
                usedDevice = null;
            }
            frame.dispose();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        } finally {
            System.exit(0);
        }
    }

    private DisplayMode findDisplayMode(DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate) {
        // Try to find an exact match
        DisplayMode displayMode = findDisplayModeInternal(displayModes, requestedWidth, requestedHeight, requestedDepth, requestedRefreshRate);

        // Try again, ignoring the requested bit depth
        if (displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, requestedWidth, requestedHeight, DONT_CARE, DONT_CARE);

        // Try again, and again ignoring the requested bit depth and height
        if (displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, requestedWidth, DONT_CARE, DONT_CARE, DONT_CARE);

        // If all else fails try to get any display mode
        if (displayMode == null)
            displayMode = findDisplayModeInternal(displayModes, DONT_CARE, DONT_CARE, DONT_CARE, DONT_CARE);

        return displayMode;
    }

    private DisplayMode findDisplayModeInternal(DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate) {
        DisplayMode displayModeToUse = null;
        for (int i = 0; i < displayModes.length; i++) {
            DisplayMode displayMode = displayModes[i];
            if ((requestedWidth == DONT_CARE || displayMode.getWidth() == requestedWidth) &&
                    (requestedHeight == DONT_CARE || displayMode.getHeight() == requestedHeight) &&
                    (requestedHeight == DONT_CARE || displayMode.getRefreshRate() == requestedRefreshRate) &&
                    (requestedDepth == DONT_CARE || displayMode.getBitDepth() == requestedDepth))
                displayModeToUse = displayMode;
        }

        return displayModeToUse;
    }

    public void addGLEventListener(GLEventListener glEventListener) {
        this.helpOverlayGLEventListener.addGLEventListener(glEventListener);
    }

    public void removeGLEventListener(GLEventListener glEventListener) {
        this.helpOverlayGLEventListener.removeGLEventListener(glEventListener);
    }

    public void addKeyListener(KeyListener l) {
        glCanvas.addKeyListener(l);
    }

    public void addMouseListener(MouseListener l) {
        glCanvas.addMouseListener(l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        glCanvas.addMouseMotionListener(l);
    }

    public void removeKeyListener(KeyListener l) {
        glCanvas.removeKeyListener(l);
    }

    public void removeMouseListener(MouseListener l) {
        glCanvas.removeMouseListener(l);
    }

    public void removeMouseMotionListener(MouseMotionListener l) {
        glCanvas.removeMouseMotionListener(l);
    }

    public void registerKeyStrokeForHelp(KeyStroke keyStroke, String description) {
        helpOverlayGLEventListener.registerKeyStroke(keyStroke, description);
    }

    public void registerMouseEventForHelp(int id, int modifiers, String description) {
        helpOverlayGLEventListener.registerMouseEvent(id, modifiers, description);
    }

    public String getTitle() {
        return frame.getTitle();
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }


    private class MyKeyAdapter extends KeyAdapter {
        public MyKeyAdapter() {
            registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "Show/hide help message");
            registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Quit demo");
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    stop();
                    break;
                case KeyEvent.VK_F1:
                    helpOverlayGLEventListener.toggleHelp();
                    break;
            }
        }
    }

    private class MyWindowAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            stop();
        }
    }

    private class MyExceptionHandler implements ExceptionHandler {
        public void handleException(final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    JOptionPane.showMessageDialog(frame, stringWriter.toString(), "Exception occurred", JOptionPane.ERROR_MESSAGE);
                    stop();
                }
            });
        }
    }

    private static class MyHelpOverlayGLEventListener implements GLEventListener {
        private java.util.List eventListeners = new ArrayList();
        private HelpOverlay helpOverlay = new HelpOverlay();
        private boolean showHelp = false;

        public void toggleHelp() {
            showHelp = !showHelp;
        }

        public void registerKeyStroke(KeyStroke keyStroke, String description) {
            helpOverlay.registerKeyStroke(keyStroke, description);
        }

        public void registerMouseEvent(int id, int modifiers, String description) {
            helpOverlay.registerMouseEvent(id, modifiers, description);
        }

        public void addGLEventListener(GLEventListener glEventListener) {
            eventListeners.add(glEventListener);
        }

        public void removeGLEventListener(GLEventListener glEventListener) {
            eventListeners.remove(glEventListener);
        }

        public void display(GLAutoDrawable glDrawable) {
            for (int i = 0; i < eventListeners.size(); i++) {
                ((GLEventListener) eventListeners.get(i)).display(glDrawable);
            }
            if (showHelp)
                helpOverlay.display(glDrawable);
        }

        public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
            for (int i = 0; i < eventListeners.size(); i++) {
                ((GLEventListener) eventListeners.get(i)).displayChanged(glDrawable, b, b1);
            }
        }

        public void init(GLAutoDrawable glDrawable) {
            for (int i = 0; i < eventListeners.size(); i++) {
                ((GLEventListener) eventListeners.get(i)).init(glDrawable);
            }
        }

        public void reshape(GLAutoDrawable glDrawable, int i0, int i1, int i2, int i3) {
            for (int i = 0; i < eventListeners.size(); i++) {
                ((GLEventListener) eventListeners.get(i)).reshape(glDrawable, i0, i1, i2, i3);
            }
        }
    }
}
