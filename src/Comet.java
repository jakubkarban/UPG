public class Comet extends Type{
    public Comet(String name, Vector2D<Double> position, Vector2D<Double> speed, double weight, double pom){
        super(name,position,speed,weight,pom);
    }
    public Comet(){
        this("",new Vector2D(0,0),new Vector2D(0,0),0,0);
    }
} 