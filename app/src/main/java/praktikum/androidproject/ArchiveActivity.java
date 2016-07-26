package praktikum.androidproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import praktikum.androidproject.R;

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

        //LinearLayout archiveLayout = (LinearLayout) findViewById(R.id.archive_background);
        findViewById(R.id.archive_background).setBackgroundColor(getIntent().getIntExtra("Color",255));

        sort = "UPPER(" + archiveDbHelper.COLUMN_TIME + ")";
        btn_time.setBackgroundResource(R.drawable.selected_button_layout);

        Log.d(LOG_TAG, "Creating database.");
        database = new messageDatabase(this);

        database.open();
        showAllListEntries();
        database.close();

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

    private void showAllListEntries () {
        List<messageObject> messageObjectList = database.getAllMessageObjects(sort);

        ArrayAdapter<messageObject> messageObjectArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_1,
                messageObjectList);

        messageObjectListView.setAdapter(messageObjectArrayAdapter);
    }
}