package com.example.mainview;

import java.io.File;
import java.io.FileNotFoundException;



import com.example.facedetect.Facedetect;
import com.example.facedetect.R;
import com.example.partialphoto.RectPhotoActivity;
import com.gem.hsx.sqlite.userdb;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity {
  private ImageView logo;
  private Button btmale;
  private Button btfemale;
  private EditText username;
  private EditText password;
  private Button sginin;
  private Button register;
  private TextView userstate;
  private Button signout;
  private userdb udb;
  private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
		Context ctx = MainActivity.this; 
		
		SharedPreferences sp = ctx.getSharedPreferences("map", MODE_PRIVATE);
		//Editor editor = sp.edit();
		///editor.putString("username",username.getText().toString());
		//editor.putString("password",password.getText().toString());
		//editor.commit();
		final	String name_saved=sp.getString("username", "");
	 	final 	String word_saved=sp.getString("password", "");
       // setContentView(R.layout.activity_main);
       
        if(name_saved.equals("")){
 	
    		setContentView(R.layout.activity_signin);
    		
    		username=(EditText)findViewById(R.id.editText1);
    		password=(EditText)findViewById(R.id.editText2);
    		
    		sginin=(Button)findViewById(R.id.button1);
    		register=(Button)findViewById(R.id.button2);
    		sginin.setOnClickListener(new OnClickListener(){
    			public void onClick(View v){
    				
    				if(username.getText().toString().equals("") || password.getText().toString().equals("")){
    					Toast.makeText(MainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
    					return;
    				}
    				
            		udb=new userdb(MainActivity.this);
       			 	mCursor = udb.select();
       			 	
       			 	boolean flag=false;
       			 	if(mCursor.moveToFirst()){//判断游标是否为空

		               for(int i=0;i<mCursor.getCount();i++){

	 		            	
		            	
		               
	 		               final String name=mCursor.getString(mCursor.getColumnIndex("username"));
	 		               final String usercode=mCursor.getString(mCursor.getColumnIndex("password"));
	 		              
	 		               if(username.getText().toString().equals(name) && password.getText().toString().equals(usercode) ){
	 		            	  flag=true;
	 		               }
	 		              mCursor.moveToNext();//移动到指定记录
		                }
       			 	}
       			 	
	 		        if(flag==false){
	 		        	Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
	 		        }else{
	 		        	
	 		        	Context ctx = MainActivity.this; 
	        			SharedPreferences sp = ctx.getSharedPreferences("map", MODE_PRIVATE);
	        			Editor editor = sp.edit();
	        			editor.putString("username",username.getText().toString());
	        			editor.putString("password",password.getText().toString());
	        			editor.commit();
	        			
	        			Intent intent=MainActivity.this.getIntent();
	        			MainActivity.this.finish();
	        			startActivity(intent);
	        			
	 		        }
	 		        	
    				
    			}
    		});
    		
    		register.setOnClickListener(new OnClickListener(){
    			public void onClick(View v){
    				Intent intent=new Intent(MainActivity.this,Register.class);
    				startActivity(intent);
    			}
    		});
        
        }else{
        	
        	setContentView(R.layout.activity_main);
	        btmale=(Button)findViewById(R.id.button1);
	        signout=(Button)findViewById(R.id.button2);
	        userstate = (TextView)findViewById(R.id.textView1);
	        userstate.setText("用户名:"+name_saved);
	        
	        signout.setOnClickListener(new OnClickListener(){
		        	public void onClick(View v){
		        		Context ctx = MainActivity.this; 
		        		
		        		SharedPreferences sp = ctx.getSharedPreferences("map", MODE_PRIVATE);
		        		Editor editor = sp.edit();
		        		editor.clear();
		        		//editor.putString("password",password.getText().toString());
		        		editor.commit();
		        		final Intent intent=MainActivity.this.getIntent();
		        		MainActivity.this.finish();
		        		startActivity(intent);
		        		
		        	}
	        	});
	        
	        btmale.setOnClickListener(new OnClickListener(){
	        	public void onClick(View v){
	        		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	                builder.setTitle(getString(R.string.dig_title)).setIcon(
	                        R.drawable.dig_clolor);
	               
	                builder.setNegativeButton(getString(R.string.local),
	                        new DialogInterface.OnClickListener() {
	        
	                           public void onClick(DialogInterface dialog, int which) {
	                        	       Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	                        	       intent.addCategory(Intent.CATEGORY_OPENABLE);  
	                        	       intent.setType("image/*");  
	                        	       
	                        	       intent.putExtra("crop", "true");  
	                                   intent.putExtra("aspectX",1);  
	                                   intent.putExtra("aspectY",1);  
	                        	       intent.putExtra("outputX", 80);  
	                        	       intent.putExtra("outputY", 80);  
	                        	       intent.putExtra("return-data",true); 
	                        	       
	                        	       startActivityForResult(intent, 11);  
	
	                            }
	                        });     
	                
	                builder.setPositiveButton(getString(R.string.camera),
	                        new DialogInterface.OnClickListener() {
	        
	                           public void onClick(DialogInterface dialog, int which) {
	                        	   
	                               Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");  
	                               intent.putExtra(MediaStore.EXTRA_OUTPUT,  
	                        	     Uri.fromFile(new File(Environment  
	                        	       .getExternalStorageDirectory(), "camera.jpg")));  
	                        	      intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);  
	                        	      startActivityForResult(intent, 10);  
	                                 
	                        	//   Intent intent = new Intent(MainActivity.this,RectPhotoActivity.class);  
	                        	//   startActivityForResult(intent,10);
	                              
	                            }
	                        });
	                builder.show();
	        	}
	        });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        
        return true;
    }
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	 if(resultCode == RESULT_OK){
		   if(requestCode==11){

			   System.out.println("requestCode"+requestCode);  
			   Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");  
			   super.onActivityResult(requestCode, resultCode, data);  
			   
			   Bundle bundle = new Bundle();        
			   bundle.putParcelable("bitmap", cameraBitmap);
			   bundle.putBoolean("local", true);
			   Intent intent=new Intent(MainActivity.this,Facedetect.class);
			   intent.putExtras( bundle);
			   startActivity(intent);
			   
		   }else{
			   Bundle bundle = new Bundle();        
			  
			   bundle.putBoolean("local", false);
			   Intent intent=new Intent(MainActivity.this,Facedetect.class);
			   intent.putExtras( bundle);
			   startActivity(intent);
			   
			   
		   }
			   


	 }

	 /*
	        if (resultCode == RESULT_OK) {  
		           Uri uri = data.getData();  
		           Log.e("chensijiauri", uri.toString());  
		          ContentResolver cr = this.getContentResolver();  
	        try {  
		             Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
		           //   ImageView imageView = (ImageView) findViewById(R.id.iv01);  
		        
	       // imageView.setImageBitmap(bitmap);  
	          } catch (FileNotFoundException e) {  
	            Log.e("Exception", e.getMessage(),e);  
	           }  
	      }  
		       super.onActivityResult(requestCode, resultCode, data);  
*/
  }  

}
