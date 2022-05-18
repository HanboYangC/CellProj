import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.princeton.cs.algs4.*;

public class Test {
    // Basic Parameters
    static double width = 40;
    static double height = 50;

    // Basic Initialization
    public static void genData() {
        new File("./res/data/");

        for (int i = 1; i <= 8; ++i) {
            try (PrintWriter fout = new PrintWriter("./res/data/" + i + "CellTest.txt")) {
                // Generate Data of Wall
                fout.println(width + " " + height);

                double max_radius = width / (i * 2.0);
                if (max_radius > height / (i * 2.0)) max_radius = height / (i * 2.0);

                // Generate Data of iCellTest.txt files
                // Setup
                double[] x_seq = new double[i];
                double[] y_seq = new double[i];
                double[] r_seq = new double[i];
                double[] pr_seq = new double[i];
                char[] c_seq = new char[i];
                // Query i times for i cells
                int[] t_seq = new int[i];
                int[] n_seq = new int[i];
                for (int j = 0; j < i; j++) {
                    // Generate Data of Cells
                    // Diagonal start-up
                    x_seq[j] = width / (i * 2.0) + width / i * j;
                    y_seq[j] = height / (i * 2.0) + height / i * j;
                    r_seq[j] = StdRandom.uniform(0.01, max_radius);
                    pr_seq[j] = StdRandom.uniform(r_seq[j], max_radius);
                    String str = "rgby";
                    c_seq[j] = str.charAt(StdRandom.uniform(4));
                    // Generate Data of Queries
                    t_seq[j] = StdRandom.uniform(10);  // random int less than 10
                    n_seq[j] = StdRandom.uniform(i);  // random int less than i
                }

                // Write Data into the test file
                fout.println(i);
                for (int j = 0; j < i; j++) {
                    fout.println(x_seq[j] + " " + y_seq[j] + " " + r_seq[j] + " " + pr_seq[j] + " " + c_seq[j] + " ");

                }
                fout.println(i);
                for (int j = 0; j < i; j++) {
                    fout.println(t_seq[j] + " " + n_seq[j]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void genScenery() {

        // Read Params from txt
        for (int i = 1; i <= 8; ++i) {
            try {
                // 你好！我是一段测试代码，可以把我放在任何地方来测试哦~
                // StdOut.println(_AnyVariable);   // Verify the reading program
                // 你好！我是一段测试代码，可以把我放在任何地方来测试哦~

                In fin = new In("./res/data/" + i + "CellTest.txt");

                double _w = fin.readDouble();
                double _h = fin.readDouble();

                /*
                    Generate Your Wall Here
                */
                Rectangle wall = new Rectangle(_w, _h);
                /*
                    Generate Your Wall Here
                */

                int _n_cell = fin.readInt();

                for (int j = 0; j < _n_cell; j++) {
                    double _x = fin.readDouble();
                    double _y = fin.readDouble();
                    double _r = fin.readDouble();
                    double _pr = fin.readDouble();
                    fin.readChar();  // Trim the space
                    char _color = fin.readChar();
                    /*
                    Generate Your Cell Here, One by One
                */
                    new Cell(_x, _y, _r, _pr, _color);
                /*
                    Generate Your Wall Here, One by One
                */
                }

                int _n_query = fin.readInt();  // int _n_query = (int) arr_double[7];

                for (int j = 0; j < _n_query; j++) {
                    int _t = fin.readInt();
                    int _ID = fin.readInt();

                    /*
                    Query Your Cell Here, One by One
                    */
                    Cell _cell = Cell.queryID(_ID);
                    // StdOut.println(_cell);
                    /*
                    Query Your Cell Here, One by One
                    */
                }

                fin.close();

                StdOut.printf("Testing in %dCellTest.txt:", i);
                Stopwatch timer = new Stopwatch();

                /*  Write the test code here */

                // Perception Test

                // Wall Test

                // Color Change Test

                // Overlap Test

                // Complex Test

                /*  Write the test code here */

                StdOut.printf(" number of cells: %d, time spent: %f seconds.\n", i, timer.elapsedTime());

                // 这个清空Cell类静态变量的指令非常重要，可以防止前一轮测试的对象跑到后一轮去
                Cell.release();
                // This Command is intended to release all the static class variables so that the mistake can be prevented.

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void simpletest(){
        QuadTree qt = new QuadTree(new Rectangle(4, 4));
        double[] x_seq = new double[]{1, 2, 3, 1, 3};
        double[] y_seq = new double[]{3, 2, 3, 1, 1};
        double r = 0.1;
        double pr = 0.2;
        char c = 'r';
        for (int i=0;i<5;i++)
            qt.insert(new Cell(x_seq[i], y_seq[i], r, pr, c));
        ArrayList<Cell> cells = qt.dfs(qt);
        for (Cell tmp: cells)
            System.out.println(Arrays.toString(tmp.position));
        System.out.println("Notice: the correct output is [1, 3][2, 2][3, 3][1, 1][3, 1]");
    }

    public static void main(String[] args) {
        simpletest();
    }
}
