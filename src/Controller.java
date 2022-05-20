import edu.princeton.cs.algs4.In;

public class Controller {

    public static boolean isGUI = true;
    public static double TIME_STEP = 1 / 15;
    public static double w;
    public static double h;
    public static int _n_cell;

    public static QTree qTree;

    public static void init() {
        In fin = new In("./res/sample2.txt");
        w = fin.readDouble();
        h = fin.readDouble();
        Renderer.winWidth=600;
        Renderer.winHeight= (int) (h/w*Renderer.winWidth);

        Rectangle wall = new Rectangle(w, h);
        qTree = new QTree(wall);
        _n_cell = fin.readInt();
        for (int j = 0; j < _n_cell; j++) {
            double _x = fin.readDouble();
            double _y = fin.readDouble();
            double _r = fin.readDouble();
            double _pr = fin.readDouble();
            fin.readChar();  // Trim the space
            char _color = fin.readChar();
            qTree.insert(new Cell(_x, _y, _r, _pr, _color));
        }

        /*for (Cell cell : qTree.cells) {
            System.out.println(cell.node.cells);
        }*/

    }

    public static void called(){
        qTree.moveOneStep();
    }

    public static void main(String[] args) {
        init();
        Renderer.init();


    }


}
