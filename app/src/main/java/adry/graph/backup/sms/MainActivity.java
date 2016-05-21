package adry.graph.backup.sms;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button mSaveButton;
    private SmsListAdapter mSmsAdapter;
    private List<SMSData> mSmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS")
                != PackageManager.PERMISSION_GRANTED) {
            finish();
        }


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

    private void getSms() {
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //in case we need json
//                    JSONArray jsonData = new JSONArray();
//                    for (SMSData sms : mSmsList) {
//                        jsonData.put(sms.toJSON());
//                    }
//                    String smsListToString =  jsonData.toString();

                    StringBuilder sb = new StringBuilder();
                    sb.append("<html> <meta charset=\"UTF-8\"> <head><style type=\"text/CSS\">" +
                            "body {font-family:sans-serif;} " +
                            ".dateSent {color:#B4BBC4;} " +
                            ".dateReceived {color:#9AB2D3;} " +
                            ".sendMsg {color:#8AB5F2;} " +
                            ".otherMsg {color:#4052B5;}" +
                            "</style></head><body>");
                    for (SMSData sms : mSmsList) {
                        sb.append(sms.toHTML());
                        sb.append("<br>");
                    }
                    sb.append("</body></html>");
                    String smsListToString =  sb.toString();

                    final File file = SmsBackupProvider.saveSmsBackupFile(MainActivity.this, smsListToString);

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
        }).run();
    }

    private void onDataReadyToSave(File file) {
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "adry.graph.backup.sms", file);
        i.setDataAndType(contentUri, "text/plain");
        startActivity(i);
    }
}
