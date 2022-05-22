import edu.princeton.cs.algs4.In;

import java.io.File;

public class Controller {

    public static boolean isGUI = true;
    public static double TIME_STEP = 1 / 15.0;
    public static double w;
    public static double h;
    public static int _n_cell;

    public static QTree qTree;

    public static void init_guo() {
        File file = new File("./res/data/" + 8 + "CellTest.txt");
        qTree = build_QTree_from_samplefile(file);

        Rectangle boundry = qTree.root.getBoundary();
        h = boundry.h;
        w = boundry.w;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);

        for (Cell cell : qTree.cells) {
            System.out.println(cell.node.cells.get(0).color);
        }
    }

    public static void init() {
//        In fin = new In("./res/data/" + 8 + "CellTest.txt");
        In fin = new In("./sample/sample" + 2 + ".txt");
        w = fin.readDouble();
        h = fin.readDouble();
        System.out.println(w);
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);

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

//        for (Cell cell : qTree.cells) {
//            System.out.println(cell.node.cells);
//        }

    }

    public static QTree build_QTree_from_samplefile(File file) {
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

    public static void sampletest() {
        File file = new File("./res/sample/sample2.txt");
        qTree = build_QTree_from_samplefile(file);

        Rectangle boundry = qTree.root.getBoundary();
        h = boundry.h;
        w = boundry.w;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);

    }

    public static void test_color() {
        h = 8;
        w = 8;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);
        Rectangle wall = new Rectangle(w, h);
        qTree = new QTree(wall);
        double p = 3;
        double r = 1;
        double x = 0;
        double y = 0;
        Cell cell;
        for (int i = 0; i < 4; i++) {
            y = 2 * i + 1;
            for (int j = 0; j < 2; j++) {
                x = 2 * j + 1;
                qTree.insert(new Cell(x, y, r, p, 'g'));
            }
            for (int j = 2; j < 4; j++) {
                x = 2 * j + 1;
                qTree.insert(new Cell(x, y, r, p, 'b'));
            }
        }
    }

    public static double[] gen_queryArray_from_samplefile(File queryFile) {
        In fin = new In(queryFile.getPath());
        System.out.println(fin.readDouble());
        System.out.println(fin.readDouble());
        int n_lines_to_jump = (int) fin.readDouble();
        System.out.println(n_lines_to_jump);
        for (int i = n_lines_to_jump; i >= 0; i--)
            fin.readLine();
        int query_num = fin.readInt();
        double[] queryArray = new double[query_num * 2];
        for (int i = 0; i < query_num; i++) {
            double t = fin.readDouble();
            queryArray[2 * i] = t;
            System.out.println(t);
            int ID = (int) fin.readDouble();
            System.out.println(ID);
            queryArray[2 * i + 1] = ID;
        }
        return queryArray;
    }

    public static void test_query(){
        File samplefile = new File("./res/sample/sample2.txt");
        double[] queryArray = gen_queryArray_from_samplefile(samplefile);
        qTree = build_QTree_from_samplefile(samplefile);

        Rectangle boundry = qTree.root.getBoundary();
        h = boundry.h;
        w = boundry.w;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);
    }

//    public static void querysample2test() {
//        File file = new File("./res/sample/sample2.txt");
//        qTree = build_QTree_from_file(file);
//
//        Rectangle boundry = qTree.root.getBoundary();
//        h = boundry.h;
//        w = boundry.w;
//        Renderer.winWidth = 600;
//        Renderer.winHeight = (int) (h / w * Renderer.winWidth);
//
//        File queriesfile = new File("./res/sample/sample2.txt");
//        double[] queryArray = gen_queryArray_from_file(queriesfile);
//        for (int i = 0; i < queryArray.length / 2; i++) {
//            double _t = queryArray[2 * i];
//            int _ID = (int) queryArray[2 * i + 1];
//        }
//        int idx = 0;
//
//    }
    public static void initFromMain(QTree qTreeFromMain){
        qTree = qTreeFromMain;
        Rectangle boundry = qTree.root.getBoundary();
        h = boundry.h;
        w = boundry.w;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);
    }

    public static void init_diy_guo(int cell_num_to_test){
        Test.GenBigSquare(cell_num_to_test);
        File file = new File("./res/sample/diy_sample" + cell_num_to_test + ".txt");
        qTree = build_QTree_from_samplefile(file);

        Rectangle boundry = qTree.root.getBoundary();
        h = boundry.h;
        w = boundry.w;
        Renderer.winWidth = 600;
        Renderer.winHeight = (int) (h / w * Renderer.winWidth);
    }

    public static void main(String[] args) {
//        init();
//        init_guo();
//        sampletest();
//        test_color();
//        querysample2test();
//        test_query();
        init_diy_guo(30);
        Renderer.init();
    }
}
