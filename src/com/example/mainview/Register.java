package com.example.mainview;

import com.example.facedetect.R;
import com.example.facedetect.R.layout;
import com.example.facedetect.R.menu;
import com.gem.hsx.sqlite.userdb;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

private Button register;

private EditText username;
private EditText password;
private EditText others;
private EditText age;
private RadioButton male;
private userdb udb;
private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

        setContentView(R.layout.register);
        register=(Button)findViewById(R.id.button1);
        register.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		username=(EditText)findViewById(R.id.editText1);
        		password=(EditText)findViewById(R.id.editText2);
        		age=(EditText)findViewById(R.id.editText3);
        		others=(EditText)findViewById(R.id.editText4);
        		male=(RadioButton)findViewById(R.id.radioButton1);
        		int gender=0;
        		if(male.isChecked())
        		{
        			gender=1;
        		}
        		udb=new userdb(Register.this);
   			 	mCursor = udb.select(); 
   			 	boolean flag=false;
   			       if(mCursor.moveToFirst()){//判断游标是否为空

 		               for(int i=0;i<mCursor.getCount();i++){

	 		            	
 		            	
 		               
	 		               final String name=mCursor.getString(mCursor.getColumnIndex("username"));
	 		               
	 		               if(username.getText().toString().equals(name)){
	 		            	   flag=true;
	 		               }
	 		              mCursor.moveToNext();//移动到指定记录
 		                }
   			       }
	 		        if(flag){
	 		        	Toast.makeText(Register.this, "该用户名已存在", Toast.LENGTH_SHORT).show();
	 		        }else{
	 		        			 		   
		        		if(username.getText().length()!=0&&password.getText().length()!=0 && age.getText().length()!=0
		        				&&others.getText().length()!=0)
		        		{
		        			udb.insert(username.getText().toString(),password.getText().toString(),gender,others.getText().toString(),Integer.parseInt(age.getText().toString()));
		        			Context ctx = Register.this; 
		        			 Intent intent=new Intent(Register.this,MainActivity.class);
		        			SharedPreferences sp = ctx.getSharedPreferences("map", MODE_PRIVATE);
		        			Editor editor = sp.edit();
		        			editor.putString("username",username.getText().toString());
		        			editor.putString("password",password.getText().toString());
		        			editor.commit();
		        			
		        			startActivity(intent);
		        			
		        		}else{
		        			Toast.makeText(Register.this, "信息没填完整", Toast.LENGTH_SHORT).show();
		        		}
	 		       }
        		
        	}
        });

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_signin, menu);
		return true;
	}

}
