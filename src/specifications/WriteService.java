package specifications;

import tools.Tetraminos.TETRAMINOS;

public interface WriteService {
	//Set X.
	public void setX(int index, int x);
	//Set Y.
	public void setY(int index, int y);
	//Set un Tetramino dont la forme est aléatoire.
	public void setRandomTetramino();
	//Set un Tetramino avec un type définit
	public void setTetramino(TETRAMINOS shape);
}
