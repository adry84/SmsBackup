package adry.graph.backup.sms;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Audrey on 14/05/2016.
 * ContentProvider For the smsbackup file
 */
public class SmsBackupProvider extends ContentProvider{


    private static final String TAG = "SmsBackupProvider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {

        try {
            File privateFile = getSmsBackupFile(getContext());
            return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static File saveSmsBackupFile(Context context, String data) throws Exception{
        OutputStream outputStream = null;
        try {
            File outFile = getSmsBackupFile(context);

            outputStream = new FileOutputStream(outFile.getAbsolutePath());

            outputStream.write(data.getBytes());

            return outFile;

        } catch (Exception e) {
            Log.e(TAG, "saveSmsBackupFile: ", e);
            throw e;
        } finally {
            if (outputStream != null)  {
                outputStream.close();
            }
        }
    }

    public static File getSmsBackupFile(Context context) throws Exception {
        File filePath =  new File(context.getFilesDir(), "backups");
        if (!filePath.exists()) {
            boolean hasMakeDir = filePath.mkdir();
            if (!hasMakeDir) {
                throw new Exception("Cannot create sms backup directory");
            }
        }
        String fileName = "SMSBackup.txt";
        File newFile = new File(filePath, fileName);
        if (!newFile.exists()) {
            boolean hasMakeDir = newFile.createNewFile();
            if (!hasMakeDir) {
                throw new Exception("Cannot create sms backup file");
            }
        }
        return newFile;
    }
}
