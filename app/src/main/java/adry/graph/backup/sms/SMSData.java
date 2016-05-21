package adry.graph.backup.sms;

import android.os.Build;
import android.provider.Telephony;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Audrey on 14/05/2016.
 * SmsData Model
 */
public class SMSData {

    //the sender phone number
    private String mNumber;
    //the message received
    private String mMessage;
    //the emission date
    private long mDateReceived = 0L;
    private long mDateSent = 0L;
    private int mMessageType;

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }


    public long getDateReceived() {
        return mDateReceived;
    }

    public void setDateReceived(long dateReceived) {
        mDateReceived = dateReceived;
    }

    public long getDateSent() {
        return mDateSent;
    }

    public void setDateSent(long datSent) {
        mDateSent = datSent;
    }

    public boolean isMessageTypeSent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return mMessageType == Telephony.Sms.MESSAGE_TYPE_SENT;
        }
        //TODO : how to know if the message is sent or received before kitkat
        return false;
    }

    //uncoment and improve if we want to export as json object
//    public JSONObject toJSON() throws Exception{
//        JSONObject smsObj = new JSONObject();
//        smsObj.put("number", mNumber);
//        if (mDateReceived != 0l) {
//            smsObj.put("dateSent", mDateSent);
//        }
//        if (mDateReceived != 0l) {
//            smsObj.put("dateReceived", mDateReceived);
//        }
//        smsObj.put("message", mMessage);
//        return smsObj;
//    }

    public String toHTML() throws Exception{
        boolean isMessageTypeSent = isMessageTypeSent();
        String typeMsgClass = isMessageTypeSent ? "sendMsg" : "otherMsg";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class='").append(typeMsgClass).append("'>");
        sb.append("<b>").append(mNumber).append("</b>");
        if (mDateReceived != 0L) {
            sb.append(" - <span class='dateReceived'>").append(dateFormatter.format(mDateReceived)).append("</span>");
        }
        if (mDateSent != 0L) {
            sb.append(" - <span class='dateSent'>").append(dateFormatter.format(mDateSent)).append("</span>");
        }
        sb.append("<br>");
        sb.append(mMessage).append("<br>");
        sb.append("</div>");
        return sb.toString();
    }

    public void setMessageType(int messageType) {
        mMessageType = messageType;
    }
}
