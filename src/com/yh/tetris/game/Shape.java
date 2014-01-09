package com.yh.tetris.game;

import android.graphics.Point;

public class Shape {
	// 图形类型，共有7种
	public static final int S = 0;
	public static final int Z = 1;
	public static final int L = 2;
	public static final int J = 3;
	public static final int I = 4;
	public static final int O = 5;
	public static final int T = 6;

	public Point[] coords;// 图像所占的相对于棋盘的坐标
	public Point vertex;// 图形顶点坐标。用来计算偏移量
	public int width, height;// 图形所占矩形的宽高
	public int shape; // 图形形状

	public void setCoords(Point[] p) {
		this.coords = p;
		// 重设顶点坐标
		resetVertex();
	}

	/**
	 * 重设顶点坐标
	 * 
	 * @author Administrator
	 */
	public void resetVertex() {
		int minX = coords[0].x;
		int minY = coords[0].y;
		for (Point tp : coords) {
			if (minX > tp.x)
				minX = tp.x;
			if (minY > tp.y)
				minY = tp.y;
		}
		vertex = new Point(minX, minY);
	}
}
