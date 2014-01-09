package com.yh.tetris.game;

import java.util.Random;

import android.graphics.Point;

public class TetrisBase {

	// �����н��ķ���
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int UP = 3;
	public static final int STAND = 4;

	protected int[][] virtualBoard; // ��Ϸ�������̣�������Ϸ�߼��ж�
	private int centerX;// �������ߣ�������ʼ��ͼ�ε����꣨��ͼ�����Ǵ��м����£�

	public TetrisBase() {
		virtualBoard = new int[20][10];
		centerX = virtualBoard[0].length / 2;
	}

	public TetrisBase(int[][] board) {
		virtualBoard = board;
		centerX = virtualBoard[0].length / 2;
	}

	// {{����ͼ�εķ���

	public Shape creatShape() {
		Random randShape = new Random();

		Shape nextShape = null;
		switch (Math.abs(randShape.nextInt()) % 7) {
		case Shape.S:
			nextShape = creatS();
			break;
		case Shape.Z:
			nextShape = creatZ();
			break;
		case Shape.L:
			nextShape = creatL();
			break;
		case Shape.J:
			nextShape = creatJ();
			break;
		case Shape.I:
			nextShape = creatI();
			break;
		case Shape.O:
			nextShape = creatO();
			break;
		case Shape.T:
			nextShape = creatT();
			break;
		}
		return nextShape;
	}

	public Shape creatS() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX - 1, -1);
		tCoords[1] = new Point(centerX, -1);
		tCoords[2] = new Point(centerX, -2);
		tCoords[3] = new Point(centerX + 1, -2);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -2);
		tShape.width = 3;
		tShape.height = 2;
		tShape.shape = Shape.S;
		return tShape;
	}

	public Shape creatZ() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX - 1, -2);
		tCoords[1] = new Point(centerX, -2);
		tCoords[2] = new Point(centerX, -1);
		tCoords[3] = new Point(centerX + 1, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -2);
		tShape.width = 3;
		tShape.height = 2;
		tShape.shape = Shape.Z;
		return tShape;
	}

	public Shape creatL() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX - 1, -3);
		tCoords[1] = new Point(centerX - 1, -2);
		tCoords[2] = new Point(centerX - 1, -1);
		tCoords[3] = new Point(centerX, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -3);
		tShape.width = 2;
		tShape.height = 3;
		tShape.shape = Shape.L;
		return tShape;
	}

	public Shape creatJ() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX, -3);
		tCoords[1] = new Point(centerX, -2);
		tCoords[2] = new Point(centerX, -1);
		tCoords[3] = new Point(centerX - 1, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -3);
		tShape.width = 2;
		tShape.height = 3;
		tShape.shape = Shape.J;
		return tShape;
	}

	public Shape creatI() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX - 1, -4);
		tCoords[1] = new Point(centerX - 1, -3);
		tCoords[2] = new Point(centerX - 1, -2);
		tCoords[3] = new Point(centerX - 1, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -4);
		tShape.width = 1;
		tShape.height = 4;
		tShape.shape = Shape.I;
		return tShape;
	}

	public Shape creatO() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX - 1, -2);
		tCoords[1] = new Point(centerX, -2);
		tCoords[2] = new Point(centerX - 1, -1);
		tCoords[3] = new Point(centerX, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -2);
		tShape.width = 2;
		tShape.height = 2;
		tShape.shape = Shape.O;
		return tShape;
	}

	public Shape creatT() {
		Shape tShape = new Shape();

		Point[] tCoords = new Point[4];
		tCoords[0] = new Point(centerX, -2);
		tCoords[1] = new Point(centerX - 1, -1);
		tCoords[2] = new Point(centerX, -1);
		tCoords[3] = new Point(centerX + 1, -1);

		tShape.coords = tCoords;
		tShape.vertex = new Point(centerX - 1, -2);
		tShape.width = 3;
		tShape.height = 2;
		tShape.shape = Shape.T;
		return tShape;
	}

	// }}

	// {{ͼ�α任�ķ���
	protected boolean moveLeft(Shape shape) {
		if (shape.vertex.x <= 0 || isExistCube(LEFT, shape))
			return false;
		shape.vertex.x--;
		for (Point p : shape.coords) {
			p.x--;
		}
		return true;
	}

	protected boolean moveRight(Shape shape) {
		if (shape.vertex.x + shape.width >= virtualBoard[0].length
				|| isExistCube(RIGHT, shape))
			return false;
		shape.vertex.x++;
		for (Point p : shape.coords) {
			p.x++;
		}
		return true;
	}

	protected boolean moveDown(Shape shape) {
		if (shape.vertex.y + shape.height >= virtualBoard.length
				|| isExistCube(DOWN, shape)) {
			for (Point p : shape.coords) {
				if (p.y >= 0 && p.y < virtualBoard.length)
					virtualBoard[p.y][p.x] = 1;
			}
			return false;
		}
		shape.vertex.y++;
		for (Point p : shape.coords) {
			p.y++;
		}
		return true;
	}

	protected boolean moveUp(Shape shape) {
		if (shape.vertex.y <= 0 || isExistCube(UP, shape))
			return false;
		shape.vertex.y--;

		for (Point p : shape.coords) {
			p.y--;
		}
		return true;
	}

	// ��ת����
	protected boolean rorateShape(Shape shape) {
		// �任���ͼ�ηֲ�
		Shape newShape = new Shape();
		
		int shiftX = shape.vertex.x;
		int shiftY = shape.vertex.y;

		// �仯�����������X����ת��ΪY���꣬Y���꾭������maxY-Y��ΪX����
		Point[] temp = new Point[shape.coords.length];

		for (int i = 0; i < shape.coords.length; i++) {
			temp[i] = new Point();
			temp[i].y = shape.coords[i].x - shiftX + shiftY;
		}
		int maxY = 0;
		for (int i = 0; i < shape.coords.length; i++) {
			if (shape.coords[i].y > maxY)
				maxY = shape.coords[i].y;
		}
		for (int i = 0; i < shape.coords.length; i++) {
			temp[i].x = maxY - shape.coords[i].y + shiftX;
		}
		newShape.setCoords(temp);

		// ���任���ͼ����û�г����߽�
		for (int i = 0; i < temp.length; i++) {
			// ���y�������±߽磬��任ʧ��
			if (temp[i].y >= virtualBoard.length)
				return false;
			// ���x�������ұ߽磬�����Ʒ���
			if (temp[i].x >= virtualBoard[i].length) {
				moveLeft(newShape);
				i = -1;
				continue;
			}
		}
		// ���任��λ��ͼ����û�������������ͻ
		for (int i = 0; i < temp.length; i++) {
			// ����õط����з��飬��任ʧ��
			if (isExistCube(STAND, newShape)) {
				return false;
			}
		}
		// �任�ɹ�
		shape.coords = newShape.coords;
		shape.vertex = newShape.vertex;
		int t = shape.width;
		shape.width = shape.height;
		shape.height = t;

		return true;
	}

	public boolean isExistCube(int direct, Shape shape) {
		int shiftX = 0, shiftY = 0;
		switch (direct) {
		case LEFT:
			shiftX = -1;
			shiftY = 0;
			break;
		case RIGHT:
			shiftX = 1;
			shiftY = 0;
			break;
		case DOWN:
			shiftX = 0;
			shiftY = 1;
			break;
		case UP:
			shiftX = 0;
			shiftY = -1;
			break;
		case STAND:
			shiftX = 0;
			shiftY = 0;
			break;
		}
		for (Point p : shape.coords) {
			if (p.y + shiftY < 0 || p.y + shiftY >= virtualBoard.length)
				continue;
			if (p.x + shiftX < 0 || p.x + shiftX >= virtualBoard[0].length)
				continue;
			if (virtualBoard[p.y + shiftY][p.x + shiftX] == 1)
				return true;
		}
		return false;
	}
	// }}
}
