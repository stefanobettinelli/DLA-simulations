/**************************************************************************
  	dla_fractal is a program that implements a model to generate DLA 
  	aggregation of particles. At this stage the model implements 4 types 
  	of particle movements:
  	1) Snow-flake
  	2) Random
  	3) Balistic
  	4) Spiral
  	The program is implemented with the MVC type of architecture.
  	This class is the View.
  	
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
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.game.Mini2DxGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class GameWorldView {
    public void displayWorld(int width, int height, BasicGame game){
    	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "DLA_"+((GameWorldController) game).getMovementType()+"_"+((GameWorldController) game).getParticleNo();
        cfg.useGL20 = true;
        cfg.width = width;
        cfg.height = height;
        cfg.useCPUSynch = false;
        cfg.vSyncEnabled = true;
        cfg.resizable = true;
        new LwjglApplication(new Mini2DxGame(game), cfg);
    }
}
