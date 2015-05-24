package com.example.facedetect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;



import com.canvas.draw.ResultShow;
import com.example.mainview.MainActivity;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.gem.hsx.sqlite.daliydata;

///import com.facpp.picturedetect.R;
//import com.facpp.picturedetect.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.database.Cursor;
public class Facedetect extends Activity  {
	private ImageView faceshow;
	
	////////////////////////////////////////////////////
	final private int PICTURE_CHOOSE = 1;
	//private String show=null;
	private ImageView imageView = null;
	private ProgressDialog mSaveDialog = null;
	private Bitmap img = null;
	private Button buttonDetect = null;
	private TextView textView = null;
	private TextView textViewResult = null;
	private float [] HealthArry;
	private daliydata ddb;
	private Cursor mCursor;
	private Button History_button;
	private ProgressDialog progressDialog;
	private int count;
	private Handler mHandler;
	private int DrawProcessCount=0;
	/////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facedetect);

		 faceshow=(ImageView)findViewById(R.id.imageView1);
		 textView = (TextView)this.findViewById(R.id.textView1);
		 textViewResult= (TextView)this.findViewById(R.id.textView2);
		 History_button=(Button)findViewById(R.id.button1);
		 
		Bundle bundle = getIntent().getExtras();
		boolean local=bundle.getBoolean("local");
		if(local){
			
		
			Bitmap bitmap = bundle.getParcelable("bitmap");
			faceshow.setImageBitmap(bitmap);
			img=bitmap;
		
		}else{
			
			   Drawable dra=Drawable.createFromPath(new File(  
	                   Environment.getExternalStorageDirectory(), "camera.jpg")  
              .getAbsolutePath());
			   //faceshow.setImageDrawable(dra);  
			   
			   int width = dra.getIntrinsicWidth();
			   int height = dra.getIntrinsicHeight();
			   Bitmap oldbmp = drawableToBitmap(dra);  
			    Matrix matrix = new Matrix();  
			   float scaleWidth = ((float) 150 / width);  
		       float scaleHeight = ((float) 150 / height);  
			    matrix.postScale(scaleWidth, scaleHeight);  
			    Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,  
			          matrix, true);  
			    faceshow.setImageBitmap(newbmp);  
                img=newbmp;
		}
		////////////show process////////////////////////////////////////
		//DrawProcess(img);
    	mHandler = new Handler();
		mHandler.post(new TimerProcess());
		
		
		 mSaveDialog = ProgressDialog.show(Facedetect.this, "图片正在分析中", "请稍等...", true); 
		FaceppDetect faceppDetect = new FaceppDetect();
		faceppDetect.setDetectCallback(new DetectCallback() {
			
			public void detectResult(JSONObject rst) {
				//Log.v(TAG, rst.toString());
				
				//use the red paint
				Paint paint = new Paint();
				paint.setColor(Color.RED);
				paint.setStrokeWidth(Math.max(img.getWidth(), img.getHeight()) / 100f);

				//create a new canvas
				Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
				Canvas canvas = new Canvas(bitmap);
				canvas.drawBitmap(img, new Matrix(), null);
				
				

					
		////////////////////computer simulation for healthvalue and beauty//////////////////////////////////////////		
				Random random = new Random();
				HealthArry = new float [6];
				double ans=5;
				
				for(int i=0;i<5;i++)
				{
					HealthArry[i] = random.nextInt()%3;
					ans=ans-(float)HealthArry[i]*0.5;
				}
				final double ans_health_show=ans;
				final double ans_beauty_show=(int)(Math.random()*40+60);;
       ////////////////////computer simulation healthvalue beauty//////////////////////////////////////////
				Context ctx = Facedetect.this; 
				
				SharedPreferences sp = ctx.getSharedPreferences("map", MODE_PRIVATE);
				
				String current_user=sp.getString("username", "temp");
				ddb=new daliydata(Facedetect.this,current_user+"health");
				ddb.settablename(current_user+"health");
				ddb.onCreate(ddb.getReadableDatabase());
			    mCursor=ddb.select();
			    ddb.insert("time:"+mCursor.getCount(), (int)ans_health_show);
			    
			    new  Thread()
			     {
			    	public void run() {          
			    	 try {     
			    		    while(DrawProcessCount<img.getHeight())
			    		    {
			    			 sleep(500);        
			    		    }
			    		 } catch (InterruptedException e) {         				        
			    		     e.printStackTrace();                  
			    		 } finally{   
			
    						Facedetect.this.runOnUiThread(new Runnable() {
    							public void run() {
    								//show the image
    								//imageView.setImageBitmap(img);	
    								mSaveDialog.dismiss(); 
    								textView.setText("健康指数:"+ans_health_show);
    								textViewResult.setText("魅力指数:"+ans_beauty_show);   						
    							}
    						});
		    					 //messageListener.sendEmptyMessage(0);     					 
			    	     }            
			    	}
			    		 
			     }.start();

			    

				
			}
		});
		faceppDetect.detect(img);
	

        History_button.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        	Intent intent=new Intent(Facedetect.this,ResultShow.class);
        	startActivity(intent);
        }
	});
//imageView = (ImageView)this.findViewById(R.id.imageView1);
//imageView.setImageBitmap(img);
}
	

	
/*
private Handler messageListener = new Handler(){     
	public void handleMessage(Message msg) {    
		switch(msg.arg1)
		{        
		case TASK_LOOP_COMPLETE:      
			pd.dismiss();      
			break;                
			}      
		}  
	};

*/
    public void DrawProcess(Bitmap bi){
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		
		//鐢诲妗�
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		//paint.setStrokeWidth(Pain);		
		//Rect chartRec = new Rect(0, 0,50, 50);
		Canvas can=new Canvas(bi);
		//can.drawRect(chartRec, paint);
		Path path = new Path();  
		int py=(DrawProcessCount++)%can.getHeight();
		path.moveTo(0,py );
		path.lineTo(can.getWidth(), py);
		can.drawPath(path, paint);
		

    }
    
	private class TimerProcess implements Runnable {
		public void run() {
			//faceshow.postInvalidate();
			Bitmap temp=Bitmap.createBitmap(img);
			faceshow.setImageBitmap(temp);
			DrawProcess(temp);	
			Canvas can=new Canvas(temp);
			if(DrawProcessCount<can.getHeight()){
				mHandler.postDelayed(this, 100);
			}
			
			
		}
	}
	public static Bitmap drawableToBitmap(Drawable drawable) {  
		          
		        Bitmap bitmap = Bitmap.createBitmap(  
		                              drawable.getIntrinsicWidth(),  
		                              drawable.getIntrinsicHeight(),  
		                              drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
		        Canvas canvas = new Canvas(bitmap);  
		        //canvas.setBitmap(bitmap);  
		        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
		        drawable.draw(canvas);  
		        return bitmap;  
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_facedetect, menu);
		return true;
	}
	
	private class FaceppDetect {
		DetectCallback callback = null;
		
		public void setDetectCallback(DetectCallback detectCallback) { 
			callback = detectCallback;
		}

		public void detect(final Bitmap image) {
			
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
    		Matrix matrix = new Matrix();
    		matrix.postScale(scale, scale);

    		Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
    		
    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    		byte[] array = stream.toByteArray();
    		
			new Thread(new Runnable() {
				
				public void run() {
                 
					
		    		try {
		    			//detect   //prepared for PicAnalysis	
						JSONObject result =new JSONObject();//httpRequests.detectionDetect(new PostParameters().setImg(array));
						result.put("health1", 3);
						//finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
							System.out.println("result---->"+result);

						}
					} catch (JSONException e) {
						e.printStackTrace();
						Facedetect.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("picAnalysis error.");
								
							}
						});
					}
					
				
				}
			}).start();
		}
	}

	interface DetectCallback {
		void detectResult(JSONObject rst);
	}

}

