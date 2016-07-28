package praktikum.androidproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import java.util.Random;


public class ArchiveActivity extends AppCompatActivity {

    private static final String LOG_TAG = ArchiveActivity.class.getSimpleName();
    private messageDatabase database;
    private String sort;
    private ListView messageObjectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        final Button btn_time = (Button) findViewById(R.id.btn_archive_sort_by_time);
        final Button btn_message = (Button) findViewById(R.id.btn_archive_sort_by_message);
        final Button btn_id = (Button) findViewById(R.id.btn_archive_sort_by_id);

        messageObjectListView = (ListView) findViewById(R.id.listview_archive_messages);
        messageObjectListView.setClickable(true);

        messageObjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = messageObjectListView.getItemAtPosition(position);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, o.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        findViewById(R.id.archive_background).setBackgroundColor(MainActivity.colorBackground);

        sort = "UPPER(" + archiveDbHelper.COLUMN_TIME + ")";
        btn_time.setBackgroundResource(R.drawable.selected_button_layout);

        Log.d(LOG_TAG, "Creating database.");
        database = new messageDatabase(this);

        showAllListEntries();

        btn_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_time.setBackgroundResource(R.drawable.selected_button_layout);
                btn_message.setBackgroundResource(R.drawable.button_layout);
                btn_id.setBackgroundResource(R.drawable.button_layout);
                sort = "UPPER(" + archiveDbHelper.COLUMN_TIME + ")";
                database.open();
                showAllListEntries();
                database.close();
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_message.setBackgroundResource(R.drawable.selected_button_layout);
                btn_time.setBackgroundResource(R.drawable.button_layout);
                btn_id.setBackgroundResource(R.drawable.button_layout);
                sort = "UPPER(" + archiveDbHelper.COLUMN_MESSAGE + ")";
                database.open();
                showAllListEntries();
                database.close();
            }
        });

        btn_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_id.setBackgroundResource(R.drawable.selected_button_layout);
                btn_message.setBackgroundResource(R.drawable.button_layout);
                btn_time.setBackgroundResource(R.drawable.button_layout);
                sort = "UPPER(" + archiveDbHelper.COLUMN_STRING_ID + ")";
                database.open();
                showAllListEntries();
                database.close();
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
            int color = Color.argb(255, r, g, b);

            findViewById(R.id.archive_background).setBackgroundColor(color);
            MainActivity.colorBackground = color;

            r = rnd.nextInt(255);
            g = rnd.nextInt(255);
            b = rnd.nextInt(255);
            color = Color.argb(255, r, g, b);

            MainActivity.colorText = color;
            showAllListEntries();
        }
    }

    private void showAllListEntries () {
        database.open();
        List<messageObject> messageObjectList = database.getAllMessageObjects(sort);
        database.close();
        ArrayAdapter<messageObject> messageObjectArrayAdapter = new ArrayAdapter<messageObject> (
                this,
                android.R.layout.simple_list_item_1,
                messageObjectList){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(MainActivity.colorText);

                return view;
            }
        };
        messageObjectListView.setAdapter(messageObjectArrayAdapter);
    }
}