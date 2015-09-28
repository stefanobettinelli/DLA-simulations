/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is the staring point of the program and contains the main method.
  	
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.jfree.ui.RefineryUtilities;

//import org.jfree.ui.RefineryUtilities;

public class MVCGameWorld {
	
	public static void main(String[] args){
		
		//to get user input
		Scanner userInput = new Scanner(System.in);
		int movementType = -1;
		int particleNo = 0;
		boolean analysisPhase = false;
		boolean isInt;
		Map<Integer,Integer> snowFlakeIterations = new HashMap<Integer,Integer>();
		Map<Integer,Integer> randomIterations = new HashMap<Integer,Integer>();
		Map<Integer,Integer> balisticIterations = new HashMap<Integer,Integer>();
		Map<Integer,Integer> spiralIterations = new HashMap<Integer,Integer>();
		
		//some code for reading the simulations file to generate the particles/iterations graph
		File simulationFile = new File("./particlesIterations.txt");
		if( analysisPhase ){
		try {
			BufferedReader getSimulation = new BufferedReader(new FileReader(simulationFile));
			String simulationString = getSimulation.readLine();
			while(simulationString != null){
				String[] splitSimu = simulationString.split(" ");
				int simType = (new Integer(splitSimu[0])).intValue();
				int simIterations = (new Integer(splitSimu[1])).intValue();
				int simParticleNumber = (new Integer(splitSimu[2])).intValue();
				switch ( simType ) {
				case 0:
					snowFlakeIterations.put(new Integer(simParticleNumber), new Integer(simIterations));
					break;
				case 1:
					randomIterations.put(new Integer(simParticleNumber), new Integer(simIterations));
					break;
				case 2:
					balisticIterations.put(new Integer(simParticleNumber), new Integer(simIterations));
					break;
				case 3:
					spiralIterations.put(new Integer(simParticleNumber), new Integer(simIterations));
					break;
				}
				simulationString = getSimulation.readLine();
			}
			getSimulation.close();
			final LineChart dlaLineChartBBAreaRation = new LineChart("simulationLineCharts",snowFlakeIterations,randomIterations,balisticIterations,spiralIterations);
    		dlaLineChartBBAreaRation.pack();
    		RefineryUtilities.centerFrameOnScreen(dlaLineChartBBAreaRation);
    		dlaLineChartBBAreaRation.setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		//end of graph generation...
		
		for (String string : args) {
			System.out.println(string);
		}
		
		do{
			System.out.print("[0] snow-flake \n[1] random \n[2] random straight balistic \n[3] Square spiral: ");
			isInt = userInput.hasNextInt();
			if( isInt == true )
				movementType = userInput.nextInt();
			else userInput.next();
		}while( movementType != 0 && movementType != 1 && movementType != 2 && movementType != 3 );
		
		do{
			System.out.print("Please give me the number of floating particles ( >= 1 && <= 100000): ");
			isInt = userInput.hasNextInt();
			if( isInt == true )
				particleNo = userInput.nextInt();
			else userInput.next();
		}while( particleNo < 1 || particleNo > 100000 );
		
		switch(movementType){
		case 0: System.out.println("Snow-flake have been chosen");
			break;
		case 1: System.out.println("Random have been chosen");
			break;
		case 2: System.out.println("Random straight balistic have been chosen");
			break;
		case 3: System.out.println("Square spiral have been choosen");
			break;
		}
		
		/**
		 * MVC model initialization
		 */
		GameWorldView theView = new GameWorldView();
		GameWorldModel theModel = new GameWorldModel(800, 600, particleNo, movementType);
		GameWorldController theController = new GameWorldController(theView, theModel, movementType);

		
		//finally the controller starts all the simulation
		theController.start();
	}

}
