package praktikum.androidproject;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView antwort;

    private messageDatabase database;
    private messageObject msgObj;

    public static int colorBackground;
    public static int colorText;

    private SensorManager sensorManager;
    private Sensor proxSensor;
    private Sensor accelerometer;
    private float accel;
    private float accelAkt;
    private float accelVor;
    private long lastSensorChangeTime = System.currentTimeMillis();

    private boolean save;
    public static boolean changeColor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_archive = (Button) findViewById(R.id.button_start_archive_activity);
        Button btn_post = (Button) findViewById(R.id.button_start_post_activity);
        antwort = (TextView) findViewById(R.id.antwort);

        colorBackground = Color.WHITE;
        colorText = Color.BLACK;

        changeColor = false;
        save = true;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        accel = 0.00f;
        accelAkt = SensorManager.GRAVITY_EARTH;
        accelVor = SensorManager.GRAVITY_EARTH;

        database = new messageDatabase(this);

        database.open();
        msgObj = database.getLastMessage();
        antwort.setText(msgObj.toString());
        database.close();


        btn_archive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent archive_intent = new Intent(getApplicationContext(), ArchiveActivity.class);

                changeColor = false;

                startActivity(archive_intent);

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent post_intent = new Intent(getApplicationContext(), PostActivity.class);

                changeColor = false;

                startActivity(post_intent);

            }
        });
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {

        if (System.currentTimeMillis() - lastSensorChangeTime > 3000) {

            if (event.sensor == proxSensor && event.values[0] == 0 && save) {

                database.open();
                database.createMessageObject(msgObj.getTimeStamp(), msgObj.getMessage(), msgObj.getStringId());
                database.close();

                Toast.makeText(getApplicationContext(), R.string.toast_save_message, Toast.LENGTH_SHORT).show();

                save = false;

            } else if (event.sensor == proxSensor && event.values[0] > 0) {

                save = true;
                lastSensorChangeTime = System.currentTimeMillis();

            } else if (event.sensor == accelerometer && event.values.length > 2) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                accelVor = accelAkt;
                accelAkt = (float) Math.sqrt((double) (x * x + y * y + z * z));

                float delta = accelAkt - accelVor;

                accel = accel * 0.9f + delta;

                if (accel > 10) {

                    new MainActivity.getRequest().execute("http://app-imtm.iaw.ruhr-unibochum.de:3000/posts/random");
                    lastSensorChangeTime = System.currentTimeMillis();

                }
            }
        }
    }

    protected void onResume() {

        super.onResume();
        sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        findViewById(R.id.main_background).setBackgroundColor(colorBackground);

        TextView textView=(TextView) findViewById(R.id.antwort);
        textView.setTextColor(MainActivity.colorText);

        changeColor = true;
    }



    public void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);

        if (changeColor) {

            Random rnd = new Random();

            int r = rnd.nextInt(255);
            int g = rnd.nextInt(255);
            int b = rnd.nextInt(255);

            colorBackground = Color.argb(255, r, g, b);

            findViewById(R.id.main_background).setBackgroundColor(colorBackground);

            r = rnd.nextInt(255);
            g = rnd.nextInt(255);
            b = rnd.nextInt(255);

            colorText = Color.argb(255, r, g, b);

            TextView textView=(TextView) findViewById(R.id.antwort);
            textView.setTextColor(colorText);

        }
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

            JsonParser jsonParser = new JsonParser(result);

            msgObj = jsonParser.getMessageObject();

            antwort.setText(msgObj.toString());

            Toast.makeText(getApplicationContext(), R.string.toast_get_message, Toast.LENGTH_SHORT).show();

        }
    }
}