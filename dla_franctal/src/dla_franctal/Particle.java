/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class rappresents a single particle.
  	
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

import org.mini2Dx.core.graphics.Graphics;

public class Particle {
	
	private int iterationNumber = 0;
	private int startX;
	private int startY;
	private int actualX;
	private int actualY;
	private int height;
	private int width;
	private int worldWidth;
	private int worldHeight;
	private int[][] worldMatrix;
	private boolean floating = true;
	private static int particleCounter = 0;
	private int particleId;
	private int straightMoveDirection;
	//variables for the spirale square movement
	private int k = 1;
	private int initialK = 1;
	private int direction = 1;//randInt.getRandInt(1, 4);
	
	/**
	 * 
	 * @param x: x coordinate of the particle
	 * @param y: y coordinate of the particle
	 * @param width: width of the particle  
	 * @param height: height of the particle
	 * @param worldWidth: width of the Particle World
	 * @param worldHeight: height of the Particle World
	 * @param dirX: if dirX is 0 moveDirectionX will be 1 pixel from up -> down else it will from down -> up
	 * @param dirY: if dirY is 0 moveDirectionY will be 1 pixel from left -> right else it will from right -> left
	 * @param worldMatrix: a matrix of 1 and 0, a cell is 1 if and only if there is a static particle in that position
	 */
	public Particle(int x, int y, int width, int height, int worldWidth, int worldHeight, int dirX, int dirY, int[][] worldMatrix){
		particleCounter++;
		this.particleId = particleCounter;
		this.worldMatrix = worldMatrix;
		this.startX = x;
		this.startY = y;
		this.actualX = x;
		this.actualY = y;
		this.width = width;
		this.height = height;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.straightMoveDirection = randInt.getRandInt(1, 8);
	}
	
	/**
	 * Makes the particle move from upside down and again from bottom to the top in a sort of snow flake falling manner 
	 */
	public boolean snowFlakeFallMove(){
		int step = 0;
		if( this.checkCollision() == 1 ) { floating = false; return false;}
		else{
			//if the particle hits the ground...
			if( this.actualX == this.worldHeight-1 ){
				this.worldMatrix[this.actualX][this.actualY] = 1;
				this.floating = false;
				return false;
			}
			//if zero then the particle moves left
			if( randInt.getRandInt(0, 1) == 0 ){
				do{
					step = randInt.getRandInt(0, 4);
					step *= -1;
				}while( (this.actualY + step) < 0 );
				this.actualY+=step;
				this.actualX++;
			}
			else{
				do{
					step = randInt.getRandInt(0, 4);
				}while( (this.actualY + step) >= this.worldWidth );
				this.actualY+=step;
				this.actualX++;
			}
		}
		this.iterationNumber++;
		return true;//the particle was able to move without colliding with the other or on the ground
	}
	/**
	 * In this type of movement the particle goes in a straight line in a randomly chosen direction 
	 */
	public boolean straightMove(){
		int step = randInt.getRandInt(1, 1);
		if( this.checkCollision() == 1 ) { floating = false; return false;}
		switch(this.straightMoveDirection){
		case 1: this.actualX+=step;
		break;
		case 2: this.actualX-=step;
		break;
		case 3: this.actualY+=step;
		break;
		case 4: this.actualY-=step;
		break;
		case 5: this.actualX+=step; this.actualY+=step;
		break;
		case 6: this.actualX-=step; this.actualY-=step;
		break;
		case 7: this.actualX-=step; this.actualY+=step;
		break;
		case 8: this.actualX+=step; this.actualY-=step;
		break;
		}
		this.iterationNumber++;
		return true;
	}
	
	/**
	 * The particle moves randomly in any direction with a 1 pixel step 
	 */
	public boolean randomMove(){
		if( this.checkCollision() == 1 ) { floating = false; return false;}
		int direction = randInt.getRandInt(1, 4);
		switch( direction ){
		case 1: this.actualX+=1;
		break;
		case 2: this.actualX-=1;
		break;
		case 3: this.actualY+=1;
		break;
		case 4: this.actualY-=1;
		break;
		}
		this.iterationNumber++;
		return true;
	}
	
	public boolean squareSpiralMove(){
		if( this.checkCollision() == 1 ) { floating = false; return false;}
		if( k > 0 && direction == 1 ){
			this.actualY+=1;
			k--;
		}
		if( k > 0 && direction == 2 ){
			this.actualX+=1;
			k--;
		}
		if( k > 0 && direction == 3 ){
			this.actualY-=1;
			k--;
		}
		if( k > 0 && direction == 4 ){
			this.actualX-=1;
			k--;
		}
		if( k == 0 ){
			k = initialK++;
			switch(direction){
			case 1:
				direction = 2;
				break;
			case 2:
				direction = 3;
				break;
			case 3:
				direction = 4;
				break;
			case 4:
				direction = 1;
				break;
			}
		}
		this.iterationNumber++;
		return true;
	}
	
	public int getIterationNumber() {
		return iterationNumber;
	}

	/**
	 * This is a very repetitive method that checks if there is a non floating particle in any of the 8 directions that surrounds THIS particle 
	 * @return 1 if there is a collision detection else 0
	 */
	private int checkCollision(){
		if( (actualX >= 1) && actualX < (worldHeight-1) && (actualY >= 1) && (actualY < worldWidth-1) ){
			if(
				worldMatrix[actualX-1][actualY] == 1  ||
				worldMatrix[actualX-1][actualY+1] == 1 ||
				worldMatrix[actualX][actualY+1] == 1 ||  
				worldMatrix[actualX+1][actualY+1] == 1 ||
				worldMatrix[actualX+1][actualY] == 1 ||
				worldMatrix[actualX+1][actualY-1] == 1 ||
				worldMatrix[actualX][actualY-1] == 1 ||
				worldMatrix[actualX-1][actualY-1] == 1
			  )
			{
				worldMatrix[actualX][actualY] = 1;
				floating = false;
				return 1;
			}
		}
		if( actualY == 0 && actualX > 0 && actualX < (worldHeight-1) ){
			if( 
				worldMatrix[actualX-1][actualY] == 1  ||
				worldMatrix[actualX-1][actualY+1] == 1 ||
				worldMatrix[actualX][actualY+1] == 1 ||  
				worldMatrix[actualX+1][actualY+1] == 1 ||
				worldMatrix[actualX+1][actualY] == 1
				)
			{
				worldMatrix[actualX][actualY] = 1;
				floating = false;
				return 1;
			}
			
		}
		if( actualY == worldWidth-1 && actualX > 0 && actualX < (worldHeight-1) ){
			if( 
				worldMatrix[actualX-1][actualY] == 1 ||
				worldMatrix[actualX+1][actualY] == 1 ||
				worldMatrix[actualX+1][actualY-1] == 1 ||
				worldMatrix[actualX][actualY-1] == 1 ||
				worldMatrix[actualX-1][actualY-1] == 1
				)
			{
				worldMatrix[actualX][actualY] = 1;
				floating = false;
				return 1;
			}
		}
		if( actualX == 0 && actualY > 0 && actualY < (worldWidth-1) ){
			if( 
				worldMatrix[actualX][actualY+1] == 1 ||
				worldMatrix[actualX+1][actualY+1] == 1 ||
				worldMatrix[actualX+1][actualY] == 1 ||
				worldMatrix[actualX+1][actualY-1] == 1 ||
				worldMatrix[actualX][actualY-1] == 1
				)
			{
				worldMatrix[actualX][actualY] = 1;
				floating = false;
				return 1;
			}
		}
		if( actualX == worldHeight-1 && actualY > 0 && actualY < (worldWidth-1) ){
			if( 
				worldMatrix[actualX-1][actualY] == 1 ||
				worldMatrix[actualX-1][actualY+1] == 1 ||
				worldMatrix[actualX][actualY+1] == 1 ||
				worldMatrix[actualX][actualY-1] == 1 ||
				worldMatrix[actualX-1][actualY-1] == 1
				)
			{
				worldMatrix[actualX][actualY] = 1;
				floating = false;
				return 1;
			}
		}
		return 0;
	}
	
	public boolean isInsideMovableBoundaries(){
		if( actualX >= 1 && ( actualX <= worldHeight-2 ) && actualY >= 1 && ( actualY <= worldWidth-2 )  ){
			return true;
		}
		else return false;
	}
	
	public boolean isOutsideOfTheWorld(){
		if( actualX < 0 || ( actualX > worldHeight-1 ) || actualY < 0 || ( actualY > worldWidth-1 )  ){
			return true;
		}
		else return false;
	}
	
	public void drawParticle(Graphics g){
		//it looks like that y and x coordinates is swapped in the parameter list
		g.drawRect(actualY, actualX, width, height);
	}
	
	public int getX(){
		return actualX;
	}
	
	public int getY(){
		return actualY;
	}
	
	public boolean isFloating(){
		return floating;
	}
	
	public String toString(){
		return "ParticleId: "+ particleId +" START POS= x:"+startX+","+" y:"+startY + " ACTUAL POS= x:"+actualX+","+" y:"+actualY;
	}
}
