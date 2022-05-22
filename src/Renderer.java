import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {
    public static GLWindow window;
    public static int winWidth=600;
    public static int winHeight=600;
    //public static int unit= (int) (winWidth/Controller.w);
    public static void init(){
        //GLProfile.initSingleton();
        GLProfile profile=GLProfile.get(GLProfile.GL2);
        GLCapabilities caps=new GLCapabilities(profile);

        window=GLWindow.create(caps);
        window.setSize(winWidth,winHeight);
        //window.setResizable(false);
        window.addGLEventListener(new EventListener());
        FPSAnimator animator=new FPSAnimator(window,15);
        animator.start();
        window.setVisible(true);

    }

    public static double getWindowH() {
        return window.getHeight();
    }

    public static double getWindowW() {
        return window.getWidth();
    }

    /*public static void main(String[] args) {
        init();
    }*/
}
