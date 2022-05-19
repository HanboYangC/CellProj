import edu.princeton.cs.algs4.In;

public class Controller {

    public static boolean isGUI = true;
    public static double TIME_STEP = 1 / 15;
    public static double w;
    public static double h;
    public static int _n_cell;

    public static QTree qTree = new QTree(new Rectangle(w / 2, h / 2, w, h));

    public static void init() {
        In fin = new In("./res/data/" + 8 + "CellTest.txt");
        w= fin.readDouble();
        h = fin.readDouble();
        Rectangle wall = new Rectangle(w, h);
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

        for(Cell cell : qTree.cells){
            System.out.println(cell.node);
        }

//        for (int i = 0; i < 1500; i++) {
////            qTree.moveOneStep();
////            qTree.cellShouldChange();
//        }
    }

    public static void main(String[] args) {
        init();

    }


}
