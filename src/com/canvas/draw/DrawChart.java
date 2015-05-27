package com.canvas.draw;


import java.util.ArrayList;
import java.util.List;

import com.example.facedetect.R;
import com.example.mainview.MainActivity;
import com.gem.hsx.sqlite.daliydata;
import com.gem.hsx.sqlite.userdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.canvas.draw.ResultShow;

public class DrawChart extends View{
	
	private int CHARTH = 600;
	private int CHARTW = 400;
	private int OFFSET_LEFT = 70;
	private int OFFSET_TOP = 80;
	private int TEXT_OFFSET = 20;
	private int X_INTERVAL = 50;
	private daliydata ddb;
    private Cursor	mCursor;
    private int Current_Page=1;
    private int sum_Page;
    private Button back;
    private Button move;
    private boolean first_draw=true;
	//private List<Point> plist;
    
	public class yandxname{
		Point p;
		String date;
	}
	
	private List<yandxname> plistH;
	private List<yandxname> plistM;
	
	public DrawChart(Context context) {
		super(context);
		plistH = new ArrayList<yandxname>();
		plistM = new ArrayList<yandxname>();
		initCurrentPage();
		prepareLine();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		drawCurve(canvas);
	}
	
	private void drawTable(Canvas canvas){
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		
		//鐢诲妗�
		paint.setStyle(Paint.Style.STROKE);
		//paint.setStrokeWidth(Pain);		
		Rect chartRec = new Rect(OFFSET_LEFT, OFFSET_TOP,CHARTW+OFFSET_LEFT, CHARTH+OFFSET_TOP);
		canvas.drawRect(chartRec, paint);
		
		
		//鐢诲乏杈圭殑鏂囧瓧
		Path textPath = new Path();
		paint.setStyle(Paint.Style.FILL);
		textPath.moveTo(30, 420);
		textPath.lineTo(30, 300);
		paint.setTextSize(15);
		paint.setAntiAlias(true);
		canvas.drawTextOnPath("健康指数", textPath, 0, 0, paint);
	
		
		//魅力指数杈圭殑鏂囧瓧
		Path textPathM = new Path();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);
		textPathM.moveTo(30, 540);
		textPathM.lineTo(30, 420);
		paint.setTextSize(15);
		paint.setAntiAlias(true);
		canvas.drawTextOnPath("魅力指数", textPathM, 0, 0, paint);
		
		
		
		//鐢诲乏渚ф暟瀛�
        canvas.drawText("100", OFFSET_LEFT - TEXT_OFFSET, OFFSET_TOP+5, paint);
        for(int i = 1 ; i<10 ; i++){
        	canvas.drawText(""+10*(10-i), OFFSET_LEFT - TEXT_OFFSET-5, OFFSET_TOP + CHARTH/10*i, paint);
        }
        canvas.drawText("0", OFFSET_LEFT - TEXT_OFFSET -10, OFFSET_TOP + CHARTH, paint);
        
        
        
        
        paint.setColor(Color.WHITE);
        //鐢昏〃鏍间腑鐨勮櫄绾�
        Path path = new Path();     
        PathEffect effects = new DashPathEffect(new float[]{2,2,2,2},1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(false);
        paint.setPathEffect(effects); 		
		for(int i = 1 ; i<10 ; i++){
			path.moveTo(OFFSET_LEFT, OFFSET_TOP + CHARTH/10*i);  
	        path.lineTo(OFFSET_LEFT+CHARTW,OFFSET_TOP + CHARTH/10*i); 
	        canvas.drawPath(path, paint);
			//canvas.drawLine(OFFSET_LEFT, OFFSET_TOP + CHARTH/10*i, OFFSET_LEFT+CHARTW, OFFSET_TOP + CHARTH/10*i, paint);			
			
		}
	}
	
	private void drawCurve(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		//canvas.drawLines(line, paint);
		
		if(plistH.size() >= 2){
			int i=0;
			for( i= (Current_Page-1)*7; i<Current_Page*7-1 && i<plistH.size()-1; i++){
				canvas.drawLine(plistH.get(i).p.x, plistH.get(i).p.y, plistH.get(i+1).p.x, plistH.get(i+1).p.y, paint);
				canvas.drawText(plistH.get(i).date, OFFSET_LEFT +(i%7)*X_INTERVAL,CHARTH+OFFSET_TOP+25, paint);
			}
		
			
			canvas.drawText(plistH.get(plistH.size()-1).date, OFFSET_LEFT +(i%7)*X_INTERVAL,CHARTH+OFFSET_TOP+25, paint);
			
		}
		
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		//canvas.drawLines(line, paint);
		
		if(plistM.size() >= 2){
			int i=0;
			for( i= (Current_Page-1)*7; i<Current_Page*7-1 && i<plistM.size()-1; i++){
				canvas.drawLine(plistM.get(i).p.x, plistM.get(i).p.y, plistM.get(i+1).p.x, plistM.get(i+1).p.y, paint);
				canvas.drawText(plistM.get(i).date, OFFSET_LEFT +(i%7)*X_INTERVAL,CHARTH+OFFSET_TOP+25, paint);
			}
		
			
			canvas.drawText(plistM.get(plistM.size()-1).date, OFFSET_LEFT +(i%7)*X_INTERVAL,CHARTH+OFFSET_TOP+25, paint);
			
		}
	}
	
	private void prepareLine(){
		int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
		Point p = new Point(OFFSET_LEFT + CHARTW , py );
		Context context=DrawChart.this.getContext();
		SharedPreferences sp = context.getSharedPreferences("map", 0);
		String current_user=sp.getString("username", "temp");
		ddb=new daliydata(context,"");
		ddb.settablename(current_user+"health");
		mCursor = ddb.select();
		//yandxname temp=new yandxname();
		if(mCursor.getCount()%7==0){
			sum_Page=mCursor.getCount()/7;
		}else{
			sum_Page=mCursor.getCount()/7+1;
		}
		
		
		if(mCursor.moveToFirst()){
			for(int i=0;i<mCursor.getCount();i++){
				yandxname temp=new yandxname();
				temp.p=new Point();
				temp.p.x=i%7*X_INTERVAL+OFFSET_LEFT+20;
				temp.p.y=(int)(mCursor.getInt(mCursor.getColumnIndex("health_value"))*20/100.0*(CHARTH - OFFSET_TOP))+OFFSET_TOP;
				temp.date=mCursor.getString(mCursor.getColumnIndex("date"));
				mCursor.moveToNext();
				plistH.add(temp);
			}
			
		}
		
		ddb.settablename(current_user+"beauty");
		mCursor = ddb.select();
		if(mCursor.moveToFirst()){
			for(int i=0;i<mCursor.getCount();i++){
				yandxname temp=new yandxname();
				temp.p=new Point();
				temp.p.x=i%7*X_INTERVAL+OFFSET_LEFT+20;
				temp.p.y=(int)(mCursor.getInt(mCursor.getColumnIndex("health_value"))/100.0*(CHARTH - OFFSET_TOP))+OFFSET_TOP;
				temp.date=mCursor.getString(mCursor.getColumnIndex("date"));
				mCursor.moveToNext();
				plistM.add(temp);
			}
			
		}


	}
	public void setCurrnt_Page(int page){
		Current_Page=page;
	}
	public int getCurrent_Page(){
		return Current_Page;
	}
	public void move_Page(){
		int setpage;
		int currrnt=getCurrent_Page();
		if(currrnt>=sum_Page)
		{
			setpage=sum_Page;
		}else{
			setpage=currrnt+1;
		}
		setCurrnt_Page(setpage);
	}
	public void back_Page(){
		int setpage;
		int currrnt=getCurrent_Page();
		if(currrnt<=1)
		{
			setpage=1;
		}else{
			setpage=currrnt-1;
		}
		setCurrnt_Page(setpage);
	}
	
	private void initCurrentPage(){
		Context context=DrawChart.this.getContext();
		ddb=new daliydata(context,"");
		SharedPreferences sp = context.getSharedPreferences("map", 0);
		String current_user=sp.getString("username", "temp");
		ddb.settablename(current_user+"health");
		mCursor = ddb.select();
		if(mCursor.getCount()%7==0){
			Current_Page=mCursor.getCount()/7;
		}else{
			Current_Page=mCursor.getCount()/7+1;
		}
		
		
		
	}
	

}
