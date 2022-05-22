import java.io.*;
import java.util.*;

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

                QTree qTree = new QTree(wall);
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
                    qTree.insert(new Cell(_x, _y, _r, _pr, _color));
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
                    StdOut.println(_cell.x + ',' + _cell.y);
                    /*
                    Query Your Cell Here, One by One
                    */
                }

                fin.close();

                StdOut.printf("Testing in %dCellTest.txt:", i);
                Stopwatch timer = new Stopwatch();

                /*  Write the test code here */


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

    public static void simpletest() {
        QTree qt = new QTree(new Rectangle(4, 4));
        double[] x_seq = new double[]{1, 2, 3, 1, 3};
        double[] y_seq = new double[]{3, 2, 3, 1, 1};
        double r = 0.1;
        double pr = 0.2;
        char c = 'r';
        for (int i = 0; i < 5; i++)
            qt.insert(new Cell(x_seq[i], y_seq[i], r, pr, c));

        System.out.println("------------Simple Test-------------");
        qt.simple_test_output();
        System.out.println("Notice: the correct output is [1, 3][2, 2][3, 3][1, 1][3, 1]");
        System.out.println("--------Simple Test Finished--------");
    }

    public static void colortest() {
        QTree qt = new QTree(new Rectangle(4, 4));
        double[] x_seq = new double[]{1, 2, 3, 1, 3, 1.5, 2.5, 3.5, 1.5, 3.5};
        double[] y_seq = new double[]{3, 2, 3, 1, 1, 3.5, 2.5, 3.5, 1.5, 1.5};
        double r = 0.1;
        double pr = 1.6;
        char c = 'r';
        for (int i = 0; i < 10; i++)
            qt.insert(new Cell(x_seq[i], y_seq[i], r, pr, c));
        System.out.println("------------Color Test-------------");
        qt.color_test_output();
        System.out.println("Notice: the correct second output is all red but one yellow]");
        System.out.println("--------Color Test Finished--------");
    }

    public static void movetest() {
        QTree qTree = new QTree(new Rectangle(0, 0, 30, 30));
        qTree.insert(new Cell(0, 0, 1, 5, 'r'));
        qTree.insert(new Cell(0, 2, 1, 5, 'b'));
        qTree.insert(new Cell(-2, 2, 1, 5, 'b'));
        qTree.insert(new Cell(-2, 0, 1, 5, 'b'));
        for (int i = 1; i <= 5; i++) {
            System.out.printf("-------------Round %d-------------\n", i);
            qTree.move_test_output();
        }
    }

    public static void query_test() {
//        System.out.println("in test");
        Scanner fin = new Scanner(System.in);
        // build tree
        double w = fin.nextDouble();
        double h = fin.nextDouble();
//        System.out.printf("w : %f  h : %f \n", w, h);
        Rectangle wall = new Rectangle(w, h);
        QTree qTree = new QTree(wall);

        int _n_cell = fin.nextInt();
        for (int j = 0; j < _n_cell; j++) {
            double _x = fin.nextDouble();
            double _y = fin.nextDouble();
            double _r = fin.nextDouble();
            double _pr = fin.nextDouble();
            char _color = fin.next().charAt(0);
//                System.out.printf("%f, %f, %f, %f, %s \n",_x, _y, _r, _pr, _color);
            qTree.insert(new Cell(_x, _y, _r, _pr, _color));
        }

        // get query request
        int _n_query = fin.nextInt();
//        HashMap<Double, List<Integer>> queryTimeCell = new HashMap<>();
        HashMap<Integer, List<Integer>> queryStepCell = new HashMap<>();
        int max_step = 0;

        for (int j = 0; j < _n_query; j++) {
            double _t = fin.nextDouble();
            int _ID = fin.nextInt();
//            System.out.printf("t : %f  ID : %d \n", _t, _ID);
            int _step = (int) Math.floor(_t * 15); // TODO : find the problem

            if (_step > max_step) {
                max_step = _step;
            }
//            if(queryTimeCell.containsKey(_t)){
//                queryTimeCell.get(_t).add(_ID);
//            }else{
//                List<Integer> id_at_t = new ArrayList<>();
//                id_at_t.add(_ID);
//                queryTimeCell.put(_t,id_at_t);
//            }
            if (queryStepCell.containsKey(_step)) {
                queryStepCell.get(_step).add(_ID);
            } else {
                List<Integer> id_at_step = new ArrayList<>();
                id_at_step.add(_ID);
                queryStepCell.put(_step, id_at_step);
            }

        }
//        System.out.println(max_step);


        // do query
        // print the result in the terminal
        // save the result in file 'test_sample1.txt'
        int fileNum = 2;

        try {
            String outPath = "./res/output/test_sample" + fileNum + ".txt";
            File outFile = new File(outPath);
            outFile.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

            for (int i = 0; i <= max_step; i++) {
//                qTree.moveOneStep();
                if (queryStepCell.containsKey(i)) {
                    List<Integer> id_at_step = queryStepCell.get(i);
                    for (int id : id_at_step) {
                        Cell cellQueried = Cell.queryID(id);
                        String queryResult = String.format("%f %f %c\n", cellQueried.x, cellQueried.y, cellQueried.getColorChar());
                        out.write(queryResult);
                        out.flush();
//                        System.out.print(queryResult);
//                        System.out.printf("[ID : %s] x : %f  y : %f  color : %s \n",cellQueried.ID, cellQueried.x, cellQueried.y,cellQueried.color);
                    }
                }
                qTree.moveOneStep();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // do the compare
        CompareResult(fileNum, _n_query, 0.001);

    }

    public static void query_test_guo(int fileNum) {
        Out fout = new Out("./res/output/test_sample" + fileNum + "_out.txt");

        File samplefile = new File("./res/sample/sample" + fileNum + ".txt");
        QTree qTree = build_QTree_from_samplefile(samplefile);
        double[] queryArray = gen_queryArray_from_samplefile(samplefile);

        for (int i = 0; i < queryArray.length / 2; i++) {
            if (Math.abs(qTree.time - queryArray[2 * i]) < 0.03) {
                Cell output_cell = Cell.queryID((int) queryArray[2 * i + 1]);
                fout.println(output_cell.standard_output());
            } else {
                qTree.moveOneStep();
                Cell output_cell = Cell.queryID((int) queryArray[2 * i + 1]);
                fout.println(output_cell.standard_output());
            }
        }

        File out = new File("./res/output/test_sample" + fileNum + "_out.txt");
        File ref = new File("./res/sample/sample" + fileNum + "_out.txt");
        compare_out_file(out, ref);
    }

    public static void compare_out_file(File out, File ref) {  // output and the ref correct answers
        In fin_out = new In(out.getPath());
        In fin_ref = new In(ref.getPath());

        double _x_out;
        double _x_ref;
        double _y_out;
        double _y_ref;
        char _color_out;
        char _color_ref;

        int query_num = 0;  // query line num, not the cell ID
        while (!fin_out.isEmpty()) {
            _x_out = fin_out.readDouble();
            _y_out = fin_out.readDouble();
            _x_ref = fin_ref.readDouble();
            _y_ref = fin_ref.readDouble();
            fin_out.readChar();  // Trim the space
            fin_ref.readChar();  // Trim the space
            _color_out = fin_out.readChar();
            _color_ref = fin_ref.readChar();
            if (Math.abs(_x_out + _y_out - _x_ref - _y_ref) > 0.01 || _color_out != _color_ref) {
                System.out.printf("Cell ID QueryArray[%d] Error: out: %f %f %c ref: %f %f %c\n", query_num * 2 + 1, _x_out, _y_out, _color_out, _x_ref, _y_ref, _color_ref);
            }
            query_num++;
        }
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

    public static double[] gen_queryArray_from_samplefile(File queryFile) {
        In fin = new In(queryFile.getPath());
        fin.readDouble();
        fin.readDouble();
//        System.out.println(fin.readDouble());
//        System.out.println(fin.readDouble());
        int n_lines_to_jump = (int) fin.readDouble();
//        System.out.println(n_lines_to_jump);
        for (int i = n_lines_to_jump; i >= 0; i--)
            fin.readLine();
        int query_num = fin.readInt();
        double[] queryArray = new double[query_num * 2];
        for (int i = 0; i < query_num; i++) {
            double t = fin.readDouble();
            queryArray[2 * i] = t;
//            System.out.println(t);
            int ID = (int) fin.readDouble();
//            System.out.println(ID);
            queryArray[2 * i + 1] = ID;
        }
        return queryArray;
    }

    public static void CompareResult(int fileNum, int numOfQuery, double epsilon) {
        In fin1 = new In("./res/sample/sample" + fileNum + "_out.txt"); // the correct answer
        In fin2 = new In("./res/output/test_sample" + fileNum + "_out.txt"); // our output
//        int numOfQuery = 3;
//        double delta = 0.001;
        double x1, x2, y1, y2;
        char c1, c2;
//        while (fin1.hasNextLine()){
        for (int i = 0; i < numOfQuery; i++) {
            // the answer
            x1 = fin1.readDouble();
            y1 = fin1.readDouble();
            fin1.readChar();
            c1 = fin1.readChar();

            // the result of query
            x2 = fin2.readDouble();
            y2 = fin2.readDouble();
            fin2.readChar();
            c2 = fin2.readChar();

            if (Math.abs(x1 - x2) > x1 * (0.05) || Math.abs(y1 - y2) > y1 * (0.05) || c1 != c2) {
                System.out.println("something is wrong!");
                System.out.printf("[line %d] x : %f  y : %f  color : %s || x : %f  y : %f  color : %s \n", i, x1, y1, c1, x2, y2, c2);
//                break;
                return;
            }
        }
        System.out.println("all correct!");

    }

    public static void GenBigSquare(int cell_num_to_test) {
        Out samplefile = new Out("./res/sample/diy_sample" + cell_num_to_test + ".txt");

        // Generate Basic Info of a QTree
        double a = Math.ceil(Math.sqrt(cell_num_to_test) + 0.5);  // square's height or width
        if (a < 10) a = 10;
        double max_radius = 0.5;
        double max_pr = 2.0 * max_radius;
        String str = "rgby";

        samplefile.println(a + " " + a);

        // Generate Data of iCellTest.txt files
        // Setup
        double[] x_seq = new double[cell_num_to_test];
        double[] y_seq = new double[cell_num_to_test];
        double[] r_seq = new double[cell_num_to_test];
        double[] pr_seq = new double[cell_num_to_test];
        char[] c_seq = new char[cell_num_to_test];
        // Query i times for i cells
        double[] t_seq = new double[cell_num_to_test];
        int[] n_seq = new int[cell_num_to_test];

        for (int i = 0; i < cell_num_to_test; i++) {
            // Generate Data of Cells
            x_seq[i] = max_radius + (2 * i + 1) % a;
            y_seq[i] = 2 * max_radius * Math.floor(i / a) + max_radius;
            r_seq[i] = StdRandom.uniform(0.01, max_radius);
            pr_seq[i] = StdRandom.uniform(r_seq[i], max_pr);
            c_seq[i] = str.charAt(StdRandom.uniform(4));
        }

        // Generate Data of Queries
        for (int i = 0; i < cell_num_to_test; i++) {
            t_seq[i] = i * 1.0 / 15.0 + StdRandom.uniform(0, 1.0 / 15.0);
            n_seq[i] = StdRandom.uniform(cell_num_to_test);
        }

        // Write rest Data into the test file
        samplefile.println(cell_num_to_test);  // num of cells
        for (int i = 0; i < cell_num_to_test; i++) {
            samplefile.println(x_seq[i] + " " + y_seq[i] + " " + r_seq[i] + " " + pr_seq[i] + " " + c_seq[i] + " ");
        }
        samplefile.println(cell_num_to_test);  // num of queries
        for (int i = 0; i < cell_num_to_test; i++) {
            samplefile.println(t_seq[i] + " " + n_seq[i]);
        }
    }

    public static void main(String[] args) {
//        CompareResult(1,3,0.001);
//        query_test();
        GenBigSquare(2000);
        query_test_guo(2);
//        simpletest();
//        genData();
//        genScenery();
//        colortest();
//        movetest();
    }
}
