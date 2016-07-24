package praktikum.androidproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView antwort;
    private messageDatabase database;
    private static final String LOG_TAG = GetActivity.class.getSimpleName();
    private int color;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random rnd = new Random();
        int r = rnd.nextInt(255);
        int g = rnd.nextInt(255);
        int b = rnd.nextInt(255);
        color = Color.argb(255,r,g,b);
        findViewById(R.id.main_background).setBackgroundColor(color);

        database = new messageDatabase(this);

        Button btn_archive = (Button) findViewById(R.id.button_start_archive_activity);
        //Button btn_get = (Button) findViewById(R.id.button_start_get_activity);
        Button btn_post = (Button) findViewById(R.id.button_start_post_activity);

        //Button und TextFeld
        Button get = (Button) findViewById(R.id.get);
        antwort = (TextView) findViewById(R.id.antwort);

        //Click Listener für Button get
        get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new MainActivity.getRequest().execute("http://app-imtm.iaw.ruhr-unibochum.de:3000/posts/random");
            }

        });

        // ClickListener implementieren für den Button zum Wechsel der Activity
        btn_archive.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    //Neues Intent anlegen
                    Intent archive_intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                    archive_intent.putExtra("Color", color);

                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(archive_intent);

                }
        });

/*            btn_get.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    //Neues Intent anlegen
                    Intent get_intent = new Intent(getApplicationContext(), GetActivity.class);

                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(get_intent);

                }
            });
*/
        btn_post.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                    //Neues Intent anlegen
                    Intent post_intent = new Intent(getApplicationContext(), PostActivity.class);
                    post_intent.putExtra("Color", color);

                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(post_intent);

            }
        });

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

            messageObject msgObj = jsonParser.getMessageObject();

            antwort.setText(msgObj.toString());

            database.open();
            messageObject msg = database.createMessageObject(msgObj.getTimeStamp(), msgObj.getMessage(), msgObj.getStringId());
            database.close();

        }
    }
}