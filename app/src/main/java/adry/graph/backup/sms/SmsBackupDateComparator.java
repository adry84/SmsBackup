package adry.graph.backup.sms;

/**
 * Created by Audrey on 21/05/2016.
 * SmsData Comparator by date asc
 */
public class SmsBackupDateComparator implements java.util.Comparator<SMSData> {
    @Override
    public int compare(SMSData lhs, SMSData rhs) {
        return (int) (lhs.getDateReceived() - rhs.getDateReceived());

    }
}
