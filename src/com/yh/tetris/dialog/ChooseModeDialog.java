package com.yh.tetris.dialog;

import com.yh.tetris.MainActivity;
import com.yh.tetris.R;
import com.yh.tetris.util.ImageUtil;
import com.yh.tetris.view.GameView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class ChooseModeDialog extends BaseDialog {

	Bitmap frame;// 外层框架
	Bitmap[] bt_classic;// 经典模式
	Bitmap[] bt_race;// 竞速模式
	Bitmap[] bt_build;// 建造模式
	Bitmap classic;
	Bitmap race;
	Bitmap build;
	RectF rt_frame;
	RectF rt_classic;
	RectF rt_race;
	RectF rt_build;

	@SuppressLint("WrongCall")
	public ChooseModeDialog(Context mContext) {
		super(mContext);
		initBitmap();
		float x = MainActivity.width / 2 - frame.getWidth() / 2;
		float y = MainActivity.height / 2 - frame.getHeight() / 2;
		rt_frame = new RectF(x, y, x + frame.getWidth(), y + frame.getHeight());
		float rt_x = frame.getWidth() / 2 - bt_classic[0].getWidth() / 2 + x;
		float rt_y = frame.getHeight() / 4 + y;
		int bt_width = bt_classic[0].getWidth();
		int bt_height = bt_classic[0].getHeight();
		rt_classic = new RectF(rt_x, rt_y, rt_x + bt_width, rt_y + bt_height);
		rt_y = rt_y + (float) (frame.getHeight() * 0.185);
		rt_race = new RectF(rt_x, rt_y, rt_x + bt_width, rt_y + bt_height);
		rt_y = rt_y + (float) (frame.getHeight() * 0.185);
		rt_build = new RectF(rt_x, rt_y, rt_x + bt_width, rt_y + bt_height);

		setDialogRectF(rt_frame);
		onDraw(null);
	}

	private void initBitmap() {
		Bitmap tframe = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.frame_choosemode);
		frame = ImageUtil.resizeImage(tframe, (4 * MainActivity.width) / 5,
				ImageUtil.WIDTH_HEIGHT);
		Bitmap tclassic = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bt_chooseclassic);
		Bitmap tbuild = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bt_choosebuild);
		Bitmap trace = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bt_chooserace);
		float bt_width = (float) (frame.getWidth() * 0.91);
		bt_classic = ImageUtil.inciseImage(ImageUtil.resizeImage(tclassic,
				bt_width, ImageUtil.WIDTH_HEIGHT), 2, 1);
		bt_race = ImageUtil.inciseImage(
				ImageUtil.resizeImage(trace, bt_width, ImageUtil.WIDTH_HEIGHT),
				2, 1);
		bt_build = ImageUtil
				.inciseImage(ImageUtil.resizeImage(tbuild, bt_width,
						ImageUtil.WIDTH_HEIGHT), 2, 1);
		classic = bt_classic[0];
		race = bt_race[0];
		build = bt_build[0];
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Canvas mCv = new Canvas(dialogBlock);
		mCv.drawBitmap(frame, rt_frame.left - dialogRectF.left, rt_frame.top
				- dialogRectF.top, paint);
		mCv.drawBitmap(classic, rt_classic.left - dialogRectF.left,
				rt_classic.top - dialogRectF.top, paint);
		mCv.drawBitmap(race, rt_race.left - dialogRectF.left, rt_race.top
				- dialogRectF.top, paint);
		mCv.drawBitmap(build, rt_build.left - dialogRectF.left, rt_build.top
				- dialogRectF.top, paint);
		mCv.save(Canvas.ALL_SAVE_FLAG);
		mCv.restore();
//		if(canvas!=null)
//			canvas.drawBitmap(dialogBlock, dialogLocat.x, dialogLocat.y, paint);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		GameView.VIEW.addDialog(this);
		super.show();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		// super.close();
		frame = null;
		classic = null;
		race = null;
		build = null;
		bt_classic = null;
		bt_race = null;
		bt_build = null;
		GameView.VIEW.removeDialog();
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!rt_frame.contains(x, y)) {
				close();
			} else if (rt_classic.contains(x, y)) {
				classic = bt_classic[1];
			} else if (rt_race.contains(x, y)) {
				race = bt_race[1];
			} else if (rt_build.contains(x, y)) {
				build = bt_build[1];
			}
			break;
		case MotionEvent.ACTION_UP:
			if (listener != null) {
				if (rt_classic.contains(x, y) && classic == bt_classic[1]) {
					listener.onResult(1);
				} else if (rt_race.contains(x, y) && race == bt_race[1]) {
					listener.onResult(2);
				} else if (rt_build.contains(x, y) && build == bt_build[1]) {
					listener.onResult(3);
				}
			}
			classic = bt_classic[0];
			race = bt_race[0];
			build = bt_build[0];
			break;
		}
	}

	@Override
	public RectF getDialogRectF() {
		// TODO Auto-generated method stub
		return rt_frame;
	}

}
