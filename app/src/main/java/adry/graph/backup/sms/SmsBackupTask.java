package adry.graph.backup.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Audrey on 21/05/2016.
 * task to get sms on the phone
 */
public class SmsBackupTask {
    public static List<SMSData> getSmsList(Context ctx) {
        ArrayList<SMSData> smsList = new ArrayList<>();
        addSmsInListWithUri(ctx, smsList, "content://sms/");
        Collections.sort(smsList, new SmsBackupDateComparator());
        return smsList;
    }

    private static void addSmsInListWithUri(Context ctx, ArrayList<SMSData> smsList, String uriString) {
        Uri uri =  Uri.parse(uriString);
        CursorLoader cursorLoader = new CursorLoader(ctx);
        cursorLoader.setUri(uri);
        Cursor c = cursorLoader.loadInBackground();

        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for( int i = 0; i < c.getColumnCount(); i++) {
                Log.d("SmsBackupTask", "Column: " + c.getColumnName(i) + "\n");
            }
            for(int i=0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();
                sms.setMessage(c.getString(c.getColumnIndexOrThrow("body")));
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")));
                int indexColumn = c.getColumnIndex("type");
                if (indexColumn != -1) {
                    sms.setMessageType(c.getInt(indexColumn));
                }
                indexColumn = c.getColumnIndex("date");
                if (indexColumn != -1) {
                    sms.setDateReceived(c.getLong(indexColumn));
                }
                indexColumn = c.getColumnIndex("date_sent");
                if (indexColumn != -1) {
                    sms.setDateSent(c.getLong(indexColumn));
                }
                smsList.add(sms);
                c.moveToNext();
            }
        }
        c.close();
    }
}
