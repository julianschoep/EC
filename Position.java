public class Position {

    private double[] coordinates;

    public Position(double[] coordinates){
        this.coordinates = coordinates;

    }

    public double[] getPosition(){
        return this.coordinates;
    }

    public void setPosition(double[] position){
        this.velocity = velocity;
    }

    public double getCoordAt(int i){
        return this.coordinates[i];
    }

    public void setCoordAt(int i, double coord){
        this.coordinates[i] = coord;
    }

    public double[] subtract(double[] subtractor){
        double result[];
        result = this.coordinates;
        for(int i= 0; i <= this.coordinates.length-i; i++){
            result[i] = this.coordinates[i] + subtractor[i]
        }
        return result;
    }

}