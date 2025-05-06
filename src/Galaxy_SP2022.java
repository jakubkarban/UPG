import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.servlet.DisplayChart;

import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage; 


import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;




public class Galaxy_SP2022 {
	
	private static int lengthScaleFactor = 1;	   
	private static int maxScaleFactor = 5;
	private static final String zoomOutString = "Zoom Out";
	private static final String zoomInString = "Zoom in"; 
	private static JFrame okno; 
	private static DrawingPanel panel;
	private static double scalingFactor = 1.2; 
	// private String PATH = new DrawingPanel(panel); 
				
	public static void main(String[] args) throws IOException {
		 
		okno = new JFrame();
		okno.setTitle("Jakub Karban, A21B0165P");
		okno.setSize(800,600);
		okno.setMinimumSize(new Dimension(800,600));
		
		panel = new DrawingPanel(space);
		okno.add(panel, BorderLayout.CENTER); 	
		
	
		
		
		JPanel toolBar = new JPanel(new GridLayout(6, 5, 4, 3)); 
		        
		JButton pngButton = new JButton("Png Export");
		toolBar.add(pngButton, BorderLayout.EAST); 
		
		JButton svgButton = new JButton("SVG Export"); 
		toolBar.add(svgButton, BorderLayout.EAST);
		
		JButton speedUp = new JButton("Speed 2x"); 
		toolBar.add(speedUp, BorderLayout.EAST);
		
		JButton speedDown = new JButton("Speed 0.5x"); 
		toolBar.add(speedDown, BorderLayout.EAST);
		
		JButton speedNormal = new JButton("Speed 1x"); 
		toolBar.add(speedNormal, BorderLayout.EAST);
		
		JButton stopIt = new JButton("Stop It"); 
		toolBar.add(stopIt, BorderLayout.EAST);
		
		JButton zoomIn = new JButton(zoomInString); 
		toolBar.add(zoomIn, BorderLayout.EAST);
		
		JButton zoomOut = new JButton(zoomOutString); 
		toolBar.add(zoomOut, BorderLayout.EAST); 
		
		JButton gravityFon = new JButton("Gravity field on"); 
		toolBar.add(gravityFon, BorderLayout.EAST);
		
		JButton gravityFoff = new JButton("Gravity field off"); 
		toolBar.add(gravityFoff, BorderLayout.EAST);

	    
		okno.pack(); 
		
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setLocationRelativeTo(null); 		
		okno.setVisible(true);
		okno.add(toolBar, BorderLayout.EAST);
		
		
		
		pngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	panel.printPng(panel);           
            }         
        });	
		
		svgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        		String svgNS = "http://www.w3.org/2000/svg";
        	    Document doc = domImpl.createDocument(svgNS, "svg", null);

        	    SVGGraphics2D graphics = new SVGGraphics2D(doc);

        	    Galaxy_SP2022 test = new Galaxy_SP2022();
        	    try {
        	    	test.paintImage(graphics);

        	    	boolean useCSS = true;
        	    	Writer out = new OutputStreamWriter(new FileOutputStream("pictures/SvgAnimation.svg"), "UTF-8");
        	    	graphics.stream(out, useCSS);            
        	    } catch (IOException ex) {
        	    	ex.printStackTrace();
        	    }
            }         
        });	
		
		speedUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	space.setTimeSpeed(2);             
            }         
        });	
		
		speedDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	space.setTimeSpeed(0.5);             
            }         
        });	
		
		speedNormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	space.setTimeSpeed(1);             
            }         
        });	
		
		stopIt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	panel.setStopped(!panel.stopped());
            }         
        });
		
		zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	zoomIn.setEnabled(true);
                ++lengthScaleFactor;
               
                panel.populateStarGroup( space.getScaleFactor() );
                if (lengthScaleFactor > maxScaleFactor)
                    zoomOut.setEnabled(false);
            }       
        });
		
		zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	String command = evt.getActionCommand();
            	
                if (command.equals(zoomOutString)) {
                    zoomIn.setEnabled(true);
                    ++lengthScaleFactor;
                    panel.populateStarGroup(lengthScaleFactor);
                    if (lengthScaleFactor > maxScaleFactor)
                        zoomOut.setEnabled(false);
                }
                zoomOut.setEnabled(true);
            }         
        });
		
		gravityFon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	
            	panel.setGravitational(true);		
            	
            	            
            }         
        });	
		gravityFoff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
            	
            	panel.setGravitational(false);		
            	
            	            
            }         
        });
		
		
		space.loadArgs(new File(args[0]));  		
		for (int i = 0; i < space.getskyFrames().size(); i++){
			myTable.add(i, null);
		}
		for (int i = 0; i < space.getskyFrames().size(); i++){
			JTable.add(i, null);
		}
		
				
		Timer tm = new Timer();
		tm.schedule(new TimerTask() {
			@Override
			public void run() {
				
		               
		            
		      
				
				panel.repaintTime(space.getTime());	
				update(); 
				
				
				okno.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {
					}
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_SPACE) {							
							panel.setStopped(!panel.stopped());	
						}
						if(e.getKeyCode() == KeyEvent.VK_SHIFT){
							panel.setAdd(true);
						}
						// Zpomaleni animace na 0,5
						if(e.getKeyCode() == KeyEvent.VK_NUMPAD0){
							space.setTimeSpeed(0.5);
						}
						// Vraceni animace na puvodni hodnotu
						if(e.getKeyCode() == KeyEvent.VK_NUMPAD1){
							space.setTimeSpeed(1);
						}
						// Zrychleni animace 2x
						if(e.getKeyCode() == KeyEvent.VK_NUMPAD2){
							space.setTimeSpeed(2);
						}
						if(e.getKeyCode() == KeyEvent.VK_G){
							panel.setGravitational(true);
						}
						if(e.getKeyCode() == KeyEvent.VK_F){
							panel.setGravitational(false);
						}						
					}
					@Override
					public void keyReleased(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_SHIFT){
							panel.setAdd(false);
						}
					}
					
				});
				
				
				okno.addMouseListener(new MouseListener() {
					
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub		
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						}
					@Override
					public void mouseClicked(MouseEvent e) {
						
						panel.MouseInteract(e.getX() - 8,e.getY() - 30);
						for (Type planets:space.getskyFrames()) {
							int pom = space.getskyFrames().indexOf(planets);
							if (planets.selected()) {
								if (myTable.get(pom) == null) {
									myTable.set(pom, graphs(planets)); 
								}
								else {
									getMyTable().get(pom).toFront(); 
								}
							}
						}
						
					}});
				if(!panel.stopped()){
					space.recount();
				}else { 
					space.myDeltas();
				}
			}
		}, 0, 20);
		
		
		
	}
	
	public void paintImage(Graphics g) throws IOException {
	    File imageSrc = new File("pictures\\Animation.png");
	    BufferedImage img = ImageIO.read(imageSrc);
	    
	    g.drawImage(img,0,0,null);
	}
	
	private static ArrayList<JFrame> myTable = new ArrayList<JFrame>();
	private static ArrayList<ChartPanel> JTable = new ArrayList<ChartPanel>(); 
	private static SkyFrame space = new SkyFrame(); 
	private JPanel dPanel;
	 
	
	public static JFrame graphs(Type planets) {
		JFrame win = new JFrame(); 
		win.setTitle("Jakub Karban, A21B0165P"); 
		win.setSize(640, 480); 
		win.setLocationRelativeTo(null); 
		win.setVisible(true); 
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ChartPanel label = new ChartPanel(myData(planets)); 
		JTable.set(space.getskyFrames().indexOf(planets), label);  
		win.add(label); 
		win.pack(); 
 		return win; 
	}
	
	public static ArrayList<JFrame> getMyTable() {
		return myTable; 
	}
	
	public static void update() {
		for (int i = 0; i < myTable.size(); i++) {
			JFrame jframe = myTable.get(i); 
			if (jframe != null) {
				if (jframe.isDisplayable()) {
					ChartPanel chartpanel = JTable.get(i); 
					jframe.remove(chartpanel);			 
					chartpanel = new ChartPanel(myData(space.getskyFrames().get(i)));
					chartpanel.setPreferredSize(new Dimension(640, 480));
					jframe.add(chartpanel); 
					jframe.pack(); 
				}
				else {
					myTable.set(i, null); 
				}
			}
		}
	}
	
	public static void delete(Type planets){
		int plnt = space.getskyFrames().indexOf(planets);
		myTable.remove(plnt);
		JTable.remove(plnt);
	}
	
	public static JFreeChart myData(Type planets) {
		XYSeriesCollection xys1 = new XYSeriesCollection(); 
		XYSeries xys2 = new XYSeries("Speed " + planets.getClass().getName() + ": " + planets.getName()); 
		xys2.add(0, null);
		xys2.add(30, null);
		double current = System.currentTimeMillis()/1000.0; 
		while(planets.speedTime(current)) {}
		List<Vector2D<Double>> speed = planets.getAccelerationLast30s(); 
		List<Double> speedTime = planets.getAccelerationTimes(); 
		double min = Double.MAX_VALUE; 
		double max = Double.MIN_VALUE; 
		double different = speedTime.get(0) - current; 
		for (int i = 0; i < speed.size(); i++) {
			double speed1 = speed.get(i).lenght()*3.6;  
			if (min > speed1) {
				min = speed1; 
			}
			if (max < speed1) {
				max = speed1; 
			}
			xys2.add(speedTime.get(i) - current - different, speed1);
		}
		xys1.addSeries(xys2);
		JFreeChart jfreechart = ChartFactory.createXYLineChart(planets.getClass().getName() + " speed: " + planets.getName(), "Time", "Speed", xys1);
		jfreechart.setBackgroundPaint(Color.BLACK);
		XYPlot xyp = jfreechart.getXYPlot();
		xyp.setBackgroundPaint(Color.BLACK);
		xyp.setRangeGridlinePaint(Color.WHITE);
		NumberAxis rng = (NumberAxis)xyp.getRangeAxis();
		rng.setRange(min, max);
		return jfreechart; 
	}
	
	
}


