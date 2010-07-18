package es.softwareprocess.AndroidTableEditor;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;


public class AndroidTableEditor extends Activity {
	String program = "AndroidTableEditor";
	String hostname = "192.168.0.242";
	int port = 30666;
	Harbinger harb = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        //setContentView(R.layout.main);
                
        String id = "";
        String destination = "";
                
        harb = new Harbinger(hostname, port, program, id, destination  );
        
        Timer timer = new Timer("GraphViewUpadtes");
        
        GrapherTask gt = new GrapherTask(harb);
                
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT,1);

        ll.setOrientation(LinearLayout.VERTICAL);
            //android:layout_width="fill_parent"
            //android:layout_height="fill_parent"
            //android:layout_weight="1">

        for (int i = 0; i < 4; i++) {
        	GrapherView gv = new GrapherView( this );
        	gt.add(gv);
        	ll.addView( gv, lp );
        }
        timer.scheduleAtFixedRate(gt,100,100);
        setContentView(ll);
        
    }    
    class GrapherTask extends TimerTask {
        private static final String TAG = "GrapherTask";
    	Harbinger harb = null;
    	GrapherTask(Harbinger harb) {
    		this.harb = harb;
    	}
    	Collection<GrapherView> gvs = new LinkedList<GrapherView>();
    	public void add(GrapherView gv) {
    		gvs.add(gv);    		
    	}
    	public void run() {
    		int i = 0;
    		for (GrapherView gv : gvs) {
    			if (gv.changed()) {
    				StringBuffer b = new StringBuffer(12*128);
    				b.append(i+": ");
    				double [] da = gv.dataArray();
    				for (double d: da) {
    					b.append(d+" ");    					
    				}
    				try {
    					harb.sendMessage(b.toString());    					
    				} catch (IOException e) {
    					Log.v(TAG,"Harbinger UDP message failure: "+e.toString());    			
    				}
    				gv.setChanged(false);
    			}
    			i++;
    		}
    	}    	
    }
}
