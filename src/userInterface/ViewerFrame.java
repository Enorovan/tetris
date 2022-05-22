package userInterface;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import tools.HardCodedParameters;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class ViewerFrame extends JFrame {
	//HARD CODED PARAMETERS
	private static final String WINDOW_NAME = HardCodedParameters.WINDOW_NAME;
	private static final int WINDOW_WIDTH = HardCodedParameters.WINDOW_WIDTH;
	private static final int WINDOW_HEIGHT = HardCodedParameters.WINDOW_HEIGHT;
	private static final String ICON_PATH = HardCodedParameters.ICON_PATH;
	private static final String BGM_PATH = HardCodedParameters.BGM_PATH;
	
    private JLabel score;
    private ViewerBoard board;

    public ViewerFrame() {
    	score = new JLabel("0");
    	score.setHorizontalAlignment(JLabel.CENTER);
        board = new ViewerBoard(this);
    }

    public void init() {
        setLayout(new BorderLayout());
        //Indique aux éléments swing où se placer.
        add(score, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        //Démarre.
        board.start();
        //Indique les dimensions de la fenêtre.
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        //Indique le titre de la fenêtre
        setTitle(WINDOW_NAME);
        //Paramètres
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(true);
        
        //Change l'icone de la fenêtre.
        ImageIcon icon = new ImageIcon(ICON_PATH);
        setIconImage(icon.getImage());
        
        //Permet de mettre la musique de fond de Tetris et de la faire boucler.
        try {
        	//Obtention du fichier et conversion en stream audio.
        	File soundFile = new File(BGM_PATH); 
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);              
            Clip clip = AudioSystem.getClip();
            
            //Ouverture du fichier.
            clip.open(audioIn);
            //Diminution du son.
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-60.0f);
            //Faire boucler la musique
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //Jouer la musique.
            clip.start();
            
         } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (LineUnavailableException e) {
            e.printStackTrace();
         }
    }

    //Retourne le score.
    public JLabel getScore() {
        return score;
    }
}