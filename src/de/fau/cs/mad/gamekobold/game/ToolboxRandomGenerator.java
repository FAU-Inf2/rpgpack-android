package de.fau.cs.mad.gamekobold.game;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ToolboxRandomGenerator extends Activity{
   
	private TextView contentView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toolbox_random);
        contentView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	DownloadTask task = new DownloadTask();
	            task.execute("http://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new");
	        }
	    });
        
    }
	
    private class DownloadTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... urls) {
                HttpResponse response = null;
                HttpGet httpGet = null;
                HttpClient mHttpClient = null;
                String s = "";

                try {
                    if(mHttpClient == null){
                        mHttpClient = new DefaultHttpClient();
                    }


                    httpGet = new HttpGet(urls[0]);


                    response = mHttpClient.execute(httpGet);
                    s = EntityUtils.toString(response.getEntity(), "UTF-8");


                } catch (IOException e) {
                    e.printStackTrace();
                } 
                return s;
            }

            @Override
            protected void onPostExecute(String result){
                contentView.setText(result);

            }
        }
}