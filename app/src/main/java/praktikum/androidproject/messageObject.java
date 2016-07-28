package praktikum.androidproject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class messageObject {

    private String timeStamp;
    private String message;
    private boolean preview;
    private String StringId;
    private long id;

    public messageObject(long id, String... params) {

        this.id = id;
        this.message = params[0];
        this.StringId = params[1];
        this.preview = false;

        if (params.length > 2) {
            this.timeStamp = params[2];
        } else {
            this.timeStamp = makeTimeStamp();
        }
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public String getStringId() {
        return StringId;
    }

    public void setStringId(String StringId) {
        this.StringId = StringId;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String makeTimeStamp() {

        Calendar calendar = new GregorianCalendar();
        int month = calendar.get(Calendar.MONTH) + 1;
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String mins;
        if(minutes < 10) {
            mins = "0" + minutes;
        } else {
            mins = String.valueOf(minutes);
        }

        String sec;
        if(seconds < 10) {
            sec = "0" + seconds;
        } else {
            sec = String.valueOf(seconds);
        }

        String time = calendar.get(Calendar.YEAR) + "/" + month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "     "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":" + mins + ":" + sec;

        return time;
    }

    @Override
    public String toString() {

        String output = "";

        if (this.preview) {
            if (this.message.length() > 100) {
                output = timeStamp + "\n" + message.substring(0,96) + "...";
            } else {
                output = timeStamp + "\n" + message;
            }
        }
        else {
            output = timeStamp + "\n" + message;
        }

        return output;
    }
}
