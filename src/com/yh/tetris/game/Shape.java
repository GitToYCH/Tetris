package com.yh.tetris.game;

import android.graphics.Point;

public class Shape {
	// ͼ�����ͣ�����7��
	public static final int S = 0;
	public static final int Z = 1;
	public static final int L = 2;
	public static final int J = 3;
	public static final int I = 4;
	public static final int O = 5;
	public static final int T = 6;

	public Point[] coords;// ͼ����ռ����������̵�����
	public Point vertex;// ͼ�ζ������ꡣ��������ƫ����
	public int width, height;// ͼ����ռ���εĿ��
	public int shape; // ͼ����״

	public void setCoords(Point[] p) {
		this.coords = p;
		// ���趥������
		resetVertex();
	}

	/**
	 * ���趥������
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
