package com.example.partialphoto;

import android.app.Activity;
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.util.DisplayMetrics;
import android.widget.ImageView; 
  
public class DrawViewFrame extends ImageView{  
    public DrawViewFrame(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
      
    Paint paint1 = new Paint();  
    {  
        paint1.setAntiAlias(true);  
        paint1.setColor(Color.GREEN);  
        paint1.setStyle(Style.STROKE);  
        paint1.setStrokeWidth(4f);
        paint1.setAlpha(100);  
    };  
    
    Paint paint2 = new Paint();  
    {  
        paint2.setAntiAlias(true);  
        paint2.setColor(Color.BLACK);  
        paint2.setStyle(Style.FILL); 
        paint2.setAlpha(150);  
    }; 
    
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        super.onDraw(canvas);  
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		int point_dw1 = (int)(width/10);
		int point_dh1 = (int)(5*height/12-height*RectPhotoActivity.frame/30);
		//int point_dh1 = (int)(height/3);
		
		int point_dw2 = width-point_dw1;
		int point_dh2 = height-point_dh1;
		
		//draw a rectangle around viewing frame
        canvas.drawRect(new Rect(point_dw1, point_dh1, point_dw2, point_dh2), paint1);
        
        //draw four translucent rectangles outside viewing frame
        canvas.drawRect(new Rect(0, 0, point_dw1, height), paint2);
        canvas.drawRect(new Rect(point_dw2, 0, width, height), paint2);
        canvas.drawRect(new Rect(point_dw1, 0, point_dw2, point_dh1), paint2);
        canvas.drawRect(new Rect(point_dw1, point_dh2, point_dw2, height), paint2);
        
    }  
  
}  