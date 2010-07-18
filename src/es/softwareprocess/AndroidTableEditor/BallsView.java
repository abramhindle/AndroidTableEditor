package es.softwareprocess.AndroidTableEditor;


import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

public class BallsView extends View implements View.OnTouchListener {
	String program = "AndroidBalls";
	String hostname = "192.168.0.242";
	Harbinger harb = null;
	int port = 30666;

	

    static int colorHelper(int r, int g, int b) {
    	int color = (0xFF << 24 | r << 16 | g << 8 | b);
    	return color;    
    }    
    static int randomColor() {
    	return colorHelper(((int)(Math.random()*255)), 
    					   ((int)(Math.random()*255)), 
    					   ((int)(Math.random()*255)));
    }
    class Ball {
    	private ShapeDrawable bDrawable;
    	private float bx;
    	private float by;
    	private int bcolor;
    	private int bradius;
    	public Ball(float x, float y, int radius, int color) {
    		bx = x;
    		by = y;
    		bradius = radius;        
    		if (color == 0) {    			
    			bcolor = BallsView.randomColor();
    		} else {
    			bcolor = color;    			
    		}
    		bDrawable = new ShapeDrawable(new OvalShape());
    		setColor( bcolor );
    		
    	}
    	
    	void setColor(int r, int g, int b) {
    		setColor( BallsView.colorHelper( r, g, b));
    	}
    	void setColor( int color ) {
    		bcolor = color;
            bDrawable.getPaint().setColor( bcolor );    		
    	}
    	void drawBall( Canvas canvas ) {    		
    		int x = (int)bx - bradius;
    		int y = (int)by - bradius;
    		int rad2 = bradius + bradius;
            bDrawable.setBounds(x, y, x + rad2, y + rad2);
            bDrawable.draw(canvas);
    	}
    	float distance( float x, float y) {
    		float dx = x - bx;
    		float dy = y - by;
    		return dx * dx + dy * dy;    		
    	}
    	void move(float x, float y) {
    		bx = x;
    		by = y;
    	}
    	void relMove(float x, float y) {
    		move( bx + x, by + y);
    	}    	
    }
    
    private int nballs = 0;
    private Ball[] balls = null;
    
    public BallsView(Context context) {
        super(context);
        nballs = 5;
        balls = new Ball[nballs];
        for (int i = 0 ; i < nballs; i ++) {
        	balls[i] = new Ball(50, 10 + 20 * i, 3+2*i , 0); //0xFF000000 | (int)(Math.random() * 21312312));
        }

        this.setOnTouchListener( this );
        this.setFocusable( true );        
        //this.setFocusableInTouchMode( this );
        String id = ""+randomColor();
        String destination = "";
        
        
        harb = new Harbinger(hostname, port, program, id, destination  );
        
    }

    protected void doSomething(float fx, float fy) {
    	//find nearest ball
    	// drag it half way here
    	
    	if (nballs > 0) {
    		Ball b = balls[0];
    		
    		float distance = b.distance(fx,fy);
    		// find nearest ball min distance
    		for (int i = 1; i < nballs; i++) {
    			float newdistance = balls[i].distance(fx, fy);
    			if (newdistance < distance) {
    				b = balls[i];
    				distance = newdistance;
    			}
    		}
    		// b is the selected ball
    		b.relMove( (float)((fx - b.bx) / 2.0), (float)((fy - b.by) / 2.0 ));
    		String msg = "Ball: " + b.bx + " " + b.by + " " + b.bcolor + " " + b.bradius; 
    		Log.v(TAG, msg);
    		try {
    			harb.sendMessage(msg);
    		} catch (IOException e) {
    			Log.v(TAG,"Harbinger UDP message failure: "+e.toString());    			
    		}
    	} else {
    		nballs = 1;
    		balls = new Ball[1];
    		balls[0] = new Ball(fx, fy, 5, -1);
    	}
    }
    
    protected void onDraw(Canvas canvas) {
    	for(int i = 0; i < nballs; i++) {
    		balls[i].drawBall( canvas );    		
    	}
    	
    }    
    public void	 onClick(View v) {    	
    }
    public boolean onLongClick(View v) {
    	return false;
    }
    
    public boolean onTouch(View v, MotionEvent e) {    	
    	Log.v(TAG, "Clicked at" + e.getX() + " "+ e.getY());
    	doSomething(e.getX(), e.getY());    	    	       	    	   
    	invalidate();
    	return true;
    }
    private static final String TAG = "test";
}
