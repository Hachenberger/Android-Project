package praktikum.androidproject;

public class messageObject {

    private String timeStamp;
    private String message;
    private String preview;
    private String StringId;
    private long id;

    public messageObject(String timeStamp, String message, String preview, String StringId, long id) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.preview = preview;
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

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
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
        String output = timeStamp + ":\n" + preview;

        return output;
    }

}
