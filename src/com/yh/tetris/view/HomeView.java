package com.yh.tetris.view;

import com.yh.tetris.MainActivity;
import com.yh.tetris.R;
import com.yh.tetris.dialog.BaseDialog.OnResultListener;
import com.yh.tetris.dialog.ChooseModeDialog;
import com.yh.tetris.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class HomeView extends BaseView {

	private Paint paint;

	private Context mContext;
	private Bitmap[] startGame = null;// 开始按钮
	private Bitmap background = null;
	private Rect startRect;// 开始按钮所处的矩形框
	private int index = 0;// 显示的开始按钮的图片

	private HomeThread homeThread;
	private GameView game;

	public HomeView(GameView game) {
		this.mContext = game.getContext();
		this.game = game;
		initialize();
		if (game.load != null)
			game.load.notifyLoadComplete();
	}

	private void initialize() {
		homeThread = new HomeThread();
		paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap startRes = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.startgame);
		startRes = ImageUtil.resizeImage(startRes, MainActivity.width,
				ImageUtil.WIDTH_HEIGHT);
		startGame = ImageUtil.inciseImage(startRes, 2, 1);
		
		Bitmap bgRes = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bacground_a);
		background = ImageUtil.resizeImage(bgRes, MainActivity.width,
				MainActivity.height);
		
		startRect = new Rect();
		startRect.left = (int)MainActivity.width / 2 - startGame[0].getWidth() / 2;
		startRect.top = (int)MainActivity.height / 2 - startGame[0].getHeight() / 2;
		startRect.right = startRect.left + startGame[0].getWidth();
		startRect.bottom = startRect.top + startGame[0].getHeight();
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(background, 0, 0, paint);
		canvas.drawBitmap(startGame[index], startRect.left, startRect.top,
				paint);
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (startRect.contains((int) event.getX(), (int) event.getY())) {
			ChooseModeDialog cmd=new ChooseModeDialog(mContext);
			cmd.setOnResultListener(new OnResultListener() {
				@Override
				public void onResult(int result) {
					// TODO Auto-generated method stub
					if(result==1){
						GameView.VIEW.removeDialog();
						game.myHandler.sendEmptyMessage(GameView.TETEISVIEW);
					}
				}
			});
			cmd.show();
		}
	}

	@Override
	public void onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			game.myHandler.sendEmptyMessage(GameView.EXITGAME);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		homeThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		homeThread.run = false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	class HomeThread extends Thread {
		boolean run = true;
		int times = 1;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (run) {
				index = Math.abs(times % 2);
				times++;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		}

	}
}
