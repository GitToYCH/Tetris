package com.yh.tetris.util;

import java.util.Random;

import com.yh.tetris.game.Shape;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

public class ImageUtil {
	// ��������ͼƬ�ķ�ʽ
	public static final int WIDTH_HEIGHT = 0; // �Կ��Ϊ�����ȱ�������
	public static final int HEIGHT_WIDTH = 1; // �Ը߶�Ϊ�����ȱ�������

	/**
	 * ����ͼƬ
	 * 
	 * @param bitmap
	 *            ԭͼƬ
	 * @param w
	 *            ���ź�Ŀ��
	 * @param h
	 *            ���ź�ĸ߶�
	 * @return ���ź��ͼƬ
	 */
	public static Bitmap resizeImage(Bitmap bitmap, float w, float h) {

		Bitmap BitmapOrg = bitmap;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		float newWidth = w;
		float newHeight = h;

		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		return resizedBitmap;

	}

	/**
	 * �ȱ�������ͼƬ
	 * 
	 * @param bitmap
	 *            ԭͼƬ
	 * @param s
	 *            ��Ҫ���ŵ��³���
	 * @param type
	 *            ���ŵķ�ʽ���Կ�Ϊ�����ȱ���or�Ը�Ϊ�����ȱ���
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, float s, int type) {
		float newWidth;
		float newHeight;
		float scale = (float) (bitmap.getWidth())
				/ (float) (bitmap.getHeight());

		switch (type) {
		case WIDTH_HEIGHT:
			newWidth = s;
			newHeight = s / scale;
			break;
		case HEIGHT_WIDTH:
			newHeight = s;
			newWidth = s * scale;
			break;
		default:
			newWidth = bitmap.getWidth();
			newHeight = bitmap.getHeight();
		}
		return resizeImage(bitmap, newWidth, newHeight);
	}

	/**
	 *  ��һ�Ŵ�ͼƬ�������зֳ�һ����ͼƬ�����紫��һ��90*90��ͼ����3*3�������֣��ܵõ�9��30*30��ͼƬ
	 *  @author Administrator 
	 *  @param res ԴͼƬ
	 *  @param width ����ͼƬ��
	 *  @param height ����ͼƬ��
	 *  @return
	 */
	public static Bitmap[] inciseImage(Bitmap res, int width, int height) {
		Bitmap[] imgs = new Bitmap[width * height];
		int cellWidth = res.getWidth() / width;
		int cellHeight = res.getHeight() / height;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				imgs[i * width + j] = Bitmap.createBitmap(res, cellWidth * j,
						cellHeight * i, cellWidth, cellHeight);
			}
		return imgs;
	}

	/**
	 * ������ת��ΪͼƬ
	 * 
	 * @param number
	 *            ��
	 * @return ����ͼƬ
	 */
	public static Bitmap getNumberImage(Bitmap numberRes, int number) {
		Bitmap[] numberIcon = new Bitmap[10];
		int width = numberRes.getWidth() / 10;
		for (int i = 0; i < 10; i++) {
			numberIcon[i] = Bitmap.createBitmap(numberRes, width * i, 0, width,
					numberRes.getHeight());
		}

		String numberStr = String.valueOf(number);
		Bitmap numberBmp = Bitmap.createBitmap(width * numberStr.length(),
				numberRes.getHeight(), Config.ARGB_8888);
		Canvas drawShape = new Canvas(numberBmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		for (int i = 0; i < numberStr.length(); i++) {
			int n = Integer.valueOf(String.valueOf(numberStr.charAt(i)));
			drawShape.drawBitmap(numberIcon[n], width * i, 0, paint);
		}
		drawShape.save(Canvas.ALL_SAVE_FLAG);
		drawShape.restore();
		return numberBmp;
	}

	/**
	 * �Ѷ���˹�����ͼ��(Shape����)����ΪͼƬ(Bitmap����)
	 * 
	 * @author Administrator
	 * @param shape
	 *            Ҫ���ƵĶ���˹�����ͼ��
	 * @param res
	 *            ������Դ
	 * @return ����ͼ��ͼƬ
	 */
	public static Bitmap getShapeImage(Shape shape, Bitmap[] res) {
		// ���������ɫ
		Random rand = new Random();
		int color = Math.abs(rand.nextInt()) % res.length;
		Bitmap colorCube = res[color];
		// ����ͼ��
		Bitmap shapeBmp = Bitmap.createBitmap(colorCube.getWidth()
				* shape.width, colorCube.getHeight() * shape.height,
				Config.ARGB_8888);
		// ������������tetrisShapeΪ����
		Canvas drawShape = new Canvas(shapeBmp);
		// ������
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		for (Point p : shape.coords) {
			int x = (p.x - shape.vertex.x) * colorCube.getWidth();
			int y = (p.y - shape.vertex.y) * colorCube.getHeight();
			drawShape.drawBitmap(colorCube, x, y, paint);
		}
		// �ѻ��õ�ͼ�α�����tetrisShape��
		drawShape.save(Canvas.ALL_SAVE_FLAG);
		drawShape.restore();

		return shapeBmp;
	}
	/**
	 * ����һ��ͼƬ
	 *  @author Administrator 
	 *  @param res ԭͼƬ
	 *  @return
	 */
	public static Bitmap copyImage(Bitmap res){
		Bitmap newBmp=Bitmap.createBitmap(res.getWidth(), res.getHeight(), Config.ARGB_8888);
		
		Canvas drawShape = new Canvas(newBmp);
		drawShape.drawBitmap(res, 0, 0, null);
		drawShape.save(Canvas.ALL_SAVE_FLAG);
		drawShape.restore();
		
		return newBmp;
	}
}
