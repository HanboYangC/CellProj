import java.io.*;
import java.util.*;

import edu.princeton.cs.algs4.*;

public class Test {
    // Basic Parameters
    static double width = 40;
    static double height = 50;

    // test Basic Initialization
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

    // test color change
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

    // test movements
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

    // num_sample : 1/2/3
    public static void query_test(int num_sample) throws FileNotFoundException {
        File sample_file = new File("./res/sample/sample" + num_sample + ".txt");
        try{
            Scanner fin = new Scanner(sample_file);
            // build tree
            double w = fin.nextDouble();
            double h = fin.nextDouble();
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
            HashMap<Integer, List<Integer>> queryStepCell = new HashMap<>();
            int max_step = 0;

            for (int j = 0; j < _n_query; j++) {
                double _t = fin.nextDouble();
                int _ID = fin.nextInt();
//            System.out.printf("t : %f  ID : %d \n", _t, _ID);
                int _step = (int) Math.floor(_t * 15);

                if (_step > max_step) {
                    max_step = _step;
                }
                if (queryStepCell.containsKey(_step)) {
                    queryStepCell.get(_step).add(_ID);
                } else {
                    List<Integer> id_at_step = new ArrayList<>();
                    id_at_step.add(_ID);
                    queryStepCell.put(_step, id_at_step);
                }

            }
            // do query
            // print the result in the terminal
            // save the result in file 'test_sample1.txt'
            try {
                String outPath = "./res/output/test_sample" + num_sample + ".txt";
                File outFile = new File(outPath);
                outFile.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

                for (int i = 0; i <= max_step; i++) {
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
                System.out.println("-- results of query are in :" + outPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // do the compare
            CompareResult(num_sample, _n_query);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void CompareResult(int fileNum, int numOfQuery) {
        In fin1 = new In("./res/sample/sample" + fileNum + "_out.txt"); // the correct answer
        In fin2 = new In("./res/output/test_sample" + fileNum + ".txt"); // our output
        double x1, x2, y1, y2;
        char c1, c2;
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
                return;
            }
        }
        System.out.println("all correct!");

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

    public static void GenBigSquare(int cell_num_to_test) {
        Out samplefile = new Out("./res/sample/diy_sample" + cell_num_to_test + ".txt");

        // Generate Basic Info of a QTree
        double a = Math.ceil(Math.sqrt(cell_num_to_test) + 0.5);  // square's height or width
        //if (a < 10) a = 10;
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
            x_seq[i] = max_radius + i % a;
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

    public static void testTime(int cell_num_to_test,int steps){
        Test.GenBigSquare(cell_num_to_test);
        File file = new File("./res/sample/diy_sample" + cell_num_to_test + ".txt");
        QTree qTree = build_QTree_from_samplefile(file);
        Stopwatch stopwatch=new Stopwatch();
        for (int i = 0; i < steps; i++) {
            qTree.moveOneStep();
        }
        System.out.println(stopwatch.elapsedTime());
    }

    public static void main(String[] args) throws FileNotFoundException {
        // compare the query result with given sample
        System.out.println("--------------- test sample 1 ---------------");
        query_test(1);
        System.out.println("--------------- test sample 2 ---------------");
        query_test(2);
        System.out.println("--------------- test sample 3 ---------------");
        query_test(3);

        // test time complicity
        for (int i = 100; i < 10000; i+=100) {
            testTime(i,20);
        }
    }
}
