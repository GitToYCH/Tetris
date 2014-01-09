package com.yh.tetris.game;

/**
 * 经典的俄罗斯方块玩法
 * 
 * @author Administrator
 * @version 1.0
 */
public class ClassicTetris extends TetrisBase {

	private Shape currentShape = null;// 当前图形
	private Shape nextShape = null;// 下一个图形
	private int score = 0;// 游戏分数

	private boolean activeLock = false;// 动作锁。方块同一时间只能进行一项动作

	public ClassicTetris() {
		super();
		currentShape = creatShape();
		nextShape = creatShape();
	}

	public Shape getCurrentShape() {
		return currentShape;
	}

	public Shape getNextShape() {
		return nextShape;
	}

	// {{方块控制
	/**
	 * 移动方块
	 * 
	 * @author Administrator
	 * @param direct
	 *            移动的方向
	 * @return
	 */
	public boolean move(int direct) {
		// 锁阻塞
		while (activeLock) {
			System.out.println("移动被旋转阻塞");
		}
		activeLock = true;

		boolean isMove = false;
		switch (direct) {
		case LEFT:
			isMove = moveLeft(currentShape);
			break;
		case RIGHT:
			isMove = moveRight(currentShape);
			break;
		case UP:
			isMove = moveUp(currentShape);
			break;
		case DOWN:
			isMove = moveDown(currentShape);
			break;
		default:
			isMove = false;
		}
		activeLock = false;
		return isMove;
	}

	/**
	 * 旋转当前方块
	 * 
	 * @author Administrator
	 * @return
	 */
	public boolean rotate() {
		// 锁阻塞
		while (activeLock) {
			System.out.println("旋转被移动阻塞");
		}
		activeLock = true;
		boolean i = super.rorateShape(currentShape);
		activeLock = false;
		return i;
	}

	// }}

	// {{游戏逻辑

	public boolean isGameOver() {
		for (int i = 0; i < 10; i++) {
			if (virtualBoard[0][i] == 1)
				return true;
		}
		return false;
	}

	// 获得被消除的行
	public int[] getTrueLines() {
		int[] removeLines = new int[] { -1, -1, -1, -1 };
		int ri = 0;
		for (int i = virtualBoard.length - 1; i >= 0; i--) {
			int j;
			for (j = 0; j < virtualBoard[i].length; j++) {
				if (virtualBoard[i][j] == 1)
					continue;
				else
					break;
			}
			if (j == virtualBoard[i].length) {
				removeLines[ri] = i + ri;
				ri++;
			}
		}
		return removeLines;
	}

	public boolean removeLines(int line) {
		// 判断消除行是否合法
		if (line < 0 || line >= virtualBoard.length)
			return false;
		// 消除行以上的全部下移
		for (int i = line; i > 0; i--)
			for (int j = 0; j < virtualBoard[i].length; j++)
				virtualBoard[i][j] = virtualBoard[i - 1][j];
		// 第一行清0
		for (int j = 0; j < virtualBoard[0].length; j++)
			virtualBoard[0][j] = 0;
		return true;
	}

	public int getScore() {
		return score;
	}

	public int getScore(int removeLineNumber) {
		int baseScore = 25;
		int combo;
		if (removeLineNumber == 0)
			combo = 0;
		else {
			combo = 1;
			for (int i = 0; i < removeLineNumber; i++)
				combo = combo * 2;
		}
		score += combo * baseScore;
		return score;
	}

	// }}

	// {{游戏控制
	public void changeShape() {
		while (activeLock) {
		}
		activeLock = true;
		currentShape = nextShape;
		nextShape = creatShape();
		activeLock = false;
	}
	// }}
}
