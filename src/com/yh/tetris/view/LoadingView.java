package com.yh.tetris.view;

import com.yh.tetris.MainActivity;
import com.yh.tetris.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class LoadingView extends BaseView{
	
	private Paint paint;
	private Bitmap loading;
	private GameView game;
	private Context context;
	public LoadingView(GameView game){
		this.game=game;
		this.context=game.getContext();
		paint=new Paint();
		paint.setAntiAlias(true);
		loading=BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
	}
	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(loading, MainActivity.width/2-loading.getWidth()/2, 3*MainActivity.height/4, paint);
		paint.setColor(Color.WHITE);
	}
	//通知加载完成
	public void notifyLoadComplete(){
		stop();
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					//故意延迟半秒
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.myHandler.sendEmptyMessage(GameView.LOADING_COMPLETE);
				Looper.loop();
			}
		}.start();
	}
	
	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
