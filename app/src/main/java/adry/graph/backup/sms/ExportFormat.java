package adry.graph.backup.sms;

/**
 * Created by Audrey on 22/05/2016.
 * enumeration of existing app format for sms export
 */
public enum ExportFormat {
    CONVERSION_TYPE_TEXT("TEXT", "txt"),
    CONVERSION_TYPE_CSV("CSV", "csv"),
    CONVERSION_TYPE_JSON("JSON", "json"),
    CONVERSION_TYPE_HTML("HTML", "html");

    private final String mLabel;
    private final String mExtension;

    ExportFormat(String text, String ext) {
        mLabel = text;
        mExtension = ext;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getExtension() {
        return mExtension;
    }
}
