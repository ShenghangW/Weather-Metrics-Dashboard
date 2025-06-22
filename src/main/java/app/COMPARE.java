package app;

public class COMPARE {

    private int measurementID;
    private double value1;
    private double value2;
    private double delta;

    public COMPARE(int measurementID, double value1, double value2, double delta) {
        
        this.measurementID = measurementID;
        this.value1 = value1;
        this.value2 = value2;
        this.delta = delta;

    }

    public int getMeasurementID() {
        return measurementID;
    }

    public double getValue1() {
        return value1;
    }

    public double getValue2() {
        return value2;
    }

    public double getDelta() {
        return delta;
    }

}
