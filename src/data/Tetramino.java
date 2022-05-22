package data;

import java.util.Random;

import specifications.DataService;
import tools.HardCodedParameters;
import tools.Tetraminos;

public class Tetramino implements DataService {
	//HARD CODED PARAMETERS
    private static int LINE_LENGTH = HardCodedParameters.LINE_LENGTH;
	private static int COLUMN_LENGTH = HardCodedParameters.COLUMN_LENGTH;

	//Type des tetramino
    private Tetraminos.TETRAMINOS tetramino;
    //Coordonnées
	private int coordinates[][];
	//Forme mathématiques des Tetraminos
	private int[][][] coordinatesArray;

    public Tetramino() {
    	coordinates = new int[LINE_LENGTH][COLUMN_LENGTH];
		coordinatesArray = new int[][][] {
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},		//None
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},	//Z
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},		//S
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},		//I
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},		//T
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},		//O
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},	//L
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}		//J
        };
        setTetramino(Tetraminos.TETRAMINOS.None);
    }

    @Override
    public int getX(int index) {
        return coordinates[index][0];
    }

	@Override
    public int getY(int index) {
        return coordinates[index][1];
    }

	@Override
    public Tetraminos.TETRAMINOS getTetramino() {
        return tetramino;
    }

	@Override
    public int minY() {
        int min = coordinates[0][1];
        for (int i = 0; i < LINE_LENGTH; i++) {
            min = Math.min(min, coordinates[i][1]);
        }
        return min;
    }
    
	@Override
    public void setX(int index, int x) {
    	coordinates[index][0] = x;
	}
   
	@Override
	public void setY(int index, int y) {
    	coordinates[index][1] = y;
    }

	@Override
    public void setRandomTetramino() {
        Random alea = new Random();
        int type = Math.abs(alea.nextInt()) % 7 + 1;
        Tetraminos.TETRAMINOS[] values = Tetraminos.TETRAMINOS.values();
        setTetramino(values[type]);
    }
	
	@Override
    public void setTetramino(Tetraminos.TETRAMINOS tetramino) {
        for (int i = 0; i < LINE_LENGTH ; i++) {
            System.arraycopy(coordinatesArray[tetramino.ordinal()][i], 0, this.coordinates[i], 0, COLUMN_LENGTH);
        }
        this.tetramino = tetramino;
    }
	
	//Le tetramino pivote vers la gauche.
    public Tetramino rotateLeft() {
        if (tetramino == Tetraminos.TETRAMINOS.O) return this;

        Tetramino result = new Tetramino();
        result.tetramino = tetramino;

        for (int i = 0; i < LINE_LENGTH; ++i) {
            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }
        return result;
    }

  //Le tetramino pivote vers la droite.
    public Tetramino rotateRight() {
        if (tetramino == Tetraminos.TETRAMINOS.O) return this;

        Tetramino result = new Tetramino();
        result.tetramino = tetramino;

        for (int i = 0; i < LINE_LENGTH; ++i) {
            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }
        return result;
    }
}
