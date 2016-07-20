package praktikum.androidproject;

public class messageObject {

    private String timeStamp;
    private String message;
    private boolean preview;
    private String StringId;
    private long id;

    public messageObject(String timeStamp, String message, String StringId, long id) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.preview = false;
        this.StringId = StringId;
        this.id = id;
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

    public String getPreviewMessage() {
        String output = timeStamp + ":\n" + preview;

        return output;
    }

    @Override
    public String toString() {

        String output = "";

        if (this.preview) {
            if (this.message.length() > 25) {
                output = timeStamp + "\n" + message.substring(0,21) + "...";
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
