import edu.princeton.cs.algs4.In;

import java.io.File;
import java.sql.Time;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner fin = new Scanner(System.in);
            double w = fin.nextDouble();
            double h = fin.nextDouble();
//            System.out.printf("w : %f  h : %f \n", w, h);
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
//            System.out.println(qTree.cells);

            // default
            if(args.length == 0 || Objects.equals(args[0], "gui")){
                Controller.initFromMain(qTree);
                Renderer.init();
            } else if (Objects.equals(args[0], "terminal")) {
                // print the required information in the terminal.

            } else {
                System.out.println("Invalid Arguments");
            }


        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }


        // get the input ffrom
    }
}
