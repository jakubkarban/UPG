import java.util.ArrayList;
import java.util.List;

public abstract class Type {

    private String name;
    private Vector2D<Double> position_of_V;
    private Vector2D<Double> speed_of_V;
    private Vector2D<Double> acceleration;
    private double weight;
    private double pom;
    private boolean selected = false;
    private List<Vector2D<Double>> tail = new ArrayList<Vector2D<Double>>();
    private List<Double> tailTime = new ArrayList<Double>();
    private List<Vector2D<Double>> speed = new ArrayList<Vector2D<Double>>();
    private List<Double> speedTime = new ArrayList<Double>();
    
    public boolean selected(){
        return selected;
    }

    public void setSelected(boolean is){
        selected = is;
    }

    public double getR() {
        return pom;
    }

    public void setR(double R){
        this.pom = R;
    }
    
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Vector2D<Double> getPosition() {
        return position_of_V;
    }

    public void setPosition(Vector2D<Double> position_of_V) {
        this.position_of_V = position_of_V;
    }

    public Vector2D<Double> getVector() {
        return speed_of_V;
    }

    public void setSpeed(Vector2D<Double> speed_of_V) {
        this.speed_of_V = speed_of_V;
    }

    public Type(String name, Vector2D<Double> position, Vector2D<Double> speed, double weight, double pom){
        this.name = name;
        this.position_of_V = position;
        this.speed_of_V = speed;
        this.weight = weight;
        this.pom = pom;
    }

    public Vector2D<Double> getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D<Double> acceleration) {
        this.acceleration = acceleration;
    }

    public void addToTail(Vector2D<Double> now, double time) {
        tail.add(tail.size(),now);
        addTailTime(time);
    }

    public void deleteLast(){
        tail.remove(0);
    }

    public void addTailTime(double time){
        tailTime.add(tailTime.size(),time);
    }

    public boolean isTime(double time){
        if(tailTime.size()>0&&tailTime.get(0)<time){
            deleteLast();
            tailTime.remove(0);
            return true;
        }
        return false;
    }

    
    
    public List<Vector2D<Double>> getAccelerationLast30s(){
        return speed;
    }

    public List<Double> getAccelerationTimes(){
        return speedTime;
    }
    
    public boolean addspeed(Vector2D<Double> vect, double time){
        int i = speed.size();
        if(i < 1){
            speedTime.add(0,time);
            speed.add(0,vect);
            return true;
        }
        boolean pom = (speedTime.get(i-1) + 0.1) < time;
        if(pom){
            speedTime.add(i,time);
            speed.add(i,vect);
        }
        return pom;
    }

    public boolean speedTime(double time){
        if(speedTime.size() > 0 && speedTime.get(0) < time){
            speed.remove(0);
            speedTime.remove(0);
            return true;
        }
        return false;
    }
    
    public List<Vector2D<Double>> getTail() {
        return tail;
    }
    
}