package praktikum.androidproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView antwort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Ab hier von mir PW

        //Button und TextFeld
        Button get = (Button) findViewById(R.id.get);
        antwort = (TextView) findViewById(R.id.antwort);

        //Click Listener für Button get
        get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new getRequest().execute("http://app-imtm.iaw.ruhr-unibochum.de:3000/posts/random");
            }

        });


        //Beginn Test Sensor

        SensorManager sMgr;
        sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        Sensor motion;
        motion = sMgr.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);

        SensorEventListener mySensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                new getRequest().execute("http://app-imtm.iaw.ruhr-unibochum.de:3000/posts/random");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sMgr.registerListener(mySensorEventListener, motion, SensorManager.SENSOR_DELAY_NORMAL);

        //Ende Test Sensor




    }



public class getRequest extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... params) {

       HttpURLConnection verbindung = null;
       BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                verbindung = (HttpURLConnection) url.openConnection();
                verbindung.connect();
                InputStream stream = verbindung.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String zeile = "";
                    while((zeile = reader.readLine()) != null){
                    buffer.append(zeile);
                    }
                    return buffer.toString();
                } catch (MalformedURLException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                }finally {
                if (verbindung != null) {
                verbindung.disconnect();
                }
                try {
                if (reader != null) {
                reader.close();
                }
                } catch (IOException e) {
                e.printStackTrace();
                }
                }
                return null;
                }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            antwort.setText(result);


        }
    }
}
