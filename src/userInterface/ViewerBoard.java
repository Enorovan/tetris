package userInterface;

import javax.swing.*;

import engine.Engine;
import tools.HardCodedParameters;
import tools.Tetraminos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class ViewerBoard extends JPanel implements ActionListener {
	//HARD CODED PARAMETERS
	private static int BOARD_WIDTH = HardCodedParameters.BOARD_WIDTH;
	private static int BOARD_HEIGHT = HardCodedParameters.BOARD_HEIGHT;
    private static Color colors[] = HardCodedParameters.COLORS;
	
	//Label pour le score.
    private JLabel score;
    
    //Controlleur.
    private Engine engine;

    //Constructeur
    public ViewerBoard(ViewerFrame parent) {
        setFocusable(true);
        engine = new Engine(this);
        score = parent.getScore();
        addKeyListener(new TetrisKeyAdapter());
        setBackground(Color.BLACK);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) { engine.gameAction(); }

    //Démarre le controlleur.
    void start() { engine.start(); }

    //Dessine le board.
    public void paint(Graphics g) {
        super.paint(g);
        engine.paint(g, getSize().getWidth(), getSize().getHeight());
    }

    //Obtenir la largeur et la hauteur d'une case.
    private int getSquareWidth() { return (int) getSize().getWidth() / BOARD_WIDTH; }
    private int getSquareHeight() { return (int) getSize().getHeight() / BOARD_HEIGHT; }

    //Déssiner une case.
    public void drawSquare(Graphics g, int x, int y, Tetraminos.TETRAMINOS tetramino) {
        Color color = colors[tetramino.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, getSquareWidth() - 2, getSquareHeight() - 2);
    }
    
    //Méthode permettant de changer le texte du label score
    public void setScoreText(String text) {
    	score.setText(text);
    	score.setHorizontalAlignment(JLabel.CENTER);
    }

    //Classe privée permettant de binder les touches au jeu.
    private class TetrisKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!engine.isStarted() || engine.isCurrentTetraminoNull()) {
                return;
            }
            
            //Appuyer sur R redémarre le jeu et réinitialise le score            
            if (e.getKeyCode() == KeyEvent.VK_R) {
            	engine.start();
            	setScoreText("0");
            	return;
            }

            //Appuyer sur P pause le jeu
            if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            	engine.pause();
                return;
            }

            if (engine.isPaused())
                return;

            switch (e.getKeyCode()) {
	            case KeyEvent.VK_Q:		//Appuyer sur Q déplace le bloc courant à gauche.
	            	engine.moveLeft();
	                break;
	            case KeyEvent.VK_D:		//Appuyer sur D déplace le bloc courant à droite.
	            	engine.moveRight();
	                break;
	            case KeyEvent.VK_S:		//Appuyer sur S déplace le bloc courant d'une ligne vers le bas en ignorant le tick.
	            	engine.moveDown();
	                break;
	            case KeyEvent.VK_A:		//Appuyer sur A fait pivoter le bloc courant vers la gauche.
	            	engine.rotateLeft();
	                break;
	            case KeyEvent.VK_E:		//Appuyer sur E fait pivoter le bloc courant vers la droite.
	            	engine.rotateRight();
	                break;
	            case KeyEvent.VK_SPACE:	//Appuyer sur ESPACE fait tomber le bloc courant directement en bas de la grille.
	            	engine.dropDown();
	                break;
            }
        }
    }
}
