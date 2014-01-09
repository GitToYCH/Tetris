package com.yh.tetris.view;

import com.yh.tetris.MainActivity;
import com.yh.tetris.dialog.BaseDialog;
import com.yh.tetris.util.ImageUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements Callback {

	public static final int EXITGAME = 0;
	public static final int TETEISVIEW = 1;
	public static final int HOMEVIEW = 2;
	public static final int PAUSEVIEW = 3;
	public static final int LOADING_COMPLETE = 4;

	RefreshThread rfthread;
	BaseView mBaseView;
	BaseView nextView;
	MainActivity activity;
	private BaseDialog dialog;
	public LoadingView load;
	private Bitmap screenshot;
	private Bitmap background;
	public static GameView VIEW;

	private Paint paint;
	@SuppressLint("HandlerLeak")
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TETEISVIEW:
				rfthread.pause = true;
				if (mBaseView != null)
					mBaseView.stop();
				mBaseView = load;
				rfthread.pause = false;
				mBaseView.start();
				new Thread() {// 后台加载
					public void run() {
						Looper.prepare();
						nextView = new RecycleModelView(GameView.this,
								BaseView.BASEVIEW_LOADING);
						Looper.loop();
					}
				}.start();// 启动线程
				break;
			case HOMEVIEW:
				rfthread.pause = true;
				if (mBaseView != null)
					mBaseView.stop();
				mBaseView = load;
				rfthread.pause = false;
				mBaseView.start();
				new Thread() {// 后台加载
					public void run() {
						Looper.prepare();
						nextView = new HomeView(GameView.this);
						Looper.loop();
					}
				}.start();
				break;
			case LOADING_COMPLETE:
				rfthread.pause = true;
				if (mBaseView != null)
					mBaseView.stop();
				mBaseView = nextView;
				rfthread.pause = false;
				mBaseView.start();
				break;
			case EXITGAME:
				rfthread.run = false;
				mBaseView.stop();
				activity.finish();
				break;
			}
		}
	};

	public GameView(Context context) {
		super(context);
	}

	public GameView(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub

		load = new LoadingView(this);
		this.activity = activity;

		paint = new Paint();
		paint.setAntiAlias(true);
		screenshot = Bitmap.createBitmap((int)MainActivity.width,
				(int)MainActivity.height, Config.ARGB_8888);
		myHandler.sendEmptyMessage(HOMEVIEW);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		rfthread = new RefreshThread(holder);

		setFocusable(true);
		VIEW = this;
	}

	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas) {
		if (dialog != null) {
			canvas.drawBitmap(background, 0, 0, paint);
			dialog.onDraw(canvas);
		} else
			mBaseView.onDraw(canvas);
	}

	public void addDialog(BaseDialog bd) {
		mBaseView.pause();
		background = ImageUtil.copyImage(screenshot);
		dialog = bd;
	}

	public void removeDialog() {
		dialog = null;
		background=null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		rfthread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		rfthread.run = false;
		mBaseView.stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.onTouchEvent(event);
		} else
			mBaseView.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.close();
		} else
			mBaseView.onKeyDown(keyCode, event);
		return true;
	}

	class RefreshThread extends Thread {
		SurfaceHolder surface;
		boolean run = true;
		boolean pause = false;

		public RefreshThread(SurfaceHolder surface) {
			this.surface = surface;
		}

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Canvas canvas = new Canvas();
			canvas.setBitmap(screenshot);
			while (run) {
				while (!pause) {
					try {
						canvas = surface.lockCanvas();
						onDraw(canvas);
//						Thread.sleep(200);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							surface.unlockCanvasAndPost(canvas);
						}
					}
				}
				super.run();
			}
		}
	}
}