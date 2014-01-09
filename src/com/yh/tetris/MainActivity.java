package com.yh.tetris;

import com.yh.tetris.view.GameView;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

public class MainActivity extends Activity {
	// ��Ļ�ֱ���
	public static float width, height;
	GameView mGameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ���豸�ķֱ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels; 
		
		mGameView=new GameView(this);
		setContentView(mGameView);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mGameView.onKeyDown(keyCode, event);
	}
	
}
