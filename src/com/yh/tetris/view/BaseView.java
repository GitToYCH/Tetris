package com.yh.tetris.view;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class BaseView {

	public static final int BASEVIEW_LOADING=0;	//����ģʽ����ɺ��֪ͨ����������
	public static final int BASEVIEW_NORMAL=1;	//��ͨģʽ������֪ͨ����������
	
	public abstract void onDraw(Canvas canvas);
	public abstract void onTouchEvent(MotionEvent event);
	public abstract void onKeyDown(int keyCode, KeyEvent event);
	public abstract void start();
	public abstract void stop();
	public abstract void pause();
	public abstract void resume();
}
