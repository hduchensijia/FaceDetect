package com.example.partialphoto;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import com.example.facedetect.Facedetect;
import com.example.facedetect.R;
import android.annotation.SuppressLint;
//import android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
//import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

@SuppressLint("WrongCall") public class RectPhotoActivity extends Activity implements Callback {

	private static final String tag="PartialPhoto";
	public static int frame = 0;
	private boolean isPreview = false;
	private SurfaceView mPreviewSV = null;
	private DrawViewFrame mDrawIV = null;
	private SurfaceHolder mySurfaceHolder = null;
	private ImageButton mPhotoImgBtn = null;
	private ImageButton btnAdd = null;
	private ImageButton btnMinus = null;
	private Camera myCamera = null;
	private Bitmap mBitmap = null;
	private AutoFocusCallback myAutoFocusCallback = null;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window myWindow = this.getWindow();
		myWindow.setFlags(flag, flag);

	setContentView(R.layout.activity_facedetect);

	mPreviewSV = (SurfaceView)findViewById(R.id.previewSV);
		mPreviewSV.setZOrderOnTop(false);
		mySurfaceHolder = mPreviewSV.getHolder();
		mySurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		mySurfaceHolder.addCallback(this);

	//mDrawIV = (DrawViewFrame)findViewById(R.id.drawIV);
	mDrawIV.onDraw(new Canvas());

		
		btnAdd = (ImageButton) findViewById(R.id.addframe);
		btnMinus = (ImageButton) findViewById(R.id.minusframe);
		
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				frame++;
				if(frame >= 10)
					frame--;
				mDrawIV.postInvalidate();
			}
		});
		
		btnMinus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				frame--;
				if(frame <= -2)
					frame++;
				mDrawIV.postInvalidate();
			}
		});
	
		
		//notify on completion of camera auto focus
		myAutoFocusCallback = new AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				// TODO Auto-generated method stub
				if(success)    //focus successfully
					Log.i(tag, "myAutoFocusCallback: success...");
				else
					Log.i(tag, "myAutoFocusCallback: failure");
			}
		};
		
		
		
		mPhotoImgBtn = (ImageButton)findViewById(R.id.takephoto);
		mPhotoImgBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPreview && myCamera!=null){
					myCamera.takePicture(null, null, myJpegCallback);  //without shutter sound
					//myCamera.takePicture(myShutterCallback, null, myJpegCallback);  //with shutter sound
				}
			}
		});
	}



	
	//three important callback functions of SurfaceHolder.Callback as follows:
	//surfaceChanged, surfaceCreated, surfaceDestroyed
	
	//This is called immediately after any structural changes have been made to the surface.
	public void surfaceChanged(SurfaceHolder holder, int format, int prevWidth,int prevHeight) {
		// TODO Auto-generated method stub		
		Log.i(tag, "SurfaceHolder.Callback:surfaceChanged!");
		if(isPreview){
			myCamera.stopPreview();
		}
		if(null != myCamera){			
			Camera.Parameters myParam = myCamera.getParameters();
			myParam.setPictureFormat(ImageFormat.JPEG);   //set the stored picture format
			
			int width = mDrawIV.getWidth();
			int height = mDrawIV.getHeight();
			
			myParam.setPictureSize(width, height);
			
			//NOTE: 
			//set the size of preview interface
			if (getWindowManager().getDefaultDisplay().getRotation() == 0){
				myCamera.setDisplayOrientation(90); 
				myParam.setPreviewSize(prevHeight, prevWidth);  
			} 
			else   
				myParam.setPreviewSize(prevWidth, prevHeight);
			
			try
			{
				myCamera.setParameters(myParam);	
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			myCamera.startPreview();
			myCamera.autoFocus(myAutoFocusCallback);
			isPreview = true;
		}

	}

	
	
	//This is called immediately after the surface is first created.
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub	
		myCamera = Camera.open();
		try {
			myCamera.setPreviewDisplay(mySurfaceHolder);
			Log.i(tag, "SurfaceHolder.Callback: surfaceCreated!");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			if(null != myCamera){
				myCamera.release();
				myCamera = null;
				//Writes a printable representation of this Throwable's stack trace
				e.printStackTrace();
			}
		}
	}

	

	//This is called immediately before a surface is being destroyed.
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(tag, "SurfaceHolder.Callback: surface Destroyed");
		if(null != myCamera)
		{
			myCamera.setPreviewCallback(null); 
			myCamera.stopPreview(); 
			isPreview = false; 
			myCamera.release();
			myCamera = null;    
		}
	}
	
	
	
/*
	// play a shutter sound or give other feedback of camera operation
	ShutterCallback myShutterCallback = new ShutterCallback() {
		
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(tag, "myShutterCallback:onShutter...");
		}
	};
*/
	
	//supply original image data from a photo capture
	PictureCallback myJpegCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(tag, "myJpegCallback:onPictureTaken...");
			if(null != data){
				mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				myCamera.stopPreview();
				isPreview = false;
			}
			
			int width = mDrawIV.getWidth();
			int height = mDrawIV.getHeight();
			
			int point_dw = (int)(width/10);
			int point_dh = (int)(5*height/12-height*frame/30);
			
			int rect_dw = width-2*point_dw;
			int rect_dh = height-2*point_dh;
			
			Bitmap sizeBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
			
			//NOTE: 
			//set the format of the picture of the interception
			Bitmap rectBitmap = Bitmap.createBitmap(sizeBitmap, point_dw, point_dh, rect_dw, rect_dh);
			
			myCamera.startPreview();
			isPreview = true;
			
			if(null != rectBitmap)
			{
				String CurrentPhotoFile = "";
				CurrentPhotoFile = saveJpeg(rectBitmap);   //save bitmap as a picture in sdcard
				
				//skip back with the picture path
				//Intent intent = new Intent();
				//intent.putExtra("path", CurrentPhotoFile);
				//setResult(RESULT_OK, intent);
				//finish();
			}
		}
	};
	

	
	//save the picture
	public String saveJpeg(Bitmap bm){
		String jpegName = "";
		String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pp/";   //save directory
		File folder = new File(savePath);
		if(!folder.exists())
			folder.mkdir();
		
		long dataTake = System.currentTimeMillis();
		jpegName = savePath + dataTake +".jpg";
		
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);

			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(tag, "saveJpeg：storage completed!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "saveJpeg: storage failed!");
			e.printStackTrace();
		}
		
		return jpegName;
	}

}
