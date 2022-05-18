public class Rectangle {
    public double x;
    public double y;
    public double w;
    public double h;
    public double north;
    public double south;
    public double east;
    public double west;

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.north=y+h/2;
        this.south=y-h/2;
        this.east=x+w/2;
        this.west=x-w/2;
    }

    public Rectangle(double w, double h){
        this.x = w/2;
        this.y = h/2;
        this.w = w;
        this.h = h;
        this.north = h;
        this.south = 0;
        this.east = w;
        this.west = 0;
    }

    public boolean isOverlap(Rectangle rectangle){
        if(rectangle.south>this.north || rectangle.west>this.east|| rectangle.north<this.south||rectangle.east<this.west){
            return false;
        }
        return true;
    }

    public boolean isContainCell(Cell cell){
        if(cell.x>=this.west && cell.x<this.east && cell.y>=this.south && cell.y<this.north) {
            return true;
        }
        return false;
    }


}
