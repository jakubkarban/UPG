import java.io.*;
import java.util.*;
 
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.w3c.dom.events.MouseEvent; 


public class SkyFrame {
	public SkyFrame(){} 
   
	public SkyFrame(int i, double d, double e, SkyFrame space2) {
		// TODO Auto-generated constructor stub
	}

	private double force;
    private double time1; // time in seconds
    private double time;
    private double time2 = 1;
    private double timeToBack = 0; 

    private long current;
    private long start;
    private long end;
    private long delta = 0;
    
    private File file;     
    private JButton bttn;
    protected double xPos, yPos;    
    protected double xVel = 0, yVel = 0;
    private SkyFrame space; 
   
    
    public void getSpace(SkyFrame space) {
    	this.space = space; 
    }
    
    private static int lengthScaleFactor = 1;
	public static int getScaleFactor() {
	        return lengthScaleFactor;
	    } 
	
	public void setGravitation(double gravitation) {
	        this.force = gravitation;
	    }

	public double getGravitation() {
	       return force;
	    }
	
    public double getTime() {
        return time;
    }
    
    public void returnMyButton(JButton bttn) {
    	this.bttn = bttn; 
    	
    }

    public void myDeltas(){
        delta = (System.nanoTime() - (start+(long)((time*1000000000.0)/time1)));
    }

    private List<Type> skyFrames = new ArrayList<Type>();

    public List<Type> getskyFrames() {
        return skyFrames;
    }

    public void setTimeSpeed(double time2) {
        this.time2 = time2;
    }
    
    public void setTimeBack(double timeToBack) {
    	this.timeToBack = timeToBack; 
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void settime1(double time1) {
        this.time1 = time1;
    }

    public double gettime2(){
        return time2;
    }

   

    public void recount(){
        current = System.nanoTime();
        if(delta > 0){
            start += delta;
            end += delta;
            for (Type planets : skyFrames) {
				List<Double> speedTimes = planets.getAccelerationTimes(); 
				for(int i = 0; i < speedTimes.size(); i++) {
					double update = speedTimes.get(i); 
					update += (double) (delta/1000000000.0); 
					speedTimes.set(i, update); 
				}
			}
            delta = 0;
        }
        double deltaSize;
        int s = getskyFrames().size();
        if (s < 250) {
        	deltaSize = 500;
        } else {
        	deltaSize = 50;
        }
        if (s < 50) {
        	deltaSize = 20000; 
        }
        double dt_min = time1/deltaSize;
        double dt_max = (double)((current - end)/1000000000.0*time2)*time1;
        time += dt_max;
        double t = dt_max;
        tailing();
        while(t > 0){
            double dt_cmpl = Math.min(t, dt_min);

            for (Type planets: getskyFrames()) {
              
                double myX = 0;
                double myY = 0;
                for (Type planetsForce: getskyFrames()) {
                    if(planets!=planetsForce){
                        Vector2D<Double> get_vector_planets = planetsForce.getPosition();
                        Vector2D<Double> vector_planets = planets.getPosition();
                        double lenght = get_vector_planets.decreasing(vector_planets).lenght();
                        myX += VectorCounter(vector_planets.x, get_vector_planets.x, lenght, planetsForce.getWeight()*force);
                        myY += VectorCounter(vector_planets.y, get_vector_planets.y, lenght, planetsForce.getWeight()*force);

                  
                    }
                }
                planets.setAcceleration(new Vector2D<>(myX,myY));
            }
            double time3 = System.currentTimeMillis()/1000.0;
            for (Type planets: skyFrames) {
                planets.setSpeed(planets.getVector().increasing(planets.getAcceleration().expand(dt_cmpl*0.5)));
                planets.setPosition(planets.getPosition().increasing(planets.getVector().expand(dt_cmpl)));
                planets.setSpeed(planets.getVector().increasing(planets.getAcceleration().expand(dt_cmpl*0.5)));
                planets.addspeed(planets.getVector(),time3+30.0);
            }
            for (int i = 0; i < skyFrames.size(); i++){
                Type planets = skyFrames.get(i);
                if(!planets.equals(null)){
                    for (int j = i; j < skyFrames.size(); j++){
                        Type planets2 = skyFrames.get(j);
                        if(!planets.equals(planets2)){
                            if(typeColision(planets, planets2)){
                                Vector2D planets3 = (planets.getVector().expand(planets.getWeight()));
                                Vector2D planets4 = (planets2.getVector().expand(planets.getWeight()));
                                if(planets.getWeight()>planets2.getWeight()){
                                    planets.setWeight(planets.getWeight()+planets2.getWeight());
                                    planets.setSpeed((planets3.increasing(planets4).expand(1/planets.getWeight())));
                                    planets.setR(Math.cbrt((planets.getWeight()*3)/(4*Math.PI)));
                                    Galaxy_SP2022.delete(planets2);
                                    skyFrames.remove(planets2);
                                }
                                else{
                                    planets2.setWeight(planets.getWeight()+planets2.getWeight());
                                    planets2.setSpeed((planets4.increasing(planets3).expand(1/planets2.getWeight())));
                                    planets2.setR(Math.cbrt((planets2.getWeight()*3)/(4*Math.PI)));
                                    Galaxy_SP2022.delete(planets);
                                    skyFrames.remove(planets);
                                }
                                j--;
                                i--;
                            }
                        }
                    }
                }else {
                    break;
                }
            }
            t = t - dt_cmpl;
        }
        end = System.nanoTime();
    }

    public boolean typeColision(Type planets, Type planets2){
        return planets.getPosition().decreasing(planets2.getPosition()).lenght()<(planets.getR() + planets2.getR());
    }

    public double VectorCounter(double planets, double gravity_planets, double lenght, double weight){
        double x;
        x = ((gravity_planets-planets)*(1/(Vector2D.pow(lenght,3))))*weight;
        return x;
    }

    
    public boolean loadArgs(File file){
        try {
            Scanner sc = new Scanner(file);
            String line = sc.nextLine();
            setForce(Double.parseDouble(line.substring(0,line.indexOf(","))));
            settime1(Double.parseDouble(line.substring(line.indexOf(",")+1)));
            while (sc.hasNextLine()){
                line = sc.nextLine();
                if(!line.isBlank()){
                    String name = line.substring(0,line.indexOf(","));
                    String substring = line.substring(line.indexOf(",")+1);
                    String type = substring.substring(0,substring.indexOf(","));
                    substring = substring.substring(substring.indexOf(",")+1);
                    double position_x = Double.parseDouble(substring.substring(0,substring.indexOf(",")));
                    substring = substring.substring(substring.indexOf(",")+1);
                    double position_y = Double.parseDouble(substring.substring(0,substring.indexOf(",")));
                    substring = substring.substring(substring.indexOf(",")+1);
                    double speed_x = Double.parseDouble(substring.substring(0,substring.indexOf(",")));
                    substring = substring.substring(substring.indexOf(",")+1);
                    double speed_y = Double.parseDouble(substring.substring(0,substring.indexOf(",")));
                    substring = substring.substring(substring.indexOf(",")+1);
                    double weight = Double.parseDouble(substring);
                    double range = Math.cbrt((weight*3)/(4*Math.PI));
                    Type object = null;
                    if(type.equals("Planet")){
                        object = new Planet(name,new Vector2D(position_x,position_y),new Vector2D(speed_x,speed_y),weight,range);
                    }
                    if(type.equals("Comet")){
                        object = new Comet(name,new Vector2D(position_x,position_y),new Vector2D(speed_x,speed_y),weight,range);
                    }
                   
                    skyFrames.add(object);
                }
            }
            
            start = System.nanoTime();
            end = start;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
       
        
        return true;
         
    	
    }

    public void tailing(){
        double t = (((current-start)/1000000000.0));
        for (Type planets: skyFrames) {
            while(planets.isTime(t-1 / time2)){}
            if(planets.getTail().size() == 0){
                planets.addToTail(planets.getPosition(), t);
                
            }else if((planets.getPosition().decreasing(planets.getTail().get(planets.getTail().size() - 1))).lenght()>planets.getR() / 5) {
                planets.addToTail(planets.getPosition(), t);
            }
        }
    }

    public double getAbsPos(byte mAxis) throws StackOverflowError{
        if (mAxis == Coord.X)
            return xPos/SkyFrame.getScaleFactor() + space.getAbsPos(mAxis);
        else
            return yPos/SkyFrame.getScaleFactor() + space.getAbsPos(mAxis);
    }
    
    public double getVel(byte mAxis) {
        if (mAxis == Coord.X)
            return xVel;
        else
            return yVel;
    }    
}