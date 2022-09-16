package es.sistemasinteligentes.practica1si.desktop;

import AEstrella.Mundo;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.sistemasinteligentes.practica1si.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class DesktopLauncher {
    
	public static void main (String[] arg) {           
           
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();   
            config.width = 1024;
            config.height = 768;                
            new LwjglApplication(new GameMain(), config);    
	}
}
