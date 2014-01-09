package com.yh.tetris.game;

import java.util.Random;

import android.graphics.Point;

public class TetrisBase {

	// 方块行进的方向
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int UP = 3;
	public static final int STAND = 4;

	protected int[][] virtualBoard; // 游戏虚拟棋盘，用来游戏逻辑判断
	private int centerX;// 棋盘中线，用来初始化图形的坐标（令图形总是从中间落下）

	public TetrisBase() {
		virtualBoard = new int[20][10];
		centerX = virtualBoard[0].length / 2;
	}

	public TetrisBase(int[][] board) {
		virtualBoard = board;
		centerX = virtualBoard[0].length / 2;
	}

	// {{创建图形的方法

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

	// {{图形变换的方法
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

	// 旋转方块
	protected boolean rorateShape(Shape shape) {
		// 变换后的图形分布
		Shape newShape = new Shape();
		
		int shiftX = shape.vertex.x;
		int shiftY = shape.vertex.y;

		// 变化虚拟坐标规则：X坐标转变为Y坐标，Y坐标经过运算maxY-Y变为X坐标
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

		// 检查变换后的图形有没有超出边界
		for (int i = 0; i < temp.length; i++) {
			// 如果y超出了下边界，则变换失败
			if (temp[i].y >= virtualBoard.length)
				return false;
			// 如果x超出了右边界，则左移方块
			if (temp[i].x >= virtualBoard[i].length) {
				moveLeft(newShape);
				i = -1;
				continue;
			}
		}
		// 检查变换到位的图形有没有与其他方块冲突
		for (int i = 0; i < temp.length; i++) {
			// 如果该地方已有方块，则变换失败
			if (isExistCube(STAND, newShape)) {
				return false;
			}
		}
		// 变换成功
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
