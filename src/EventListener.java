import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import java.util.ArrayList;

public class EventListener implements GLEventListener {

    @Override
    public void init(GLAutoDrawable drawable) {
        //System.out.println("Hello,OpenGL");
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 1, 1, 0);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //System.out.println("Hello JOGL");
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        //Controller.timeStep();
        gl.glColor4d(0.5, 0.5, 0, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(0, 0);
        gl.glVertex2d(Controller.w, 0);
        gl.glVertex2d(Controller.w, Controller.h);
        gl.glVertex2d(0, Controller.h);
        gl.glEnd();
        for (Cell cell : Controller.qTree.cells) {
            drawSingleCircle(gl,cell.x,cell.y,cell.radius,cell.color);
        }
        Controller.qTree.moveOneStep();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        double ratio = Renderer.getWindowH() / Renderer.getWindowW();
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        double unit;
        double rulerW;
        double rulerH;
        if (Controller.w > Controller.h) {
            unit = (int) (Renderer.getWindowW() / Controller.w);
            rulerW = Renderer.getWindowW() / unit;
            rulerH = rulerW * ratio;
        } else {
            unit = (int) (Renderer.getWindowH() / Controller.h);
            rulerH = Renderer.getWindowH() / unit;
            rulerW = rulerH / ratio;
        }
        gl.glOrtho(0, rulerW, 0, rulerH, -1, 1);
    }

    public void drawSingleCircle(GL2 gl, double x, double y, double r, Cell.Color c) {

        //gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        switch (c) {
            case RED:
                gl.glColor3f(1, 0, 0);
                break;
            case GREEN:
                gl.glColor3f(0, 1, 0);
                break;
            case BLUE:
                gl.glColor3f(0, 0, 1);
                break;
            case YELLOW:
                gl.glColor3f(1, 1, 0);
                break;
        }
//        gl.glColor3f(1,0,0);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        int seg = 360 * 2;
        gl.glVertex2d(x, y);
        for (int i = 0; i < seg; i++) {
            double angle = i * Math.PI / 180;
            gl.glVertex2d(x + Math.cos(angle) * r, y + Math.sin(angle) * r);
        }
        gl.glEnd();
    }

}
