import edu.princeton.cs.algs4.In;

import java.io.File;

public class Controller {

    public static boolean isGUI = true;
    public static double TIME_STEP = 1 / 15.0;
    public static double w;
    public static double h;
    public static int _n_cell;

    public static QTree qTree;

//    public static void init() {
//        File file = new File("./res/data/" + 8 + "CellTest.txt");
//        QTree qTree = build_QTree_from_file(file);
//
//        Rectangle boundry = qTree.root.getBoundary();
//        Renderer.winWidth = 600;
//        Renderer.winHeight = (int) (boundry.h / boundry.w * Renderer.winWidth);
//
//        System.out.println(qTree.root.getBoundary().w);
//    }

    public static void init() {
        In fin = new In("./res/data/" + 8 + "CellTest.txt");
        w = fin.readDouble();
        h = fin.readDouble();
        Renderer.winWidth=600;
        Renderer.winHeight= (int) (h/w*Renderer.winWidth);

//        File file = new File("./res/data/" + 8 + "CellTest.txt");
//        QTree qTree = build_QTree_from_file(file);
//        Rectangle boundry = qTree.root.getBoundary();
//        double w = boundry.w;
//        double h = boundry.h;
//        Renderer.winWidth=600;
//        Renderer.winHeight= (int) (h/w*Renderer.winWidth);

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

    public static QTree build_QTree_from_file(File file) {
        In fin = new In(file.getPath());
        double w = fin.readDouble();
        double h = fin.readDouble();
        Rectangle wall = new Rectangle(w, h);
        QTree qTree = new QTree(wall);

        int _n_cell = fin.readInt();
        for (int j = 0; j < _n_cell; j++) {
            double _x = fin.readDouble();
            double _y = fin.readDouble();
            double _r = fin.readDouble();
            double _pr = fin.readDouble();
            fin.readChar();  // Trim the space
            char _color = fin.readChar();
            qTree.insert(new Cell(_x, _y, _r, _pr, _color));
        }
        return qTree;
    }

    public static void called() {
        qTree.moveOneStep();
    }

    public static void main(String[] args) {
        init();
        Renderer.init();
    }
}
