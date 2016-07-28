package praktikum.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class messageDatabase {

    private SQLiteDatabase database;
    private archiveDbHelper dbHelper;

    private String[] columns = {
            archiveDbHelper.COLUMN_ID,
            archiveDbHelper.COLUMN_TIME,
            archiveDbHelper.COLUMN_MESSAGE,
            archiveDbHelper.COLUMN_STRING_ID
    };

    public messageDatabase(Context context) {
        dbHelper = new archiveDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        //database.delete("table_messages",null,null);
    }

    public void close() {
        dbHelper.close();
    }

    public messageObject createMessageObject(String timeStamp, String message, String StringId) {
        ContentValues values = new ContentValues();

        values.put(archiveDbHelper.COLUMN_TIME, timeStamp);
        values.put(archiveDbHelper.COLUMN_MESSAGE, message);
        values.put(archiveDbHelper.COLUMN_STRING_ID, StringId);

        long insertId = database.insert(archiveDbHelper.TABLE_MESSAGES, null, values);

        Cursor cursor = database.query(
                            archiveDbHelper.TABLE_MESSAGES,
                            columns,
                            archiveDbHelper.COLUMN_ID + "=" + insertId,
                            null, null, null, null
                            );

        cursor.moveToFirst();
        messageObject messageObj = cursorToMessageObject(cursor);
        cursor.close();

        return messageObj;
    }

    private messageObject cursorToMessageObject(Cursor cursor) {

        int idTime = cursor.getColumnIndex(archiveDbHelper.COLUMN_TIME);
        int idMessage = cursor.getColumnIndex(archiveDbHelper.COLUMN_MESSAGE);
        int idStringId = cursor.getColumnIndex(archiveDbHelper.COLUMN_STRING_ID);
        int idId = cursor.getColumnIndex(archiveDbHelper.COLUMN_ID);

        String timeStamp = cursor.getString(idTime);
        String message = cursor.getString(idMessage);
        String StringId = cursor.getString(idStringId);
        long id = cursor.getLong(idId);

        messageObject messageObj = new messageObject(id, message, StringId, timeStamp);

        return messageObj;
    }

    public List<messageObject> getAllMessageObjects(String sort) {

        List<messageObject> messageObjectList = new ArrayList<>();

        Cursor cursor = database.query(
                            archiveDbHelper.TABLE_MESSAGES,
                            columns,
                            null, null, null, null,
                            sort
                            );

        cursor.moveToFirst();

        messageObject messageObj;

        while(!cursor.isAfterLast()) {

            messageObj = cursorToMessageObject(cursor);
            messageObjectList.add(messageObj);

            cursor.moveToNext();
        }

        cursor.close();

        return messageObjectList;
    }

    public messageObject getLastMessage() {

        messageObject lastMessageObject;

        Cursor cursor = database.query(
                            archiveDbHelper.TABLE_MESSAGES,
                            columns,
                            null, null, null, null, null
                            );
        cursor.moveToLast();

        if (cursor.getCount() > 0) {
            lastMessageObject = cursorToMessageObject(cursor);
        } else {
            lastMessageObject = new messageObject(0, String.valueOf(R.string.last_message_default), "null");
        }
        cursor.close();

        return lastMessageObject;
    }

}
