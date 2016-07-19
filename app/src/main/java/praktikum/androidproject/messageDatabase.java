package praktikum.androidproject;

/**
 * Created by Kay on 19.07.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class messageDatabase {

    private static final String LOG_TAG = messageDatabase.class.getSimpleName();

    private SQLiteDatabase database;
    private archiveDbHelper dbHelper;

    private String[] columns = {
            archiveDbHelper.COLUMN_ID,
            archiveDbHelper.COLUMN_TIME,
            archiveDbHelper.COLUMN_MESSAGE,
            archiveDbHelper.COLUMN_PREVIEW,
            archiveDbHelper.COLUMN_STRING_ID
    };

    public messageDatabase(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new archiveDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public messageObject createMessageObject(String timeStamp, String message, String StringId) {
        ContentValues values = new ContentValues();

        String preview = "";
        if (message.length() > 100) {
            preview = message.substring(0,96) + "...";
        } else {
            preview = message;
        }

        values.put(archiveDbHelper.COLUMN_TIME, timeStamp);
        values.put(archiveDbHelper.COLUMN_MESSAGE, message);
        values.put(archiveDbHelper.COLUMN_PREVIEW, preview);
        values.put(archiveDbHelper.COLUMN_STRING_ID, StringId);

        long insertId = database.insert(archiveDbHelper.TABLE_MESSAGES, null, values);

        Cursor cursor = database.query(archiveDbHelper.TABLE_MESSAGES,
                columns, archiveDbHelper.COLUMN_TIME + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        messageObject messageObj = cursorToMessageObject(cursor);
        cursor.close();

        return messageObj;
    }

    private messageObject cursorToMessageObject(Cursor cursor) {
        int idTime = cursor.getColumnIndex(archiveDbHelper.COLUMN_TIME);
        int idMessage = cursor.getColumnIndex(archiveDbHelper.COLUMN_MESSAGE);
        int idPreview = cursor.getColumnIndex(archiveDbHelper.COLUMN_PREVIEW);
        int idStringId = cursor.getColumnIndex(archiveDbHelper.COLUMN_STRING_ID);
        int idId = cursor.getColumnIndex(archiveDbHelper.COLUMN_ID);

        String timeStamp = cursor.getString(idTime);
        String message = cursor.getString(idMessage);
        String preview = cursor.getString(idPreview);
        String StringId = cursor.getString(idStringId);
        long id = cursor.getLong(idId);

        messageObject messageObj = new messageObject(timeStamp, message, preview, StringId, id);

        return messageObj;
    }

    public List<messageObject> getAllMessageObjects() {
        List<messageObject> messageObjectList = new ArrayList<>();

        Cursor cursor = database.query(archiveDbHelper.TABLE_MESSAGES,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        messageObject messageObj;

        while(!cursor.isAfterLast()) {
            messageObj = cursorToMessageObject(cursor);
            messageObjectList.add(messageObj);
            Log.d(LOG_TAG, "ID: " + messageObj.getId() + ", Inhalt: " + messageObj.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return messageObjectList;
    }
}
