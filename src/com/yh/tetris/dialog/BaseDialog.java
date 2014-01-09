package com.yh.tetris.dialog;

import com.yh.tetris.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

public class BaseDialog {
	public static interface OnResultListener{
		public void onResult(int result);
	}
	
	protected Context mContext;
	protected OnResultListener listener;
	protected Bitmap dialogBlock;//对话框的总体贴图。用于过场动画时整体处理
	protected RectF dialogRectF;//对话框的范围
	protected Point dialogLocat;//过场动画时贴图的坐标
	protected boolean isTransform;

	Paint paint;
	
	public BaseDialog(Context mContext){
		this.mContext=mContext;
		isTransform=false;

		paint = new Paint();
		paint.setAntiAlias(true);
	}
	public void setOnResultListener(OnResultListener listener){
		this.listener=listener;
	}
	@SuppressLint("WrongCall")
	public void setDialogRectF(RectF rectf){
		this.dialogRectF=rectf;
		this.dialogLocat=new Point((int)MainActivity.width,(int)rectf.top);
//		this.dialogBlock=Bitmap.createBitmap((int)rectf.width(), (int)rectf.height(), Config.ARGB_8888);
	}
	public RectF getDialogRectF(){
		return this.dialogRectF;
	}
	public void onDraw(Canvas canvas){
//		if(canvas!=null)
//			canvas.drawBitmap(dialogBlock, dialogLocat.x, dialogLocat.y, paint);
	}
	public void show(){
		new TransformThread(TransformThread.IN).start();
	}
	public void close(){
		new TransformThread(TransformThread.OUT).start();		
	}
	public void onTouchEvent(MotionEvent event){};
	
	class TransformThread extends Thread{
		public static final int IN=0;
		public static final int OUT=1;
		
		private float v0;//初速度
		private int dic;//方向
//		private float a;//加速度
		
		public TransformThread(int type){
			dic=type;
			switch(type){
			case IN:
				v0=-MainActivity.width/10;
//				a=MainActivity.width/20;
				break;
			case OUT:
				v0=MainActivity.width/10;
//				a=-MainActivity.width/20;
				break;
			}
		}
		@Override
		public void run() {
			isTransform=true;
			// TODO Auto-generated method stub
			while(true){
				if(dic==IN&&dialogLocat.x<dialogRectF.left)
					break;
				else if(dic==OUT&&dialogLocat.x>MainActivity.width)
					break;
				dialogLocat.x+=v0;
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dialogLocat.x=(int)dialogRectF.left;
			isTransform=false;
		}
		
	}
}
