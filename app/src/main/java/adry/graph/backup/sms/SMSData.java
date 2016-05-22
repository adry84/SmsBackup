package adry.graph.backup.sms;

import android.os.Build;
import android.provider.Telephony;

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

    public int getMessageType() {
        return mMessageType;
    }

    public void setMessageType(int messageType) {
        mMessageType = messageType;
    }
}
