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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Audrey on 14/05/2016.
 * ContentProvider For the smsbackup file
 */
public class SmsBackupProvider extends ContentProvider{


    private static final String TAG = "SmsBackupProvider";
    private static final String FILE_NAME = "SmsBackup";

    public static File saveSmsBackupFile(Context context, String data, String extension) throws Exception {
        Writer out = null;
        try {
            File outFile = getSmsBackupFile(context, FILE_NAME + "." + extension);
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile.getAbsolutePath()), "UTF-8"));

            out.write(data);

            return outFile;

        } catch (Exception e) {
            Log.e(TAG, "saveSmsBackupFile: ", e);
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static File getSmsBackupFile(Context context, String fileName) throws Exception {
        File filePath = new File(context.getFilesDir(), "backups");
        if (!filePath.exists()) {
            boolean hasMakeDir = filePath.mkdir();
            if (!hasMakeDir) {
                throw new Exception("Cannot create sms backup directory");
            }
        }
        File newFile = new File(filePath, fileName);
        if (!newFile.exists()) {
            boolean hasMakeDir = newFile.createNewFile();
            if (!hasMakeDir) {
                throw new Exception("Cannot create sms backup file");
            }
        }
        return newFile;
    }

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
        if (uri.getPath() == null) {
            return null;
        }
        try {
            String fileName = uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1);
            if (!fileName.startsWith(FILE_NAME)) {
                return null;
            }
            File privateFile = getSmsBackupFile(getContext(), fileName);
            return ParcelFileDescriptor.open(privateFile, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
