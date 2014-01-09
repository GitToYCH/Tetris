package com.yh.tetris.game;

/**
 * ����Ķ���˹�����淨
 * 
 * @author Administrator
 * @version 1.0
 */
public class ClassicTetris extends TetrisBase {

	private Shape currentShape = null;// ��ǰͼ��
	private Shape nextShape = null;// ��һ��ͼ��
	private int score = 0;// ��Ϸ����

	private boolean activeLock = false;// ������������ͬһʱ��ֻ�ܽ���һ���

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

	// {{�������
	/**
	 * �ƶ�����
	 * 
	 * @author Administrator
	 * @param direct
	 *            �ƶ��ķ���
	 * @return
	 */
	public boolean move(int direct) {
		// ������
		while (activeLock) {
			System.out.println("�ƶ�����ת����");
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
	 * ��ת��ǰ����
	 * 
	 * @author Administrator
	 * @return
	 */
	public boolean rotate() {
		// ������
		while (activeLock) {
			System.out.println("��ת���ƶ�����");
		}
		activeLock = true;
		boolean i = super.rorateShape(currentShape);
		activeLock = false;
		return i;
	}

	// }}

	// {{��Ϸ�߼�

	public boolean isGameOver() {
		for (int i = 0; i < 10; i++) {
			if (virtualBoard[0][i] == 1)
				return true;
		}
		return false;
	}

	// ��ñ���������
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
		// �ж��������Ƿ�Ϸ�
		if (line < 0 || line >= virtualBoard.length)
			return false;
		// ���������ϵ�ȫ������
		for (int i = line; i > 0; i--)
			for (int j = 0; j < virtualBoard[i].length; j++)
				virtualBoard[i][j] = virtualBoard[i - 1][j];
		// ��һ����0
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

	// {{��Ϸ����
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
