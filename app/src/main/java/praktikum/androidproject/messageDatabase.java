package praktikum.androidproject;

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
            archiveDbHelper.COLUMN_STRING_ID
    };

    public messageDatabase(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new archiveDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        //database.delete("table_messages",null,null);
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public messageObject createMessageObject(String timeStamp, String message, String StringId) {
        ContentValues values = new ContentValues();

        Log.d(LOG_TAG, "Start createMessageObject.");

        Log.d(LOG_TAG, "Putting values.");
        values.put(archiveDbHelper.COLUMN_TIME, timeStamp);
        values.put(archiveDbHelper.COLUMN_MESSAGE, message);
        values.put(archiveDbHelper.COLUMN_STRING_ID, StringId);

        Log.d(LOG_TAG, "Inserting values.");
        long insertId = database.insert(archiveDbHelper.TABLE_MESSAGES, null, values);
        Log.d(LOG_TAG, "InsertID: " + insertId);

        Log.d(LOG_TAG, "Creating cursor.");
        Cursor cursor = database.query(archiveDbHelper.TABLE_MESSAGES,
                columns, archiveDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        Log.d(LOG_TAG, "moving cursor to first.");
        cursor.moveToFirst();

        Log.d(LOG_TAG, "calling cursorToMessageObject.");
        messageObject messageObj = cursorToMessageObject(cursor);

        Log.d(LOG_TAG, "closing cursor.");
        cursor.close();

        return messageObj;
    }

    private messageObject cursorToMessageObject(Cursor cursor) {
        Log.d(LOG_TAG, "getting Id's.");
        int idTime = cursor.getColumnIndex(archiveDbHelper.COLUMN_TIME);
        int idMessage = cursor.getColumnIndex(archiveDbHelper.COLUMN_MESSAGE);
        int idStringId = cursor.getColumnIndex(archiveDbHelper.COLUMN_STRING_ID);
        int idId = cursor.getColumnIndex(archiveDbHelper.COLUMN_ID);

        Log.d(LOG_TAG, "getting time-string." + idTime);
        String timeStamp = cursor.getString(idTime);
        Log.d(LOG_TAG, "getting message-string.");
        String message = cursor.getString(idMessage);
        String StringId = cursor.getString(idStringId);

        Log.d(LOG_TAG, "getting long id");
        long id = cursor.getLong(idId);

        Log.d(LOG_TAG, "creating messageobject");
        messageObject messageObj = new messageObject(id, message, StringId, timeStamp);

        return messageObj;
    }

    public List<messageObject> getAllMessageObjects(String sort) {
        List<messageObject> messageObjectList = new ArrayList<>();

        Cursor cursor = database.query(archiveDbHelper.TABLE_MESSAGES,
                columns, null, null, null, null, sort);

        cursor.moveToFirst();
        messageObject messageObj;

        while(!cursor.isAfterLast()) {
            messageObj = cursorToMessageObject(cursor);
            messageObj.setPreview(false);
            messageObjectList.add(messageObj);
            Log.d(LOG_TAG, "ID: " + messageObj.getId() + ", Inhalt: " + messageObj.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return messageObjectList;
    }

    public messageObject getLastMessage() {
        messageObject lastMessageObject;

        Cursor cursor = database.query(archiveDbHelper.TABLE_MESSAGES, columns,null, null, null, null, null);
        cursor.moveToLast();

        if (cursor.getCount() > 0) {
            lastMessageObject = cursorToMessageObject(cursor);
        } else {
            lastMessageObject = new messageObject(0,"Noch keine Nachricht in Datenbank gespeichert!", "null");
        }
        cursor.close();

        return lastMessageObject;
    }

}
