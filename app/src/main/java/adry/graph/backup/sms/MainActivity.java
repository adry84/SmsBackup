package adry.graph.backup.sms;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Button mSaveButton;
    private ProgressBar mLoaderPb;
    private Spinner mExportFormatSpinner;
    private SmsListAdapter mSmsAdapter;
    private List<SMSData> mSmsList;
    private Thread mWriteSmsFileThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoaderPb = (ProgressBar) findViewById(R.id.activity_main_loader_pb);
        mLoaderPb.setVisibility(View.GONE);

        mExportFormatSpinner = (Spinner) findViewById(R.id.activity_main_export_chooser_s);
        FormatArrayAdapter dataAdapter = new FormatArrayAdapter(this);
        mExportFormatSpinner.setAdapter(dataAdapter);

        mSaveButton = (Button) findViewById(R.id.activity_main_save_b);
        assert mSaveButton != null;
        mSaveButton.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_main_list_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mSmsAdapter = new SmsListAdapter();
        recyclerView.setAdapter(mSmsAdapter);

        getSms();

        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getSms();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Cannot read sms without permissions", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void getSms() {
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mSmsList = SmsBackupTask.getSmsList(getApplicationContext());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateSmsListUI();
                        }
                    });
                }
            }).run();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void updateSmsListUI() {
        mSmsAdapter.setSmsList(mSmsList);
        mSmsAdapter.notifyDataSetChanged();
        mSaveButton.setVisibility(mSmsList != null && mSmsList.size() > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onClick(View v) {
        if (v == mSaveButton) {
            saveSmsList();
        }
    }

    private void saveSmsList() {
        if (mSmsList == null || mSmsList.size() == 0) {
            return;
        }
        mLoaderPb.setVisibility(View.VISIBLE);
        mWriteSmsFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String smsListToString;
                    ExportFormat typeConversion = (ExportFormat) mExportFormatSpinner.getSelectedItem();
                    switch (typeConversion) {
                        case CONVERSION_TYPE_CSV:
                            smsListToString = SMSDataUtils.SMSDataListToCSV(mSmsList);
                            break;
                        case CONVERSION_TYPE_HTML:
                            smsListToString = SMSDataUtils.SMSDataListToHTML(mSmsList);
                            break;
                        case CONVERSION_TYPE_JSON:
                            smsListToString = SMSDataUtils.SMSDataListToJson(mSmsList);
                            break;
                        default:
                            smsListToString = SMSDataUtils.SMSDataListToText(mSmsList);
                    }
                    final File file = SmsBackupProvider.saveSmsBackupFile(MainActivity.this, smsListToString, typeConversion.getExtension());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onDataReadyToSave(file);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "run: error transform to json", e);
                    e.printStackTrace();
                }
            }
        });
        mWriteSmsFileThread.run();
    }

    boolean isWriteSmsFileThreadAlive() {
        return mWriteSmsFileThread != null && mWriteSmsFileThread.isAlive();
    }

    private void onDataReadyToSave(File file) {
        mLoaderPb.setVisibility(View.GONE);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "adry.graph.backup.sms", file);
        i.setDataAndType(contentUri, "text/plain");
        startActivity(i);
    }
}
