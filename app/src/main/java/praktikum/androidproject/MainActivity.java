package praktikum.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_archive = (Button) findViewById(R.id.button_start_archive_activity);
        Button btn_get = (Button) findViewById(R.id.button_start_get_activity);
        Button btn_post = (Button) findViewById(R.id.button_start_post_activity);


        // ClickListener implementieren für den Button zum Wechsel der Activity
        btn_archive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent archive_intent = new Intent(getApplicationContext(), ArchiveActivity.class);

                // Intent starten und zur zweiten Activity wechseln
                startActivity(archive_intent);

            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent get_intent = new Intent(getApplicationContext(), GetActivity.class);

                // Intent starten und zur zweiten Activity wechseln
                startActivity(get_intent);

            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent post_intent = new Intent(getApplicationContext(), PostActivity.class);

                // Intent starten und zur zweiten Activity wechseln
                startActivity(post_intent);

            }
        });

    }
}
