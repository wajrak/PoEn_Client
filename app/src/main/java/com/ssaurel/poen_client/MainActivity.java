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

        static String strHeader = "Header";
        static String strAction = "Action";
        static String strItem = "Item";
        static String strPrice = "Price";
        public String strToken = null;
        private static final String TAG = "MyFirebaseIIDService";
        //TextView mTextView = (TextView) findViewById(R.id.txtDesc);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            onNewIntent(getIntent());
            FirebaseMessaging.getInstance().subscribeToTopic("PoE");
            setContentView(R.layout.activity_main);
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

        @Override
        public void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            Bundle extras = intent.getExtras();
            if (extras != null) {
                setContentView(R.layout.activity_main);
                final TextView Action =
                        (TextView) findViewById(R.id.txtAction);
                final TextView Header =
                        (TextView) findViewById(R.id.txtHeader);
                final TextView Item =
                        (TextView) findViewById(R.id.txtItem);
                final TextView Price =
                        (TextView) findViewById(R.id.txtPrice);

                strHeader = extras.getString("Header",
                        "Header");
                strAction = extras.getString("Action",
                        "Action");
                strItem = extras.getString("Item",
                        "Item");
                strPrice = extras.getString("Price",
                        "Price");

                for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.d("data ", "Key: " + key + " Value: " + value);
                }

                Action.setText(strAction);
                Header.setText(strHeader);
                Item.setText(strItem);
                Price.setText(strPrice);
            }
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