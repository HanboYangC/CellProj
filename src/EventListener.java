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
        GL2 gl=drawable.getGL().getGL2();
        gl.glClearColor(0,1,1,0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //System.out.println("Hello JOGL");
        GL2 gl=drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        Controller.timeStep();
        for(Cell cell : Cell.cells){

        }
        drawSingleCircle(gl,30,40,5,'r');
        drawSingleCircle(gl,6,6,6,'g');
        drawSingleCircle(gl,10,10,4,'b');
        drawSingleCircle(gl,20,20,8,'y');

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        double ratio=Renderer.getWindowH()/Renderer.getWindowW();
        GL2 gl=drawable.getGL().getGL2();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        double unit=1.;
        double rulerW=Renderer.getWindowW()/unit;
        double rulerH=rulerW*ratio;
        gl.glOrtho( 0,rulerW,0, rulerH,-1,1);
    }

    public void drawSingleCircle(GL2 gl,double x,double y,double r,char c){

        //gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        switch(c){
            case 'r': gl.glColor3f(1, 0, 0);break;
            case 'g': gl.glColor3f(0, 1, 0);break;
            case 'b': gl.glColor3f(0, 0, 1);break;
            case 'y': gl.glColor3f(1,1,0);break;
        }
//        gl.glColor3f(1,0,0);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
            int seg=360*2;
            gl.glVertex2d(x,y);
            for (int i = 0; i < seg; i++) {
                double angle = i * Math.PI / 180;
                gl.glVertex2d(x + Math.cos(angle) * r, y + Math.sin(angle) * r);
            }
        gl.glEnd();
    }
}
