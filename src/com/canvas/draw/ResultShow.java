package com.canvas.draw;


import com.example.facedetect.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ResultShow extends Activity {
	
	private Handler mHandler;
	private DrawChart view;
    private ImageButton back;
    private ImageButton move;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_result);
        
        init();
    }
    
    private void init() {  
        LinearLayout layout=(LinearLayout) findViewById(R.id.root);  
        view = new DrawChart(this);  
      
        view.invalidate();  
        layout.addView(view);
		back=(ImageButton)findViewById(R.id.imageButton1);
		move=(ImageButton)findViewById(R.id.imageButton2);
		
		back.setOnClickListener( new OnClickListener() { 
			public void onClick(View v) { 
		        view.back_Page();
				view.invalidate();  
			} 
		});
		
		move.setOnClickListener( new OnClickListener() { 
			public void onClick(View v) { 
				view.move_Page();
				view.invalidate();  
			} 
		});
    	//mHandler = new Handler();
		//mHandler.post(new TimerProcess());
          
    }  
    
	private class TimerProcess implements Runnable {
		public void run() {
			view.invalidate();
			mHandler.postDelayed(this, 1000);
		}
	}
	
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
