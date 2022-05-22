import edu.princeton.cs.algs4.In;

import java.io.File;
import java.sql.Time;
import java.util.*;

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
            if (args.length == 0 || Objects.equals(args[0], "gui")) {
                Controller.initFromMain(qTree);
                Renderer.init();
            } else if (Objects.equals(args[0], "terminal")) {
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

                // query and print the result in the terminal
                for (int i = 0; i <= max_step; i++) {
                    if (queryStepCell.containsKey(i)) {
                        List<Integer> id_at_step = queryStepCell.get(i);
                        for (int id : id_at_step) {
                            Cell cellQueried = Cell.queryID(id);
                            String queryResult = String.format("%f %f %c\n", cellQueried.x, cellQueried.y, cellQueried.getColorChar());
                            System.out.print(queryResult);
                        }
                    }
                    qTree.moveOneStep();
                }


            } else {
                System.out.println("Invalid Arguments");
            }


        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }


        // get the input ffrom
    }
}
