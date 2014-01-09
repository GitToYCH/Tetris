package com.yh.tetris.view;

import com.yh.tetris.R;
import com.yh.tetris.util.ImageUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class RecycleModelView extends BaseView {
	TetrisView mTetrisOne;
	TetrisView mTetrisTwo;
	BaseView currentView;
	BaseView secondView;

	private Bitmap recyleContainer = null;// 垃圾桶的背景
	private RectF recyleRect;// 垃圾桶所在的矩形
	private Bitmap thumbnail = null;// 缩略图
	private boolean isChangeView; // 是否显示画面切换动画
	private Bitmap changeBitmap;// 过场动画
	private float cbmWidth, cbmHeight;
	private float cbmX, cbmY;

	private Context mContext;
	private GameView game;
	private float baseWidth;// 游戏的基本长度，为适应不同分辨率的手机
	private Paint paint;

	public RecycleModelView(GameView game, int mode) {
		this.mContext = game.getContext();
		this.game = game;

		mTetrisOne = new TetrisView(game, BaseView.BASEVIEW_NORMAL);
		mTetrisTwo = new TetrisView(game, BaseView.BASEVIEW_NORMAL);
		this.baseWidth = mTetrisOne.getBaseWidth();

		currentView = mTetrisOne;
		secondView = mTetrisTwo;

		isChangeView = false;
		cbmWidth = 10 * baseWidth;
		cbmHeight = 20 * baseWidth;
		cbmX = ((TetrisView) currentView).containerRect.left;
		cbmY = ((TetrisView) currentView).containerRect.top;

		initBitmap();

		if (this.game != null && mode == BASEVIEW_LOADING)
			game.load.notifyLoadComplete();
	}

	private void initBitmap() {

		paint = new Paint();
		paint.setAntiAlias(true);

		recyleContainer = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.nextcontainer);
		recyleContainer = ImageUtil.resizeImage(recyleContainer,
				(float) (baseWidth * 4.5), (float) (baseWidth * 9));
		recyleRect = new RectF((float) (baseWidth * 11.5),
				(float) (baseWidth * 11.5), (float) (baseWidth * 16),
				(float) (baseWidth * 20.5));

		thumbnail = ImageUtil.resizeImage(
				((TetrisView) secondView).containerShape,
				(float) (baseWidth * 3.5), (float) (baseWidth * 7));
	}

	@SuppressLint("WrongCall")
	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (isChangeView) {
			canvas.drawARGB(255, 236, 145, 81);
			canvas.drawBitmap(recyleContainer, recyleRect.left, recyleRect.top,
					paint);
			canvas.drawBitmap(changeBitmap, cbmX, cbmY, paint);
		} else {
			currentView.onDraw(canvas);
			canvas.drawBitmap(recyleContainer, recyleRect.left, recyleRect.top,
					paint);
			canvas.drawBitmap(thumbnail, (float) (baseWidth * 12),
					(float) (baseWidth * 12.5), paint);
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& recyleRect.contains(event.getX(), event.getY())) {
			currentView.pause();
			new ChangeThread().start();
			isChangeView = true;
		} else
			currentView.onTouchEvent(event);
	}

	@Override
	public void onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		currentView.onKeyDown(keyCode, event);
	}

	public void changeView() {
		thumbnail = ImageUtil.resizeImage(
				((TetrisView) currentView).containerShape,
				(float) (baseWidth * 3.5), (float) (baseWidth * 7));
		BaseView tBaseView = currentView;
		currentView = secondView;
		secondView = tBaseView;
		currentView.resume();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		currentView.start();
		secondView.start();
		secondView.pause();
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

	class ChangeThread extends Thread {
		boolean run = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (run) {
				try {
					cbmWidth =cbmWidth- baseWidth/4;
					cbmHeight = cbmHeight - baseWidth/2;
					if (cbmWidth >baseWidth * 3.5){
						changeBitmap = ImageUtil.resizeImage(
								((TetrisView) currentView).containerShape,
								cbmWidth, cbmHeight);
						cbmX = cbmX + 11 * baseWidth / 24;
						cbmY = cbmY + 71 * baseWidth / 144;
					} else {
						changeView();
						isChangeView = false;
						cbmWidth = 10 * baseWidth;
						cbmHeight = 20 * baseWidth;
						cbmX = ((TetrisView) currentView).containerRect.left;
						cbmY = ((TetrisView) currentView).containerRect.top;
						break;
					}
					Thread.sleep(25);
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.run();
			}
		}
	}
}
