public class Value_struct {
    private double x_index;
    private double y_index;

    public Value_struct(double x_index, double y_index) {
        this.x_index = x_index;
        this.y_index = y_index;
    }

    public void setX_index(double x_index) {
        this.x_index = x_index;
    }

    public void setY_index(double y_index) {
        this.y_index = y_index;
    }

    public double getX_index() {
        return x_index;
    }

    public double getY_index() {
        return y_index;
    }
}
