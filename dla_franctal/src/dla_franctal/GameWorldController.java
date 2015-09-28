/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is the Controller and is able to control the star 
  	and the end of the simulation.
  	
    Copyright (C) 2014  Stefano Bettinelli

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package dla_franctal;

import java.util.ArrayList;
import java.io.*;

import org.jfree.ui.RefineryUtilities;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class GameWorldController extends BasicGame {
	private int iterationNo = 0;
	private int movementType;
	private GameWorldView theView;
	private GameWorldModel theModel;
	
	/*
	 * Here we have an ArrayList that contains the static particles in 
	 * the current game loop iteration, 
	 * those that has already collided with the cluster...
	 */
	private ArrayList<Integer> staticParticlesAtCurrentTick = new ArrayList<Integer>();
	
	/*
	 * ...and here we have an ArrayList that contains the floating particles in 
	 * the current game loop iteration, that are the particles that are still 
	 * trying to collide with the DLA cluster
	 */
	private ArrayList<Integer> floatingParticlesAtCurrentTick = new ArrayList<Integer>();
	
	/*
	 * This ArrayList contains the calculation of the ratio between the actual number of 
	 * static particles and the bounding box area of the DLA every iteration of the game loop
	 * ===>  DLA_particles / DLA_BB_Area
	 */
	private ArrayList<Double> bbAreaRatio = new ArrayList<Double>();
	
	private static String particlesIterations = "particlesIterations.txt";
	private static File particlesIterationsFile;
	private static PrintWriter iterationOutPut;
	
	public GameWorldController(GameWorldView theView, GameWorldModel theModel, int movementType){
		this.movementType = movementType;
		this.theView = theView;
		this.theModel = theModel;
		this.theModel.setMovementType(movementType);
		
		particlesIterationsFile = new File("./"+particlesIterations);
		if(!particlesIterationsFile.exists()){
			try{
				particlesIterationsFile.createNewFile();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else System.out.println("file exists!");
		
		 try {
			 //true parameter for appen on FileWriter
			 iterationOutPut = new PrintWriter(new BufferedWriter(new FileWriter(particlesIterationsFile, true)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialise() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void update(float delta) {
    	/*if there are still floating particles the world is update one more...*/
    	if( this.theModel.getStaticParticleNumber() != this.theModel.getInitialParticleNumber() ){
    		//System.out.println(this.theModel.getStaticParticleNumber()+" "+this.theModel.getInitialParticleNumber());
    		iterationNo++;
    		this.theModel.updateWorld();
    		//update of the needed arrays to collect data for future analysis
        	staticParticlesAtCurrentTick.add(new Integer(theModel.getStaticParticleNumber()));
        	floatingParticlesAtCurrentTick.add(new Integer(theModel.getFloatingParticleNumber()));
        	theModel.getCollisionIteration().clear();
        	if( (double)theModel.dlaBoundingBoxArea() > 0 ){
        		bbAreaRatio.add(new Double( (double)theModel.getStaticParticleNumber() / (double)theModel.dlaBoundingBoxArea()  ));
        	}
    	}
    	/*otherwise the simulation can terminate*/
    	else{
    		/* A line chart is generated with static particle number ons the X axes and the ratio
    		 * DLA_particles / DLA_BB_Area on the Y axes
    		 */
    		final LineChart dlaLineChartBBAreaRation = new LineChart("DLA_line_chart_"+getMovementType()+"_"+getParticleNo(), bbAreaRatio, staticParticlesAtCurrentTick);
    		dlaLineChartBBAreaRation.pack();
    		RefineryUtilities.centerFrameOnScreen(dlaLineChartBBAreaRation);
    		dlaLineChartBBAreaRation.setVisible(true);
    		
    		/* The iteration needed to complete the actual simulation is then saved to 
    		 * the particlesIterations.txt text file
    		 */
    		iterationOutPut.println(movementType+" "+iterationNo+" "+ theModel.getInitialParticleNumber());
    		iterationOutPut.close();
    		/* this is a little work around to stop the mini2dx game loop
    		 * I couldn't find a clean way to do it in the mini2dx documentation...I will fix this soon  
    		 */
    		while(true) {
    		    try {
    		        Thread.sleep(1000);
    		    } catch (InterruptedException e) {
    		        e.printStackTrace();
    		    }
    		}
    	}
    }

    @Override
    public void interpolate(float alpha) {
    }

    @Override
    public void render(Graphics g) {
    	theModel.drawModel(g);
    	if(theModel.yMax-theModel.yMin > 0 && theModel.xMax-theModel.xMin > 0){
    		/*switch color to red to draw the bounding box perimeter...*/
    		g.setColor(Color.RED);
    		g.drawRect(theModel.yMin,theModel.xMin,theModel.yMax-theModel.yMin+1, theModel.xMax-theModel.xMin+1);
    		/*...switching back to white*/
    		g.setColor(Color.WHITE);
    	}
    }
	
	public void start(){
		theView.displayWorld(theModel.getWidth(), theModel.getHeight(), this);
	}

	public int getMovementType() {
		return movementType;
	}
	
	public int getParticleNo() {
		return theModel.getInitialParticleNumber();
	}

	public void setMovementType(int movementType) {
		this.movementType = movementType;
	}

}
