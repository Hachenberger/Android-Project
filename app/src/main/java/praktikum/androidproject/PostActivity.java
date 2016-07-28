package praktikum.androidproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        findViewById(R.id.post_background).setBackgroundColor(MainActivity.colorBackground);
        TextView textView=(TextView) findViewById(R.id.editText);
        textView.setTextColor(MainActivity.colorText);

        this.button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str2 = "";
                EditText edittext = (EditText)findViewById(R.id.editText);
                str2 = edittext.getText().toString();
                if (str2.length() > 0){
                    new DlAsync().execute(str2);
                }else {
                    Toast.makeText(getApplicationContext(), "Edittext == null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();

        MainActivity.changeColor = false;
    }

    public void onResume() {
        super.onResume();

        MainActivity.changeColor = true;
    }

    public void onPause(){
        super.onPause();

        if (MainActivity.changeColor) {

            Random rnd = new Random();

            int r = rnd.nextInt(255);
            int g = rnd.nextInt(255);
            int b = rnd.nextInt(255);

            MainActivity.colorBackground = Color.argb(255, r, g, b);

            findViewById(R.id.post_background).setBackgroundColor(MainActivity.colorBackground);


            r = rnd.nextInt(255);
            g = rnd.nextInt(255);
            b = rnd.nextInt(255);

            MainActivity.colorText = Color.argb(255, r, g, b);

            TextView textView=(TextView) findViewById(R.id.editText);
            textView.setTextColor(MainActivity.colorText);
        }
    }

    private class DlAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String str = "";
            try {
                url = new URL("http://app-imtm.iaw.ruhr-uni-bochum.de:3000/posts");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
                conn.setRequestProperty("Accept","application/json");

                JSONObject obj = new JSONObject();


                obj.put("msg",params[0]);

                byte[] bytes = obj.toString().getBytes("UTF-8");
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    str = conn.getResponseMessage() + "\n" + conn.getResponseCode();
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), R.string.toast_send_message, Toast.LENGTH_SHORT).show();
        }
    }
}