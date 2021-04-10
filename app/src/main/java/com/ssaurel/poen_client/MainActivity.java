package com.ssaurel.poen_client;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    public String strStash = "Header";
    public String strAction = "Action";
    public String strItem = "Item";
    public String strPrice = "Price";
    public String strPosX = "PosX";
    public String strPosY = "PosY";
    public String strTime = "Time";
    public String strToken = null;
    private static final String TAG = "MyFirebaseIIDService";
    //TextView mTextView = (TextView) findViewById(R.id.txtDesc);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        FirebaseMessaging.getInstance().subscribeToTopic("PoE");
        setContentView(R.layout.activity_main);
        FirebaseMessagingService as = new FirebaseMessagingService();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void GetToken(View view){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        final TextView txtToken = (TextView) findViewById(R.id.txtToken);
        txtToken.setText(refreshedToken);
        Button bntShareToken = (Button) findViewById(R.id.btnShareToken);

        if (refreshedToken.length() > 20) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            //copy token to clipboard
            strToken = refreshedToken;
            CopyToClipboard(refreshedToken);
            ShowToastMsg("Copied to clipboard");
            bntShareToken.setEnabled(true);
        }
        else{
            txtToken.setText("Error, try again.");
        }
    }

    //when message shown from bar click
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            strAction = extras.getString("Action", "Action");
            strStash = extras.getString("Stash", "Stash");
            strItem = extras.getString("Item", "Item");
            strTime = extras.getString("Timestamp", "Timestamp");
            strPrice = extras.getString("Price", "Price");
            strPosX = extras.getString("PosX", "PosX");
            strPosY = extras.getString("PosY","PosY");

            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.d("data ", "Key: " + key + " Value: " + value);
            }
            UpdateLabelsThread(strItem, strTime, strPrice, strPosX, strPosY, strStash);
        }
    }

    private void UpdateLabelsThread(String strItem, String strTime, String strPrice, String strPosX, String strPosY, String strStash) {
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setContentView(R.layout.activity_main);
                            final TextView ItemName = (TextView)findViewById(R.id.txtItemName);
                            final TextView Time = (TextView) findViewById(R.id.txtTime);
                            final TextView Stash = (TextView) findViewById(R.id.txtStash);
                            final TextView Price = (TextView) findViewById(R.id.txtPrice);
                            final TextView PosX = (TextView) findViewById(R.id.txtPosX);
                            final TextView PosY = (TextView) findViewById(R.id.txtPosY);

                            ItemName.setText(strItem);
                            Time.setText(strTime);
                            Price.setText(strPrice);
                            PosX.setText(strPosX);
                            PosY.setText(strPosY);
                            Stash.setText(strStash);
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void CopyToClipboard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied to clipboard", text);
        clipboard.setPrimaryClip(clip);
    }

    public void ShowToastMsg(String text){
        Context context = getApplicationContext();
        //CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void SendShareSheet(String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void ShareToken(View view) {
        final TextView txtToken = (TextView) findViewById(R.id.txtToken);
        SendShareSheet(txtToken.getText().toString());
    }
}
