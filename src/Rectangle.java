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
        this.north = y + h / 2;
        this.south = y - h / 2;
        this.east = x + w / 2;
        this.west = x - w / 2;
    }

    public Rectangle(double w, double h) {
        this.x = w / 2;
        this.y = h / 2;
        this.w = w;
        this.h = h;
        this.north = h;
        this.south = 0;
        this.east = w;
        this.west = 0;
    }

    public boolean isOverlap(Rectangle rectangle) {
        if (rectangle.south > this.north || rectangle.west > this.east || rectangle.north < this.south || rectangle.east < this.west) {
            return false;
        }
        return true;
    }

    // if the cell's center is contained in the rectangle
    public boolean isContainCell(Cell cell, boolean isCritical) {
        if (isCritical) {
            if (cell.x >= this.west && cell.x <= this.east && cell.y >= this.south && cell.y <= this.north) {
                return true;
            }
            return false;
        } else { // original (default)
            if (cell.x >= this.west && cell.x < this.east && cell.y >= this.south && cell.y < this.north) {
                return true;
            }
            return false;
        }
    }


    // whether part of the cell is in the rectangle
    public boolean isContainCellPart(Cell cell){
        if (cell.x >= this.west && cell.x <= this.east && cell.y >= this.south && cell.y <= this.north) {
            return true;
        }else if( cell.x >= this.west && cell.x <= this.east ){
            double d1 = Math.abs(cell.y - this.south);
            double d2 = Math.abs(cell.y - this.north);
            return Math.min(d1,d2) <= cell.radius;
        }else if(cell.y >= this.south && cell.y <= this.north){
            double d1 = Math.abs(cell.x - this.west);
            double d2 = Math.abs(cell.x - this.east);
            return Math.min(d1,d2) <= cell.radius;
        }else {
            double d1 = distanceOfPoints(cell.x,this.east,cell.y,this.north);
            double d2 = distanceOfPoints(cell.x,this.east,cell.y,this.south);
            double d3 = distanceOfPoints(cell.x,this.north,cell.y,this.north);
            double d4 = distanceOfPoints(cell.x,this.north,cell.y,this.south);
            return Math.min(Math.min(d1,d2),Math.min(d3,d4)) <= cell.radius;
        }

    }

    public double distanceOfPoints(double x1,double x2, double y1, double y2){
        double delta_x =  x1 - x2;
        double delta_y = y1- y2;
        return(Math.sqrt(delta_x*delta_x + delta_y*delta_y ));
    }
}
