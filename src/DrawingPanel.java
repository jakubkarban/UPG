import javax.imageio.ImageIO;
import javax.swing.*;		
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector; 

public class DrawingPanel extends JPanel {

	public void repaintTime(double t) {
		this.t = t;
		super.repaint();
	}

	private SkyFrame space;
	private double t;
	private double translate_X;
	private double translate_Y;
	private double scale;
	private int margin = 5;
	private boolean stoppedRender = false;
	private boolean add = false;
	private Color text = Color.BLACK;
	private Color highlight = Color.GREEN;
	private Color backgroundColor = Color.WHITE;
	private boolean gravitational = false;
	private JPanel dPanel;
	private DrawingPanel panel; 
	private Arrow[] gravityfield = new Arrow[112];
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(backgroundColor);
		g2.fillRect(0,0,this.getWidth(),this.getHeight());
		draw(g2);
	}
	
	public void getScale(double scale) {
		this.scale = scale; 
	}

	public void setAdd(boolean add){
		this.add = add;
	}
	
	public void getPng(JPanel dPanel) {
		this.dPanel = dPanel; 
	}

	public boolean stopped() {
		return stoppedRender;
	}

	public void setStopped(boolean stoppedRender) {
		this.stoppedRender = stoppedRender;
	}

	public void setGravitational(boolean gravitational){
		this.gravitational = gravitational;
	}
	public DrawingPanel(SkyFrame space) {
		this.space = space;
		this.setPreferredSize(new Dimension(800, 600));
	}

	private Ellipse2D.Double drawPlanet(Type planets){		
		double R = Math.abs(planets.getR());		
		if(R<(4/scale)){
			if(scale<3){
				R = (margin*0.75)/scale;
			}
		}
		return new Ellipse2D.Double(planets.getPosition().x-R,planets.getPosition().y-R,R*2,R*2);
	}

	public Shape drawplanets(Type planets) {
		if(planets.getClass().equals(Planet.class)){
			return drawPlanet(planets);
		}
		if(planets.getClass().equals(Comet.class)){
			return drawPlanet(planets);
		}		
		return null;
	}

	private void tail(Graphics2D g2, Type planets){
		List<Vector2D<Double>> tail = planets.getTail();
		double R = Math.abs(planets.getR());
		if(R<(4/scale)){
			if(scale<3){
				R = (margin*0.75)/scale;
			}
		}
		double c = 1;
		Color save = g2.getColor();
		int a = 200;
		for(int i = tail.size()-1; i>=0; i-=2){
			c+=0.1; // tail's width 
			g2.setColor(Color.LIGHT_GRAY);
			g2.fill(new Ellipse2D.Double((double)tail.get(i).x-(R/c),(double)tail.get(i).y-(R/c),R*2/c,R*2/c));
			if(a>14){
				a-=15;
			}else {
				a = 0;
			}
		}
		g2.setColor(save);
	}

	
	public void draw(Graphics2D g2) {
		
		g2.setColor(text);
		double min_X = Double.MIN_VALUE;
		double min_Y = Double.MIN_VALUE;
		
		double max_X = Double.MAX_VALUE;
		double max_Y = Double.MAX_VALUE;

		double max_weight_P = -Double.MAX_VALUE;
		double max_weight_C = -Double.MAX_VALUE;

		for (Type planets: space.getskyFrames()) {
			if(planets!=null){
				double x = planets.getPosition().x;
				double y = planets.getPosition().y;
				if(x + planets.getR()>min_X){
					min_X = x + planets.getR();
				}
				if(x + planets.getR()<max_Y){
					max_Y = x - planets.getR();
				}
				if(y + planets.getR()>min_Y){
					min_Y = y + planets.getR();
				}
				if(y + planets.getR()<max_X){
					max_X = y - planets.getR(); 
				}
				if(planets.getClass().equals(Planet.class)){
					if(planets.getWeight()>max_weight_P){
						max_weight_P = planets.getWeight();
					}
				}
				if(planets.getClass().equals(Comet.class)){
					if(planets.getWeight()>max_weight_C){
						max_weight_C = planets.getWeight();
					}
				}
			}
		}
		
		scale = Math.min((this.getWidth()-margin*4)/(min_X-max_Y),(this.getHeight()-margin*4)/(min_Y-max_X));
		g2.setFont(new Font(Font.SERIF, Font.ITALIC, 14));
		String text;
		
		text = String.format("Simulation time: %.12f", t);
		int line = (int)(g2.getFontMetrics().getHeight()*0.5+5);

		g2.drawString(text, (int)(this.getWidth()-g2.getFontMetrics().stringWidth(text)-margin), line);
		text = String.format("Scale: %.12f", scale);
		g2.drawString(text, (int)(this.getWidth()-g2.getFontMetrics().stringWidth(text)-margin), line*2);
		
		int lineCounter = 1;
		for (Type planets: space.getskyFrames()) {
			if(!planets.equals(null)&&planets.selected()){

				g2.setFont(new Font(Font.SERIF, Font.ROMAN_BASELINE, 14));
				
				text = String.format("Name: %s", planets.getName());
				g2.drawString(text, margin, line*lineCounter);
				lineCounter++;
				text = String.format("Position: (%.2f;%.2f)", planets.getPosition().x, planets.getPosition().y);
				g2.drawString(text, margin, line*lineCounter);
				lineCounter++;
				text = String.format("Speed: (%.4f;%.4f)", planets.getVector().x, planets.getVector().y);
				g2.drawString(text, margin, line*lineCounter);
				lineCounter++;
				text = String.format("Weight: %.4f", planets.getWeight());
				g2.drawString(text, margin, line*lineCounter);
				lineCounter++;
			}
		}

		translate_X = (((this.getWidth())*0.5)-((min_X+max_Y)/2)*scale);
		translate_Y = (((this.getHeight())*0.5)-((min_Y+max_X)/2)*scale);
		g2.translate(translate_X,translate_Y);
		g2.scale(scale,scale);
		for (Type planets: space.getskyFrames()) {
			if(!planets.equals(null)){
				setplanetsColor(planets,g2, max_weight_P, max_weight_C);
				tail(g2,planets);
			}
		}

		for (Type planets: space.getskyFrames()) {
			if(!planets.equals(null)) {
				setplanetsColor(planets,g2, max_weight_P, max_weight_C);
				g2.fill(drawplanets(planets));
				if(planets.selected()) {
					g2.setColor(highlight);
					g2.setStroke(new BasicStroke((float) (margin/scale))); 
					g2.draw(drawPlanet(planets));
				}
			}
		} if(gravitational){
			drawMyGravity(g2);
		}
	}

	private void setplanetsColor(Type planets, Graphics2D g2, Double maxMassP, Double maxMassC){
		if(planets.getClass().equals(Planet.class)){
			
			g2.setColor(Color.getColor("Planet", Color.BLUE));
		}
		if(planets.getClass().equals(Comet.class)){
			
			g2.setColor(Color.getColor("Comet", Color.RED));
		}
		
	}

	public void MouseInteract(double x,double y){
		double xPos = (x-translate_X)/scale;
		double yPos = (y-translate_Y)/scale;

		for (Type planets: space.getskyFrames()) {
			if(planets!=null){
				if(drawPlanet(planets).contains(xPos,yPos)){
					planets.setSelected(true);
				}else if(!add){
					planets.setSelected(false);
				}
			}
		}
	}
	
	private Vector starGroup;
	
	public void populateStarGroup(int mLengthScaleFactor) {
		space.getSpace(space); 
		
        if (space.getskyFrames().size() >= mLengthScaleFactor) 
            return; 
        
        SkyFrame space1 = new SkyFrame(5/SkyFrame.getScaleFactor(), 
        		SkyFrame.getScaleFactor()*(800-space.getAbsPos(Coord.X)),
        		(SkyFrame.getScaleFactor()-1)*(800-space.getAbsPos(Coord.X)), 
        		space);
        
        starGroup.addElement(space1);
     

        
    }
	
	public void printPng(DrawingPanel panel) {
		this.setPanel(panel); 
		BufferedImage bi = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		panel.paint(g);  
		g.dispose();
		
		try {
			ImageIO.write(bi, "png", new File("pictures/Animation.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
		}      
		
	}

	public DrawingPanel getPanel() {
		return panel;
	}

	public void setPanel(DrawingPanel panel) {
		this.panel = panel;
	}
	
	public void drawMyGravity(Graphics2D g2){
		for (int i = 0; i < gravityfield.length; i++){
			gravityfield[i] = new Arrow(new Vector2D<Double>(((i%14+1)*this.getWidth()/15.0-translate_X)/scale,
					((i/14+1)*this.getHeight()/9.0-translate_Y)/scale));
		}
		g2.setColor(Color.GREEN);
		g2.setStroke(new BasicStroke((float)(margin/scale)));
		for (Arrow arrow: gravityfield) {
		double myX = 0;
		double myY = 0;
		for (Type gravity: space.getskyFrames()) {
				Vector2D<Double> get_vector_planets = gravity.getPosition(); 
				Vector2D<Double> vector_planets = arrow.positionVector2D;
				double lenght = get_vector_planets.decreasing(vector_planets).lenght();
				myX += space.VectorCounter(vector_planets.x, get_vector_planets.x, lenght, gravity.getWeight()*space.getGravitation());
                myY += space.VectorCounter(vector_planets.y, get_vector_planets.y, lenght, gravity.getWeight()*space.getGravitation());
		}
		myArrow(arrow.positionVector2D,new Vector2D<Double>(myX+arrow.positionVector2D.x,myY+arrow.positionVector2D.y),g2);
		}
	}

	private void myArrow(Vector2D<Double> position, Vector2D<Double> gravity,Graphics2D g2d){
		double my_gravity_x = gravity.x - position.x;
		double my_gravity_y = gravity.y - position.y;

		double gravity_length = position.increasing(gravity).lenght();

		double gravity_normalize_x = my_gravity_x / gravity_length;
		double gravity_normalize_y = my_gravity_y / gravity_length;

		double final_arrow_x = gravity_normalize_x * 5;
		double final_arrow_y = gravity_normalize_y * 5;

		double constant_x = -final_arrow_y;
		double constant_y = final_arrow_x;

		constant_x = constant_x * 0.45;
		constant_y = constant_y * 0.45;

		g2d.draw(new Line2D.Double(position.x,position.y,gravity.x, gravity.y));

		g2d.draw(new Line2D.Double(gravity.x, gravity.y, gravity.x - final_arrow_x + constant_x, gravity.y - final_arrow_y + constant_y));
		g2d.draw(new Line2D.Double(gravity.x, gravity.y, gravity.x - final_arrow_x - constant_x, gravity.y - final_arrow_y - constant_y));
	}
}

class Coord {
    public static final byte X = 1, Y = 0;
}