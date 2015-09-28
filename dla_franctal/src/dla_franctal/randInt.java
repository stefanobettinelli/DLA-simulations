/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is used to generate random values.
  	
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

import java.util.Random;
import java.util.Vector;

public class randInt {
	
	public static int getRandInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    
	    //Vector<Double> prova = new Vector<Double>();

	    return randomNum;
	}

}
