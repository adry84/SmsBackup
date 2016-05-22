package adry.graph.backup.sms;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Audrey on 22/05/2016.
 * Class with static method to convert SmsData
 */
public class SMSDataUtils {

    private static final String CSV_SEPARATOR_FIELD = ";";
    private static final String CSV_SEPARATOR_LINE = "\n";

    private static final String TXT_SEPARATOR_LINE = "\n";

    public static String SMSDataToText(SMSData smsData) throws Exception {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        StringBuilder sb = new StringBuilder();
        if (smsData.isMessageTypeSent()) {
            sb.append("Sent Message").append(TXT_SEPARATOR_LINE);
        }
        sb.append(smsData.getNumber()).append(TXT_SEPARATOR_LINE);
        if (smsData.getDateReceived() != 0L) {
            sb.append("Received date ").append(dateFormatter.format(smsData.getDateReceived())).append(TXT_SEPARATOR_LINE);
        }
        if (smsData.getDateSent() != 0L) {
            sb.append("Sent date ").append(dateFormatter.format(smsData.getDateSent())).append(TXT_SEPARATOR_LINE);
        }
        sb.append(smsData.getMessage()).append(TXT_SEPARATOR_LINE);
        return sb.toString();
    }

    public static String SMSDataToCSV(SMSData smsData) throws Exception {
        return smsData.getNumber() +
                CSV_SEPARATOR_FIELD +
                smsData.getDateSent() +
                CSV_SEPARATOR_FIELD +
                smsData.getDateReceived() +
                CSV_SEPARATOR_FIELD +
                smsData.getMessageType() +
                CSV_SEPARATOR_FIELD +
                smsData.getMessage();
    }

    public static JSONObject SMSDataToJSON(SMSData smsData) throws Exception {
        JSONObject smsObj = new JSONObject();
        smsObj.put("number", smsData.getNumber());
        smsObj.put("dateSent", smsData.getDateSent());
        smsObj.put("dateReceived", smsData.getDateReceived());
        smsObj.put("messageType", smsData.getMessageType());
        smsObj.put("message", smsData.getMessage());
        return smsObj;
    }

    public static String SMSDataToHTML(SMSData smsData) throws Exception {
        boolean isMessageTypeSent = smsData.isMessageTypeSent();
        String typeMsgClass = isMessageTypeSent ? "sendMsg" : "otherMsg";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class='").append(typeMsgClass).append("'>");
        sb.append("<b>").append(smsData.getNumber()).append("</b>");
        if (smsData.getDateReceived() != 0L) {
            sb.append(" - <span class='dateReceived'>").append(dateFormatter.format(smsData.getDateReceived())).append("</span>");
        }
        if (smsData.getDateSent() != 0L) {
            sb.append(" - <span class='dateSent'>").append(dateFormatter.format(smsData.getDateSent())).append("</span>");
        }
        sb.append("<br>");
        sb.append(smsData.getMessage()).append("<br>");
        sb.append("</div>");
        return sb.toString();
    }

    public static String SMSDataListToCSV(List<SMSData> smsList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("NUMBER");
        sb.append(CSV_SEPARATOR_FIELD);
        sb.append("DateSent");
        sb.append(CSV_SEPARATOR_FIELD);
        sb.append("DateReceived");
        sb.append(CSV_SEPARATOR_FIELD);
        sb.append("MessageType");
        sb.append(CSV_SEPARATOR_FIELD);
        sb.append("Message");
        sb.append(CSV_SEPARATOR_LINE);
        for (SMSData sms : smsList) {
            sb.append(SMSDataToCSV(sms));
            sb.append(CSV_SEPARATOR_LINE);
        }
        return sb.toString();
    }

    public static String SMSDataListToJson(List<SMSData> smsList) throws Exception {
        JSONArray jsonData = new JSONArray();
        for (SMSData sms : smsList) {
            jsonData.put(SMSDataToJSON(sms));
        }
        return jsonData.toString();
    }

    public static String SMSDataListToHTML(List<SMSData> smsList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<html> <meta charset=\"UTF-8\"> <head><style type=\"text/CSS\">" +
                "body {font-family:sans-serif;} " +
                ".dateSent {color:#B4BBC4;} " +
                ".dateReceived {color:#9AB2D3;} " +
                ".sendMsg {color:#8AB5F2;} " +
                ".otherMsg {color:#4052B5;}" +
                "</style></head><body>");
        for (SMSData sms : smsList) {
            sb.append(SMSDataToHTML(sms));
            sb.append("<br>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    public static String SMSDataListToText(List<SMSData> smsList) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (SMSData sms : smsList) {
            sb.append(SMSDataToText(sms));
            sb.append(TXT_SEPARATOR_LINE);
        }
        return sb.toString();
    }
}
