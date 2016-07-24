package praktikum.androidproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kay on 19.07.2016.
 */
public class PostActivity extends AppCompatActivity {

    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        findViewById(R.id.post_background).setBackgroundColor(getIntent().getIntExtra("Color",255));

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
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}