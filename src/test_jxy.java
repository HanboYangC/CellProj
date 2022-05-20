import java.util.ArrayList;
import java.util.Arrays;

public class test_jxy {
    public static void main(String[] args) {
        int w = 100;
        int h = 100;
        double  r = 5;
        double  p = 10;
        Rectangle rectangle = new Rectangle(w,h);
        QTree qTree = new QTree(rectangle);
        Cell c1 = new Cell(w/4,h/4,r,p,'r');
        Cell c2 = new Cell(w/4,h/4*3,r,p,'r');
        Cell c3 = new Cell(w/4*3,h/4,r,p,'r');
        Cell c4 = new Cell(w/4*3,h/4*3,r,p,'r');
        Cell c5 = new Cell(w/2,h/2,p,r,'r');

        ArrayList<Cell> cellArrayList = new ArrayList<>(Arrays.asList(c1,c2,c3,c4,c5));
        // build tree
        for(Cell c : cellArrayList){
            qTree.insert(c);
        }
        // print tree
        for(Cell c : cellArrayList){
            System.out.print(c.ID);
            System.out.println(c.node.cells);
        }

        System.out.println("move 5");

        c5.move(w/8*3,h/8*3);

        for(Cell c : cellArrayList){
            qTree.cellShouldChange(c);
        }


        for(Cell c : cellArrayList){
            System.out.print(c.ID);
            System.out.println(c.node.brother);
        }
//        System.out.println(qTree.root.cells);



        System.out.println("finish");


    }

}
