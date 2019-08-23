package com.tao.serialliba;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.tao.serial.ASyncSerial;
import com.tao.serial.SerialData;
import com.tao.serialliba.tobaco.Iui;
import com.tao.serialliba.tobaco.SerialRsult;
import com.tao.serialliba.tobaco.TobaccoCmdHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private EditText etText;
    private ScrollView svText;

    StringBuilder sb = new StringBuilder();
    private TobaccoCmdHelper tobaccoCmdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.et_text);
        svText = findViewById(R.id.sv_text);
        tobaccoCmdHelper = new TobaccoCmdHelper("/dev/ttyS3", 9600);
        tobaccoCmdHelper.setIui(new Iui() {
            @Override
            public void showText(String str) {
//                MainActivity.this.showText(str);
            }

            @Override
            public void onResult(SerialRsult serialRsult) throws Exception {

            }

            @Override
            public void onCmdSendFaile(SerialRsult serialRsult) throws Exception {

            }

            @Override
            public void onCmdTimeOut(SerialRsult serialRsult) {

            }
        });
    }
 

    public void close(View view) {
         
    }

    public void send(View view) {
        tobaccoCmdHelper.activeReplenishmentMoudle();
    }

    public void bh(View view) {
        tobaccoCmdHelper.closeLight();
    }

    public void ch(View view) {
        tobaccoCmdHelper.resetSend();

    }

    public void clear(View view) {
        sb = new StringBuilder();
        showText("");
    }

    public void reset(View view) {
  


    }
 
    private void repeatData(SerialData data) {
     
    }

    public void showText(String str) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(System.currentTimeMillis()));
        sb.insert(0, "==\n" + format + "\n" + str + "\n");
        etText.post(new Runnable() {
            @Override
            public void run() {
                etText.setText(sb.toString());
                svText.post(new Runnable() {
                    @Override
                    public void run() {
                        svText.fullScroll(ScrollView.SCROLL_INDICATOR_TOP);
                    }
                });

            }
        });
    }
}
