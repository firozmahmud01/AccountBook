package com.firoz.mahmud.accountbook;
import android.content.Context;
import android.graphics.*;
import android.media.*;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

import java.io.InputStream;


public class ImageGenarator
{
	public Bitmap circleBitmap(Bitmap bitmap){
		//making circle image
		Bitmap output=Bitmap.createBitmap(
		bitmap.getWidth()
		,bitmap.getHeight(),
		Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		int color=0xff424242;
		Paint paint=new Paint();
		Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0,0,0,0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth()/2
		,bitmap.getHeight()/2
		,bitmap.getWidth()/2
		,paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap,rect,rect,paint);
		return output;
	}
	public Bitmap getFace(String path){
		//finding faces and crop one of them
		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap b= BitmapFactory.decodeFile(path, BitmapFactoryOptionsbfo);
		FaceDetector fd=new FaceDetector(b.getWidth(),b.getHeight(),1);
		FaceDetector.Face[] faces=new FaceDetector.Face[1];
		int total=fd.findFaces(b,faces);
		if(total<1){
			return null;
		}
		FaceDetector.Face face=faces[0];
		float eyedis=face.eyesDistance();
		PointF center=new PointF();
		face.getMidPoint(center);
		Bitmap bit=BitmapFactory.decodeFile(path);
		Bitmap output=Bitmap.createBitmap(
			(int)((eyedis*2)+(eyedis/2))*2
			,(int)((eyedis*2)+(eyedis/2))*2,
			Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		float leftp=center.x-((eyedis*2)+(eyedis/2));
		leftp=leftp>=0?leftp:0;
		float topp=center.y-((eyedis*2)+(eyedis/2));
		topp=topp>=0?topp:0;
		canvas.drawBitmap(bit,leftp*(-1),topp*(-1),null);
		return output;
	}
	public Bitmap getFace(String path, Context context){
		//finding faces and crop one of them
		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap=BitmapFactory.decodeFile(path,BitmapFactoryOptionsbfo);
		com.google.android.gms.vision.face.FaceDetector faceDetector = new
				com.google.android
						.gms.vision.face.FaceDetector
						.Builder(context.getApplicationContext())
				.setTrackingEnabled(false)
				.build();
		Frame frame = new Frame.Builder().setBitmap(bitmap).build();
		SparseArray<Face> faces = faceDetector.detect(frame);

		if(faces.size()<1){
			return null;
		}
		Face face=faces.get(0);
		Bitmap output=Bitmap.createBitmap(
				(int) face.getWidth()
				,(int)face.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		Bitmap newbitmap=BitmapFactory.decodeFile(path);
		canvas.drawBitmap(newbitmap,face.getPosition().x*(-1),(-1)*face.getPosition().y,null);
		return output;
	}
	public Bitmap cropRightSite(Bitmap bit,float persent){
		//croping right side of circle image to make new style
		//persent can be 20 of 100
		Bitmap output=Bitmap.createBitmap(
			(int)(((float)bit.getWidth())-((((float)bit.getWidth())/100)*persent))
			,bit.getHeight(),
			Bitmap.Config.ARGB_8888);
			Canvas canvas=new Canvas(output);
			canvas.drawBitmap(bit,0,0,null);
			return output;
	}
}
