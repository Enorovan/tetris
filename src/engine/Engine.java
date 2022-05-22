package engine;

import javax.swing.*;

import data.Tetramino;
import tools.HardCodedParameters;
import tools.Tetraminos;
import userInterface.ViewerBoard;

import java.awt.*;

public class Engine {
	//HARD CODED PARAMETERS
	private static int LINE_LENGTH = HardCodedParameters.LINE_LENGTH;
	private static int BOARD_WIDTH = HardCodedParameters.BOARD_WIDTH;
	private static int BOARD_HEIGHT = HardCodedParameters.BOARD_HEIGHT;
	private static final int[] DELAY = HardCodedParameters.DELAY;
	
	//Vue
    private ViewerBoard tetrisBoard;
    
    //Booléens
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    
    //Tick du moteur
    private Timer timer;

    //Tetramino courant.
    private Tetramino currentTetramino;
    private int currentX = 0;
    private int currentY = 0;
    //Plateau de Tetraminos.
    private Tetraminos.TETRAMINOS[] board;
    //Nombre de lignes supprimée d'un coup
    private int removedLinesCount = 0;

    public Engine(ViewerBoard tetrisBoard) {
        this.tetrisBoard = tetrisBoard;
        currentTetramino = new Tetramino();
        timer = new Timer(DELAY[0], tetrisBoard);
        timer.start();
        board = new Tetraminos.TETRAMINOS[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
    }

    public void gameAction() {
        if (isFallingFinished) {
            isFallingFinished = false;
            newTetramino();
        } else {
            moveDown();
        }
    }

    //Vérifie si le jeu a commencé, est en pause, ou si le Tetramino courant possède un type jouable.
    public boolean isStarted() { return isStarted; }
    public boolean isPaused() { return isPaused; }
    public boolean isCurrentTetraminoNull() { return currentTetramino.getTetramino() == Tetraminos.TETRAMINOS.None; }

    //(Re)démarre le jeu.
    public void start() {
        if (isPaused) return;
        isStarted = true;
        isFallingFinished = false;
        setRemovedLinesCount(0);
        clearBoard();
        newTetramino();
        timer.setDelay(DELAY[0]);
        timer.restart();
    }

    //Met le jeu en pause
    public void pause() {
        if (!isStarted) return;

        isPaused = !isPaused;
        
        if (isPaused) {
            timer.stop();
            tetrisBoard.setScoreText("PAUSED");
        } else {
            timer.start();
            tetrisBoard.setScoreText(String.valueOf(getRemovedLinesCount()));
        }
        tetrisBoard.repaint();
    }
    
    //Dessine le board.
    public void paint(Graphics g, double width, double height) {
        int squareWidth = (int) width / BOARD_WIDTH;
        int squareHeight = (int) height / BOARD_HEIGHT;
        int boardTop = (int) height - BOARD_HEIGHT * squareHeight;


        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int j = 0; j < BOARD_WIDTH; ++j) {
            	Tetraminos.TETRAMINOS shape = tetraminoAtCoordinates(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetraminos.TETRAMINOS.None) tetrisBoard.drawSquare(g, j * squareWidth, boardTop + i * squareHeight, shape);
            }
        }

        if (currentTetramino.getTetramino() != Tetraminos.TETRAMINOS.None) {
            for (int i = 0; i < LINE_LENGTH; ++i) {
                int x = currentX + currentTetramino.getX(i);
                int y = currentY - currentTetramino.getY(i);
                tetrisBoard.drawSquare(g, x * squareWidth, boardTop + (BOARD_HEIGHT - y - 1) * squareHeight, currentTetramino.getTetramino());
            }
        }
    }

    //Nettoie le board.
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i) board[i] = Tetraminos.TETRAMINOS.None;
    }
    
    //Créer un nouveau Tetramino, si il est impossible de le déplacer, c'est qu'il est au sommet, le jeu s'arrête.
    private void newTetramino() {
    	currentTetramino.setRandomTetramino();
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + currentTetramino.minY();

        if (!tryMove(currentTetramino, currentX, currentY)) {
        	currentTetramino.setTetramino(Tetraminos.TETRAMINOS.None);
            timer.stop();
            isStarted = false;
            tetrisBoard.setScoreText("GAME OVER");
        }
    }

    //Renvoie le Tetramino au coordonnées indiquées.
    private Tetraminos.TETRAMINOS tetraminoAtCoordinates(int x, int y) { return board[(y * BOARD_WIDTH) + x]; }

    //Vérifie si l'on peut déplacer un Tetramino.
    private boolean tryMove(Tetramino newPiece, int newX, int newY) {
        for (int i = 0; i < LINE_LENGTH; ++i) {
            int x = newX + newPiece.getX(i);
            int y = newY - newPiece.getY(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) return false;
            if (tetraminoAtCoordinates(x, y) != Tetraminos.TETRAMINOS.None)  return false;
        }

        currentTetramino = newPiece;
        currentX = newX;
        currentY = newY;
        tetrisBoard.repaint();
        return true;
    }
    
    //Envoyer le tétramino directement en bas.
    public void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentTetramino, currentX, newY - 1))
                break;
            newY--;
        }
        tetraminoDropped();
    }

    //Fait tomber un nouveau Tetramino si le précédent est tombé
    private void tetraminoDropped() {
        for (int i = 0; i < LINE_LENGTH; ++i) {
            int x = currentX + currentTetramino.getX(i);
            int y = currentY - currentTetramino.getY(i);
            board[(y * BOARD_WIDTH) + x] = currentTetramino.getTetramino();
        }

        removeFullLines();

        if (!isFallingFinished) newTetramino();
    }

    //Méthode permettant de retirer les lignes pleines
    private void removeFullLines() {
        int fullLinesCount = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (tetraminoAtCoordinates(j, i) == Tetraminos.TETRAMINOS.None) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
            	fullLinesCount++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++)
                    for (int j = 0; j < BOARD_WIDTH; j++)
                        board[(k * BOARD_WIDTH) + j] = tetraminoAtCoordinates(j, k + 1);
            }
        }

        //Incrémente le compteur.
        if (fullLinesCount > 0) {
        	setRemovedLinesCount(getRemovedLinesCount() + fullLinesCount);
            tetrisBoard.setScoreText(String.valueOf(getRemovedLinesCount()));
            isFallingFinished = true;
            currentTetramino.setTetramino(Tetraminos.TETRAMINOS.None);
            tetrisBoard.repaint();
        //Si le score est au dessus de 5, la vitesse augmente.
        } if (getRemovedLinesCount() >= 5) {
        	timer.setDelay(DELAY[1]);
        	timer.restart();
        }
    }

    //Méthodes de déplacement/rotation
    public void moveLeft() { tryMove(currentTetramino, currentX - 1, currentY); }
    public void moveRight() { tryMove(currentTetramino, currentX + 1, currentY); }
    public void moveDown() { if (!tryMove(currentTetramino, currentX, currentY - 1)) tetraminoDropped(); }
    public void rotateLeft() { tryMove(currentTetramino.rotateLeft(), currentX, currentY); }
    public void rotateRight() { tryMove(currentTetramino.rotateRight(), currentX, currentY); }

	public int getRemovedLinesCount() {
		return removedLinesCount;
	}

	public void setRemovedLinesCount(int removedLinesCount) {
		this.removedLinesCount = removedLinesCount;
	}
}
