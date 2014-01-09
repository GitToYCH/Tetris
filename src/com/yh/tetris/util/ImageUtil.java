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
	// 定义缩放图片的方式
	public static final int WIDTH_HEIGHT = 0; // 以宽度为基础等比例缩放
	public static final int HEIGHT_WIDTH = 1; // 以高度为基础等比例缩放

	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 *            原图片
	 * @param w
	 *            缩放后的宽度
	 * @param h
	 *            缩放后的高度
	 * @return 缩放后的图片
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
	 * 等比例缩放图片
	 * 
	 * @param bitmap
	 *            原图片
	 * @param s
	 *            需要缩放的新长度
	 * @param type
	 *            缩放的方式，以宽为基础等比例or以高为基础等比例
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
	 *  把一张大图片按网格切分成一组子图片。例如传入一张90*90的图，按3*3比例划分，能得到9张30*30的图片
	 *  @author Administrator 
	 *  @param res 源图片
	 *  @param width 横向图片数
	 *  @param height 纵向图片数
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
	 * 将数字转换为图片
	 * 
	 * @param number
	 *            数
	 * @return 数的图片
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
	 * 把俄罗斯方块的图形(Shape对象)绘制为图片(Bitmap对象)
	 * 
	 * @author Administrator
	 * @param shape
	 *            要绘制的俄罗斯方块的图形
	 * @param res
	 *            方块资源
	 * @return 方块图形图片
	 */
	public static Bitmap getShapeImage(Shape shape, Bitmap[] res) {
		// 随机方块颜色
		Random rand = new Random();
		int color = Math.abs(rand.nextInt()) % res.length;
		Bitmap colorCube = res[color];
		// 创建图像
		Bitmap shapeBmp = Bitmap.createBitmap(colorCube.getWidth()
				* shape.width, colorCube.getHeight() * shape.height,
				Config.ARGB_8888);
		// 创建画布，以tetrisShape为参数
		Canvas drawShape = new Canvas(shapeBmp);
		// 画方块
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		for (Point p : shape.coords) {
			int x = (p.x - shape.vertex.x) * colorCube.getWidth();
			int y = (p.y - shape.vertex.y) * colorCube.getHeight();
			drawShape.drawBitmap(colorCube, x, y, paint);
		}
		// 把画好的图形保存在tetrisShape上
		drawShape.save(Canvas.ALL_SAVE_FLAG);
		drawShape.restore();

		return shapeBmp;
	}
	/**
	 * 复制一张图片
	 *  @author Administrator 
	 *  @param res 原图片
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
