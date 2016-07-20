package praktikum.androidproject;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Kay on 20.07.2016.
 */
public class JsonParser {

    private String message;
    private JSONObject jsonobj;

    public JsonParser(String message) {
        this.message = message;

        try {
            jsonobj = new JSONObject(message);
        }
        catch (JSONException e) {e.printStackTrace();}

    }

    public messageObject getMessageObject() {

        messageObject result;

        Calendar calendar = new GregorianCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        String time = calendar.get(Calendar.YEAR) + "/" + month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "     "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        String msg = "";
        String id = "";

        try {
            msg = jsonobj.getString("msg");
            id = jsonobj.getString("_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        result = new messageObject(time, msg, id, 0);

        return result;
    }
}
