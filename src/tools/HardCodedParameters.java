package tools;

import java.awt.Color;

public class HardCodedParameters {
	public static final int LINE_LENGTH = 4;
	public static final int COLUMN_LENGTH = 2;
	public static final String WINDOW_NAME = "Tetris";
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 800;
	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 22;
	public static final String ICON_PATH = "src/images/tetris.png";
	public static final String BGM_PATH = "src/sounds/tetris.wav";
	public static Color COLORS[] = {
        new Color(0, 0, 0),			//None: White
        new Color(255, 0, 0),		//Z: Red
    	new Color(0, 255, 0),		//S: Green
    	new Color(0, 255, 255),		//I: Cyan
    	new Color(148, 0, 211),		//T: Purple
    	new Color(255, 255, 0),		//O: Yellow
    	new Color(255, 140, 0),		//L: Orange
    	new Color(0, 0, 255),		//J: Blue
     };
	public static int DELAY[] = {
        400,	//Level 1
        200		//Level 2
	};
}
