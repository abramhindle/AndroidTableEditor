package es.softwareprocess.AndroidTableEditor;


import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.graphics.Color;

public class GrapherView extends View implements View.OnTouchListener {

	static int colorHelper(int r, int g, int b) {
		int color = (0xFF << 24 | r << 16 | g << 8 | b);
		return color;
	}

	static int randomColor() {
		return colorHelper(((int) (Math.random() * 255)),
				((int) (Math.random() * 255)), ((int) (Math.random() * 255)));
	}

	public GrapherView(Context context) {
		super(context);

		this.setOnTouchListener(this);
		this.setFocusable(true);
		
		changeDataSize(min, max, start, end);
		// String id = ""+randomColor();
		// String destination = "";

		// harb = new Harbinger(hostname, port, program, id, destination );

	}

	protected void doSomething(float fx, float fy) {
	}

	protected void onDraw(Canvas canvas) {
		// draw here
		paintComponent(canvas);
	}

	public void onClick(View v) {
	}

	public boolean onLongClick(View v) {
		return false;
	}
//
//	public void mousePressed(MouseEvent e) { // Invoked when a mouse button has
//		// been pressed on a component.
//		if (e.getButton() != e.BUTTON1) {
//			maybeShowPopup(e);
//			dragOn = false;
//		} else {
//			dragOn = true;
//			lastX = e.getX();
//			lastY = e.getY();
//		}
//	}
//
//	public void mouseReleased(MouseEvent e) { // Invoked when a mouse button has
//		// been released on a component.
//		if (e.getButton() != e.BUTTON1) {
//			maybeShowPopup(e);
//		} else {
//			int x = e.getX();
//			int y = e.getY();
//			dragOn = false;
//			interpolate(x, y, lastX, lastY);
//			dragged = false;
//			repaint();
//		}
//	}

	
	
	public boolean onTouch(View v, MotionEvent e) {
		Log.v(TAG, "Clicked at" + e.getX() + " " + e.getY());

		int action = e.getAction();
		if (action == android.view.MotionEvent.ACTION_DOWN) { // the start
			dragOn = true;
			lastX = e.getX();
			lastY = e.getY();
		} else if (action == android.view.MotionEvent.ACTION_MOVE) { // the middle
			// gutted some drag code
		
			//int width = (int) getWidth();
			//int height = (int)getHeight();
			dragged = true;
			dragOn = true;
			double x = e.getX();
			double y = e.getY();
			interpolate(x, y, lastX, lastY);
			lastX = x;
			lastY = y;
			repaint();
		} else {
			double x = e.getX();
			double y = e.getY();
			dragOn = false;
			interpolate(x, y, lastX, lastY);
			dragged = false;
			repaint();	
		}
		
		
		//invalidate();
		return true;
	}

	private static final String TAG = "test";

	// imported from Grapher
	double min = -1;
	double max = 1;
	int start = 0;
	int end = 128;
	boolean changed = false;
	private double[] data = null;
	int bgColor = Color.WHITE;
	int fgColor = Color.BLACK;
	int divider = Color.BLUE;
	int hdivider = Color.RED;
	boolean dragOn = false;
	boolean dragged = false;
	double lastX = 0;
	double lastY = 0;

	
	
	public void zeroData() {
		double v = 0.0;
		if (min > 0) {
			v = min;
		}
		for (int i = 0; i < data.length; i++) {
			data[i] = v;
		}
		changed = true;
	}

	public int numElements() {
		return 1 + end - start;
	}

	private void newData() {
		data = new double[numElements()];
	}

	void setData(double[] d) {
		data = d;
	}

	void setData(Vector<Double> d) {
		Iterator<Double> iter = d.iterator();
		int index = 0;
		data = new double[d.size()];
		while (iter.hasNext()) {
			Double doub = (Double) (iter.next());
			data[index++] = doub.doubleValue();
		}
		changed = true;
		repaint();
	}

	double[] getData() {
		return data;
	}

	public void setStart(int start) {
		changeDataSize(min, max, start, end);
	}

	public void setEnd(int end) {
		changeDataSize(min, max, start, end);
	}

	public void setMin(double min) {
		changeDataSize(min, max, start, end);
	}

	public void setMax(double max) {
		changeDataSize(min, max, start, end);
	}

	public void setStartS(int start) {
		this.start = start;
	}

	public void setEndS(int end) {
		this.end = end;
	}

	public void setMinS(double min) {
		this.min = min;
	}

	public void setMaxS(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getDataSize() {
		return data.length;
	}

	public boolean changed() {
		return changed;
	}

	public void setChanged(boolean k) {
		changed = k;
	}

	public void loadData(double[] k) {
		System.arraycopy(data, 0, k, 0, data.length);
	}

	public double[] dataArray() {
		return data;
	}

	public void changeDataSize(double min, double max, int start, int end) {
		this.min = min;
		this.max = max;
		this.start = start;
		this.end = end;
		newData();
		zeroData();
		repaint();
	}

	protected void paintComponent(Canvas g) {
		int width = (int) getWidth();
		int height = (int)getHeight();
		//g.setColor(bgColor);
		g.drawColor( bgColor );
		Paint dividerPaint = new Paint();
		dividerPaint.setColor( divider );
		Paint hdividerPaint = new Paint();
		hdividerPaint.setColor( hdivider );
		Paint fgColorPaint = new Paint();
		fgColorPaint.setColor( fgColor );

		for (int i = 0; i <= 4; i++) {
			int x = i * width / 4;
			g.drawLine((float)x, 0, (float)x, (float)height, dividerPaint);
		}
		float vheight = ((float) max - (float) min);
		if (min <= 0) {
			
			float y = (float) (height * (0 - min) / (vheight));
			g.drawLine(0, (float) (height - 1) - y, (float) width - 1, (float) (height - 1) - y, hdividerPaint);
		}
		
		//double nextval = 0;
		//double val = 0;
		int dlength = data.length;
		for (int i = 0; i < (dlength - 1); i++) { // fast for smaller tables
			double reli = i / (1.0 * (dlength - 1));
			double reli2 = (i + 1) / (1.0 * (dlength - 1));
			float y1 = height - (float) (height * ((data[i] - min) / (vheight)));
			float y2 = height
					- (float) (height * ((data[i + 1] - min) / (vheight)));
			g.drawLine((float) (reli * width), y1, (float) (reli2 * width), y2, fgColorPaint);
		}
	}



	private void interpolate(double x, double y, double lastX2, double lastY2) {		
		int width = (int) getWidth();
		int height = (int) getHeight();
		double reloldx = lastX2 / (width - 1.0);
		double reloldy = 1 - lastY2 / (height - 1.0);
		double relx = x / (width - 1.0);
		double rely = 1 - y / (height - 1.0);
		if (relx < reloldx) { // switch to max min
			double tmp = relx;
			relx = reloldx;
			reloldx = tmp;
			tmp = rely;
			rely = reloldy;
			reloldy = tmp;
		}
		// interpolate from lastX to currentX
		//double rise = rely - reloldy;
		//double run = relx - reloldx;
		//double slope = rise / run; // slope is relative
		int sindex = (int) (reloldx * (data.length));
		int eindex = (int) (relx * (data.length));
		//int rrun = eindex - sindex;
		//double rrise = (max - min) * rise;
		//double rslope = rrise / rrun;
		if (sindex < 0) {
			sindex = 0;
		}
		if (eindex >= data.length) {
			eindex = data.length - 1;
		}
		if (eindex == sindex) {

		}
		// interpolate
		int length = eindex - sindex;
		if (length >= 0) {
			length = 1;
		}
		// data[i+sindex] = reloldy + i/length*rrise;
		for (int i = 0; i <= (eindex - sindex); i++) { // bad?
			data[i + sindex] = rely * (max - min) + min;
		}
		changed = true;
	}

	//public void mouseDragged(MouseEvent e) { // Invoked when a mouse button is
	//	// pressed on a component and
	//	// then dragged.
	//	if (!dragOn) {
//
	//	} else {
	//	}
	//}

	public StringBuffer getXMLOut() {
		StringBuffer buff = new StringBuffer();
		return getXMLOut(buff);
	}

	public StringBuffer getXMLOut(StringBuffer buff) {
		buff.append("<Grapher>");
		buff.append("<min>" + min + "</min>");
		buff.append("<max>" + max + "</max>");
		buff.append("<start>" + start + "</start>");
		buff.append("<end>" + end + "</end>");
		buff.append("<data>");
		for (int i = 0; i < data.length; i++) {
			buff.append("<d>" + data[i] + "</d>");
		}
		buff.append("</data>");
		buff.append("</Grapher>");
		return buff;
	}

//	private void maybeShowPopup(MouseEvent e) {
//		if (e.isPopupTrigger()) {
//			PopupFactory.popup(e, this);
//		}
//	}

	
	//public void runAlgo(GrapherAlgo g) {
	//	g.runAlgo(this);
	//	repaint();
	//	setChanged(true);
	//}
	protected void repaint() {
		invalidate();
	}
	

}
