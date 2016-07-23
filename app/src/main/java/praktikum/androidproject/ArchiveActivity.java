package praktikum.androidproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import praktikum.androidproject.R;

public class ArchiveActivity extends AppCompatActivity {

    private static final String LOG_TAG = ArchiveActivity.class.getSimpleName();

    private messageDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        Log.d(LOG_TAG, "Creating database.");
        database = new messageDatabase(this);

        database.open();
        showAllListEntries();
        database.close();

    }

    private void showAllListEntries () {
        List<messageObject> messageObjectList = database.getAllMessageObjects();

        ArrayAdapter<messageObject> messageObjectArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_1,
                messageObjectList);

        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_archive_messages);
        shoppingMemosListView.setAdapter(messageObjectArrayAdapter);
    }
}