package com.yh.tetris.view;

import com.yh.tetris.MainActivity;
import com.yh.tetris.R;
import com.yh.tetris.dialog.ChooseModeDialog;
import com.yh.tetris.dialog.BaseDialog.OnResultListener;
import com.yh.tetris.game.ClassicTetris;
import com.yh.tetris.game.Shape;
import com.yh.tetris.game.TetrisBase;
import com.yh.tetris.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class TetrisView extends BaseView {

	// ��Ϸ״̬
	private static final int GAMEOVER = 0;
	private static final int GAMEPAUSE = 1;
	private static final int GAMERUNNING = 2;

	private Paint paint;
	private Bitmap background = null;
	private Bitmap tetrisShape = null; // ��ǰ��ͼ�ε�ͼƬ
	private Bitmap nextTetrisShape = null; // ��һ��ͼ�ε�ͼƬ
	private Bitmap nextTS_resize = null; // �����ŵ���һ��ͼ�ε�ͼƬ����������ʾ����ʾ
	public Bitmap containerShape = null; // �����ڵ�ͼ�Ρ���ʾ��Ϸ����
	private Bitmap scoreBmp = null; // �÷�
	private Bitmap numberRes = null;// ���ֵ�ͼƬ
	private Bitmap container = null; // �����ı���
	private Bitmap nextContainer = null;// ��һ������������ı���
	private Bitmap[] colorsCube = null; // �������飬����˸�ɫ�ķ���
	private Bitmap gameOver = null;// ��Ϸ����
	private float x; // ��ǰX���ꡣ���ֵ�������Ļ�ľ�������
	private float y; // ��ǰY���ꡣ���ֵ�������Ļ�ľ�������
	public RectF containerRect; // �����ı߿�
	private ShapeThread shapeThread; // ��Ϸ�߳�
	private int gameState;// ��Ϸ״̬
	private boolean isMoveLeft = false, isMoveRight = false, isMoveLR = false;// �ƶ�ͼ��
	private MoveLRThread moveLRThread;// �����ƶ����߳�

	private float baseWidth;// ��Ϸ�Ļ������ȣ�Ϊ��Ӧ��ͬ�ֱ��ʵ��ֻ�
	private Context mContext;
	private GameView game;
	private ClassicTetris tetris;// ��Ϸҵ���߼�

	Point[] p;// ������

	public TetrisView(GameView game, int mode) {
		this.mContext = game.getContext();
		this.game = game;
		this.tetris = new ClassicTetris();
		initBitmap();

		this.containerRect = new RectF(baseWidth, 2 * baseWidth / 3,
				baseWidth * 11, (2 * baseWidth / 3) + baseWidth * 20);

		this.shapeThread = new ShapeThread();
		shapeThread.speed = 500;

		paint = new Paint();
		paint.setAntiAlias(true);

		tetrisShape = ImageUtil.getShapeImage(tetris.getCurrentShape(),
				colorsCube);
		nextTetrisShape = ImageUtil.getShapeImage(tetris.getNextShape(),
				colorsCube);
		nextTS_resize = resizeShape(tetris.getNextShape());
		containerShape = Bitmap.createBitmap((int) containerRect.width(),
				(int) containerRect.height(), Config.ARGB_8888);

		this.x = this.containerRect.left + tetris.getCurrentShape().vertex.x
				* baseWidth;
		this.y = this.containerRect.top - tetrisShape.getHeight();

		if (game.load != null && mode == BaseView.BASEVIEW_LOADING)
			game.load.notifyLoadComplete();

	}

	private void initBitmap() {
		baseWidth = (float) (MainActivity.width * 0.0625);

		background = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bacground_a);
		background = ImageUtil.resizeImage(background, MainActivity.width,
				MainActivity.height);

		container = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.container);
		container = ImageUtil.resizeImage(container, baseWidth * 10,
				baseWidth * 20);

		nextContainer = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.nextcontainer);
		nextContainer = ImageUtil.resizeImage(nextContainer,
				(float) (baseWidth * 4.5), (float) (baseWidth * 4.5));

		gameOver = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.gameover);
		gameOver = ImageUtil.resizeImage(gameOver, 5 * MainActivity.width / 6,
				MainActivity.width / 8);

		Bitmap cubes = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.cubes);
		colorsCube = ImageUtil.inciseImage(cubes, 3, 2);

		for (int i = 0; i < colorsCube.length; i++) {
			colorsCube[i] = ImageUtil.resizeImage(colorsCube[i], baseWidth,
					baseWidth);
		}

		numberRes = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.number);

		scoreBmp = ImageUtil.getNumberImage(numberRes, tetris.getScore());

	}

	public void onDraw(Canvas canvas) {

		canvas.drawBitmap(background,0,0,paint);
		canvas.drawBitmap(container, baseWidth, 2 * baseWidth / 3, paint);
		canvas.drawBitmap(nextContainer, (float) (baseWidth * 11.5),
				(float) (baseWidth * 1.5), paint);
		canvas.drawBitmap(containerShape, containerRect.left,
				containerRect.top, paint);

		if (gameState == GAMEOVER)
			canvas.drawBitmap(gameOver,
					MainActivity.width / 2 - gameOver.getWidth() / 2,
					MainActivity.height / 2 - gameOver.getHeight() / 2, paint);
		else if (gameState == GAMERUNNING) {
			canvas.drawBitmap(tetrisShape, x, y, paint);
			paint.setARGB(255, 236, 145, 81);
			canvas.drawRect(containerRect.left, 0, containerRect.right,
					containerRect.top, paint);
			canvas.drawBitmap(nextTS_resize, (float) (baseWidth * 12.5),
					(float) (baseWidth * 2.5), paint);
			canvas.drawBitmap(scoreBmp,
					MainActivity.width - scoreBmp.getWidth() - baseWidth / 2,
					baseWidth * 7, paint);
		}
		if (p != null) {
			paint.setColor(Color.YELLOW);
			paint.setTextSize(20);
			for (int i = 0; i < p.length; i++) {
				canvas.drawText("(" + p[i].x + "," + p[i].y + ")", 20,
						30 * i + 20, paint);
			}
		}
	}

	public float getBaseWidth() {
		return baseWidth;
	}

	// {{��Ϸ���Ʒ���
	public void start() {
		shapeThread.start();
		gameState = GAMERUNNING;
	}

	public void pause() {
		if (gameState != GAMEOVER) {
			shapeThread.pause = true;
			if (moveLRThread != null) {
				moveLRThread.run = false;
				isMoveLR = false;
				isMoveLeft = false;
				isMoveRight = false;
			}
			startY = -1;
			startX = -1;
			gameState = GAMEPAUSE;
		}
	}

	public void stop() {
		shapeThread.pause = true;
		shapeThread.run = false;
		gameState = GAMEOVER;
	}

	public void resume() {
		if (gameState != GAMEOVER) {
			shapeThread.pause = false;
			gameState = GAMERUNNING;
		}
	}

	public void gameRun() {
		if (!tetris.move(TetrisBase.DOWN)) {
			if (tetris.isGameOver()) {
				drawToContiner();
				stop();
				return;
			}
			drawToContiner();
			changeShape();
			return;
		}
		y += colorsCube[0].getHeight();
	}

	public void sureDown() {
		shapeThread.speed = 25;
	}

	private float startY = -1, startX = -1;// ��ʼX��Y����
	private float shiftX, shiftY;// X��Y�����ϵ�ƫ����

	public void onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getY();
			startX = event.getX();
			shiftX = 0;
			shiftY = 0;
			break;
		case MotionEvent.ACTION_UP:
			if (gameState == GAMERUNNING) {
				if (startX != -1 && startY != -1) {// ȷ��X��Y�����ϵ�ƫ����
					shiftX = event.getX() - startX;
					shiftY = event.getY() - startY;

					// ���Y�����ϵ�ƫ��������X�����ϵ�ƫ���������ж�Ϊ�����ƶ�
					if (Math.abs(shiftY) > Math.abs(shiftX)) {
						if (shiftY > baseWidth * 2) {
							sureDown();
						} else if (-shiftY > baseWidth * 2) {
							rotate();
						}
					}
					if (isMoveRight || isMoveLeft) {
						isMoveRight = false;
						isMoveLeft = false;
						isMoveLR = false;
						moveLRThread.run = false;
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (gameState == GAMERUNNING) {
				if (startX != -1 && startY != -1) {
					// ȷ��X��Y�����ϵ�ƫ����
					shiftX = event.getX() - startX;
					shiftY = event.getY() - startY;

					// ���X�����ϵ�ƫ��������Y�����ϵ�ƫ���������ж�Ϊ�����ƶ�
					if (Math.abs(shiftX) > Math.abs(shiftY)) {
						if (shiftX > baseWidth * 2) {
							isMoveRight = true;
						} else if (-shiftX > baseWidth * 2) {
							isMoveLeft = true;
						}
					}
					if (isMoveLeft || isMoveRight)
						if (!isMoveLR) {
							isMoveLR = true;
							moveLRThread = new MoveLRThread();
							moveLRThread.start();
						}
				}
			}
			break;
		}
	}

	public void onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			ChooseModeDialog cmd=new ChooseModeDialog(mContext);
			cmd.setOnResultListener(new OnResultListener() {
				@Override
				public void onResult(int result) {
					// TODO Auto-generated method stub
					if(result==3){
						GameView.VIEW.removeDialog();
						game.myHandler.sendEmptyMessage(GameView.HOMEVIEW);
					}
				}
			});
			cmd.show();
		}
	}

	// }}

	// {{ͼ�δ���ķ���

	public void drawToContiner() {

		Canvas drawShape = new Canvas(containerShape);
		// ������
		drawShape.drawBitmap(tetrisShape, x - containerRect.left, y
				- containerRect.top, paint);
		// �ѻ��õ�ͼ�α���
		drawShape.save(Canvas.ALL_SAVE_FLAG);
		drawShape.restore();

		int[] lines = tetris.getTrueLines();
		// ���������Ҫ�������У���������ڵ������������Ϊ�ָ��߷ָ�
		int removeLineNum = 0;// �������С����ڷ���ͳ��
		Bitmap firstBmp = null;
		Bitmap secondBmp = null;
		for (int line : lines) {
			if (line != -1) {
				removeLineNum++;
				tetris.removeLines(line);

				firstBmp = Bitmap.createBitmap(containerShape, 0, 0,
						containerShape.getWidth(), (int) baseWidth * line);
				if (line != 19)
					secondBmp = Bitmap.createBitmap(containerShape, 0,
							(int) baseWidth * (line + 1),
							containerShape.getWidth(), (int) baseWidth
									* (19 - line));

				containerShape = Bitmap.createBitmap(
						(int) containerRect.width(),
						(int) containerRect.height(), Config.ARGB_8888);

				Canvas draw = new Canvas(containerShape);
				// ������

				draw.drawBitmap(firstBmp, 0, baseWidth, paint);
				if (line != 19)
					draw.drawBitmap(secondBmp, 0,
							baseWidth + firstBmp.getHeight(), paint);
				// �ѻ��õ�ͼ�α���
				draw.save(Canvas.ALL_SAVE_FLAG);
				draw.restore();
			}
		}
		scoreBmp = ImageUtil.getNumberImage(numberRes,
				tetris.getScore(removeLineNum));
		shapeThread.speed = 500;
	}

	public void rotate() {
		// ��ת
		if (tetris.rotate()) {
			// �ⲽ�ǵ�����Ϣ
			Point[] tp = tetris.getCurrentShape().coords;
			p = new Point[tp.length + 2];
			for (int i = 0; i < tp.length; i++) {
				p[i] = new Point(tp[i]);
			}
			p[tp.length] = new Point(tetris.getCurrentShape().vertex);
			p[tp.length + 1] = new Point(tetris.getCurrentShape().width,
					tetris.getCurrentShape().height);
			// ���»��Ʒ���ͼƬ
			tetrisShape = ImageUtil.getShapeImage(tetris.getCurrentShape(),
					colorsCube);
			x = this.containerRect.left + tetris.getCurrentShape().vertex.x
					* baseWidth;
		}
	}

	public Bitmap resizeShape(Shape nextShape) {
		Bitmap bmp = null;
		switch (nextShape.shape) {
		case Shape.S:
		case Shape.Z:
		case Shape.T:
			bmp = ImageUtil.resizeImage(nextTetrisShape, 5 * baseWidth / 2,
					5 * baseWidth / 3);
			break;
		case Shape.L:
		case Shape.J:
			bmp = ImageUtil.resizeImage(nextTetrisShape, 5 * baseWidth / 3,
					5 * baseWidth / 2);
			break;
		case Shape.O:
			bmp = ImageUtil.resizeImage(nextTetrisShape, 5 * baseWidth / 2,
					5 * baseWidth / 2);
			break;
		case Shape.I:
			bmp = ImageUtil.resizeImage(nextTetrisShape, 5 * baseWidth / 8,
					5 * baseWidth / 2);
			break;
		}
		return bmp;
	}

	public void changeShape() {
		tetris.changeShape();
		tetrisShape = nextTetrisShape;
		nextTetrisShape = ImageUtil.getShapeImage(tetris.getNextShape(),
				colorsCube);
		nextTS_resize = resizeShape(tetris.getNextShape());
		x = containerRect.left + tetris.getCurrentShape().vertex.x * baseWidth;
		y = this.containerRect.top - tetrisShape.getHeight();
	}

	// }}

	// {{��Ϸ�߳�
	class ShapeThread extends Thread {
		boolean pause = false;
		boolean run = true;
		int speed = 500;

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (run) {
				while (!pause) {
					try {
						gameRun();
						Thread.sleep(speed);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
		}

	}

	class MoveLRThread extends Thread {
		boolean run = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (run) {
				try {
					if (isMoveLeft) {
						if (tetris.move(TetrisBase.LEFT))
							x -= colorsCube[0].getWidth();
					} else if (isMoveRight) {
						if (tetris.move(TetrisBase.RIGHT))
							x += colorsCube[0].getWidth();
					}
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.run();
			}
		}
	}
	// }}
}
