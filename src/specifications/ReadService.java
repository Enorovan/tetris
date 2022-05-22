package specifications;

import tools.Tetraminos.TETRAMINOS;

public interface ReadService {
	//Obtenir X.
	public int getX(int index);
	//Obtenir Y.
	public int getY(int index);
	//Obtenir Tetramino.
	public TETRAMINOS getTetramino();
	//Obtenir le plus petit Y.
	public int minY();
}
