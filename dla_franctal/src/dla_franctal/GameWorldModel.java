/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is the Model and contain the main data structures.
  	
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

import org.mini2Dx.core.graphics.Graphics;

public class GameWorldModel {
	
	/*width and height in pixels of the world grid*/
	private int width;
	private int height;
	
	/*the world is modeled as a two-dimensional matrix of integers*/
	private int worldMatrix[][];
	
	/* Collection of floating and static particles, note that once particles became static they are 
	 * removed from this collection, this is made in the update method of this class
	 * */
	private ArrayList<Particle> particleCollection;
	
	/* This array will be used for an upcoming fuature the evaluate at witch number of iteration
	 * a specific particle will collide, for now only the part regarding the collection of the data is implemented.
	 * The statistical part is still missing...
	 */
	private ArrayList<Integer> collisionIteration = new ArrayList<Integer>();;
	private int movementType;
	private int particleNo; //initial number of floating particles
	private int staticParticles;
	
	/* The values here are used to calculate the bounding box, they may looks strage
	 * for example xMax = -1, but that is a way to update correctly maximum value of
	 * the current most right positioned particle in the updateBB method in this class
	 * */
	public int xMin = 600;
	public int xMax = -1;
	public int yMin = 800;
	public int yMax = -1;
	private int elements = 0; //this is used to insert the "particleNo" particles in the simulation

	public GameWorldModel(int width, int height, int particleNo, int movementType) {
		this.movementType = movementType;
		this.particleCollection = new ArrayList<Particle>(particleNo);
		this.width = width;
		this.height = height;
		this.particleNo = particleNo;
		this.staticParticles = 0;
		//initialize to 0 all the matrix cells
		this.worldMatrix = new int[this.height][this.width];
		for(int i=0; i<this.height; i++){
			for(int j=0; j<this.width; j++){
				this.worldMatrix[i][j] = 0;
			}
		}
		
		/*
		 * if the simulation is not of the snowflake type then a seed is positioned in the center
		 * and the max and min value for X and Y are recalculated to surround the seed point
		 */
		if( movementType == 1 || movementType == 2 || movementType == 3 ){
			worldMatrix[((height/2)-1)][((width/2)-1)] = 1;
			elements++;
			staticParticles++;
			xMin = (height/2)-2;
			xMax = height/2;
			yMin = (width/2)-2;
			yMax = width/2;
		}
	}

	public GameWorldModel() {
		/*default values 800x600 world with 10000 particles*/
		this(800, 600, 10000,0);
	}
	
	/*
	 * This method allows the creation of new particles outside of the bounding box of the DLA
	 */
	private void createParticleOutsideOfBB(){
		int x = 0;
		int y = 0;
		int r = randInt.getRandInt(1, 4);
		if(movementType == 0){
			x = randInt.getRandInt(0,xMin);
			y = randInt.getRandInt(0,799);
		}
		else
		{
			switch (r) {
			case 1: 
				x = randInt.getRandInt(0,599);
				y = randInt.getRandInt(0,yMin);
				break;
			case 2:
				x = randInt.getRandInt(0,599);
				y = randInt.getRandInt(yMax,799);
				break;
			case 3:
				x = randInt.getRandInt(0,xMin);
				y = randInt.getRandInt(0,799);
				break;
			case 4:
				x = randInt.getRandInt(xMax,599);
				y = randInt.getRandInt(0,799);
				break;
			}
		}
		particleCollection.add(new Particle(x,y,1,1,width,height,randInt.getRandInt(0,1),randInt.getRandInt(0,1),worldMatrix));
	}
	
	/*
	 * update the boundaries value xMin, xMax, yMin, yMax based on the actual DLA structure
	 */
	private void updateBB(int i){
		if( particleCollection.get(i).getX() < xMin ) xMin = particleCollection.get(i).getX();
		if( particleCollection.get(i).getX() > xMax ) xMax = particleCollection.get(i).getX();
		if( particleCollection.get(i).getY() < yMin ) yMin = particleCollection.get(i).getY();
		if( particleCollection.get(i).getY() > yMax ) yMax = particleCollection.get(i).getY();
	}
	
	/*
	 * This is the most important method of the class, it's function is to call the 
	 * method for selected movement type for each floating particles
	 */
	public void updateWorld() {
		/*
		 * The particles are created during the simulation, this prevents some strange behaviors
		 * that rises when all the particles are added at the start of a simulation, for example 
		 * the it was noticed that the random generation for the position followed some sort of pattern
		 * and this influenced the formation the DLA cluster, resulting in an unxpected formation
		 * */
		if( elements < getInitialParticleNumber() ){
			createParticleOutsideOfBB();		
			elements++;
		}
		//now for the each particles that are still floating a movement update is made,
		//according to the type of movement
		for(int i=0; i<particleCollection.size(); i++){
			if( particleCollection.get(i).isFloating() == false ){
				particleCollection.remove(i);
			}
			//If the particle has gone outside of the boundaries then 
			//a new one is created to replace it, this can happen only if the movementType is 2 or 3 (balistic or square spiral)
			else if( particleCollection.get(i).isOutsideOfTheWorld() == true ){
				particleCollection.remove(i);
				createParticleOutsideOfBB();
			}
			else{
				switch(movementType){
				case 0:
					/*
					 * each movement method return a boolean if it's false it mean that the particle ha moved and
					 * the collided to the cluster and so the static particle counter il incremented by one
					 * */
					if( particleCollection.get(i).snowFlakeFallMove() == false ){
						collisionIteration.add(new Integer(particleCollection.get(i).getIterationNumber()));
						updateBB(i);
						staticParticles++;
					}
					break;
				case 1: 
					if( particleCollection.get(i).randomMove() == false ){
						collisionIteration.add(new Integer(particleCollection.get(i).getIterationNumber()));
						updateBB(i);
						staticParticles++;
					}
					break;
				case 2:
					if( particleCollection.get(i).straightMove() == false ){
						collisionIteration.add(new Integer(particleCollection.get(i).getIterationNumber()));
						updateBB(i);
						staticParticles++;
					}
					break;
				case 3:
					if( particleCollection.get(i).squareSpiralMove() == false ){
						collisionIteration.add(new Integer(particleCollection.get(i).getIterationNumber()));
						updateBB(i);
						staticParticles++;
					}
					break;
				}
			}
		}
	}

	public ArrayList<Integer> getCollisionIteration() {
		return collisionIteration;
	}

	public void drawModel(Graphics g){
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				if( worldMatrix[i][j] == 1 ){
					g.drawRect(j, i, 1, 1);
				}
			}
		}
		
		for(int i=0; i<particleCollection.size(); i++){
			particleCollection.get(i).drawParticle(g);
		}
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setMovementType(int movementType){
		this.movementType = movementType;
	}
	
	/**
	 * 
	 * @return true if every boundaries are holding a non floating particle
	 */
	public boolean boundariesTouched(){
		boolean result = false;
		boolean top = false;
		boolean bottom = false;
		boolean left = false;
		boolean right = false;
		for(int j=0; j < width; j++){
			if( worldMatrix[0][j] == 1 ) top = true;
		}
		for(int j=0; j < width; j++){
			if( worldMatrix[height-1][j] == 1 ) bottom = true;
		}
		for(int i=0; i < height; i++){
			if( worldMatrix[i][0] == 1 ) left = true;
		}
		for(int i=0; i < height; i++){
			if( worldMatrix[i][width-1] == 1 ) right = true;
		}
		result = top & bottom & left & right;
		return result;
	}
	
	public void removeParticles(){
		for(int i=0; i<particleCollection.size(); i++){
			particleCollection.remove(i);
		}
	}
	
	/**
	 * 
	 * @return the actual number of particles in the particle collection
	 */
	public int getFloatingParticleNumber(){
		return particleCollection.size();
	}
	
	public int getStaticParticleNumber(){
		return staticParticles;
	}
	
	public int getInitialParticleNumber(){
		return particleNo;
	}
	
	public int dlaBoundingBoxArea(){
		if( xMax > -1 && xMin < 600 && yMax > -1 && yMin < 800  )
			return ((xMax - xMin) * (yMax - yMin));
		return 0;
	}

}
