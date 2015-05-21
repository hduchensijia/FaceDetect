package com.example.facedetect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;


import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
///import com.facpp.picturedetect.R;
//import com.facpp.picturedetect.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Facedetect extends Activity {
	private ImageView faceshow;
	
	////////////////////////////////////////////////////
	final private int PICTURE_CHOOSE = 1;
	//private String show=null;
	private ImageView imageView = null;
	private Bitmap img = null;
	private Button buttonDetect = null;
	private TextView textView = null;
	private TextView textViewResult = null;
	/////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facedetect);

		faceshow=(ImageView)findViewById(R.id.imageView1);
		 textView = (TextView)this.findViewById(R.id.textView1);
		 textViewResult= (TextView)this.findViewById(R.id.textView2);
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
                img=oldbmp;
		}
		
		
		
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
				
				
				try {
					//find out all faces
					final int count = rst.getJSONArray("face").length();
			
					for (int i = 0; i < count; ++i) {
						float x, y, w, h;
						//get the center point
						x = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getJSONObject("center").getDouble("x");
						y = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getJSONObject("center").getDouble("y");

						//get face size
						w = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getDouble("width");
						h = (float)rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("position").getDouble("height");
						
						//change percent value to the real size
						x = x / 100 * img.getWidth();
						w = w / 100 * img.getWidth() * 0.7f;
						y = y / 100 * img.getHeight();
						h = h / 100 * img.getHeight() * 0.7f;

						//draw the box to mark it out
						canvas.drawLine(x - w, y - h, x - w, y + h, paint);
						canvas.drawLine(x - w, y - h, x + w, y - h, paint);
						canvas.drawLine(x + w, y + h, x - w, y + h, paint);
						canvas.drawLine(x + w, y + h, x + w, y - h, paint);
					}
					
					//save new image
					img = bitmap;

					JSONObject arribute=null;
					
					String temp=null;
					
				try{
				    arribute=rst.getJSONArray("face").getJSONObject(0).getJSONObject("attribute");
					}catch (JSONException e) {
			     	e.printStackTrace();	
					}
					
					try{
						temp=new String(String.format("年龄:%d波动范围%d\n性别:%s可信度%.2f\n种族:%s可信度%.2f\n微笑指数:%.2f",(int)arribute.getJSONObject("age").getDouble("value"), (int)arribute.getJSONObject("age").getDouble("range"),
								arribute.getJSONObject("gender").getString("value"),arribute.getJSONObject("gender").getDouble("confidence"),
								arribute.getJSONObject("race").getString("value"),arribute.getJSONObject("race").getDouble("confidence"),
								arribute.getJSONObject("smiling").getDouble("value")));
					}catch (JSONException e) {
						e.printStackTrace();
					}
					
					final String show=temp;
					
					
					Facedetect.this.runOnUiThread(new Runnable() {
						
						public void run() {
							//show the image
							//imageView.setImageBitmap(img);
							textView.setText("Finished, "+ count + " faces.");
							textViewResult.setText(show);
						}
					});
					
				} catch (JSONException e) {
			//		e.printStackTrace();
				//	facedetect.this.runOnUiThread(new Runnable() {
				///		public void run() {
						textView.setText("Error.");
				//		}
				//	});
				}
				
			}
		});
		faceppDetect.detect(img);
	


//imageView = (ImageView)this.findViewById(R.id.imageView1);
//imageView.setImageBitmap(img);
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
			
			new Thread(new Runnable() {
				
				public void run() {
					HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M", true, false);
		    		//Log.v(TAG, "image size : " + img.getWidth() + " " + img.getHeight());
		    		
		    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    		float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
		    		Matrix matrix = new Matrix();
		    		matrix.postScale(scale, scale);

		    		Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
		    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
		    		
		    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    		byte[] array = stream.toByteArray();
		    		
		    		try {
		    			//detect
						JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
						//finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
							System.out.println("result---->"+result);

						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						Facedetect.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("Network error.");
								
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

