package praktikum.androidproject;


import org.json.JSONException;
import org.json.JSONObject;

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

        String msg = "";
        String id = "";

        try {
            msg = jsonobj.getString("msg");
            id = jsonobj.getString("_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        result = new messageObject(0, msg, id);

        return result;
    }
}
