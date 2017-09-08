package com.example.a52426.astart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.a52426.astart.view.BlockViewGroup;


public class MainActivity extends AppCompatActivity {

    private Button btStart;
    private BlockViewGroup bvgContent;
    private Button btClear;
    private Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bvgContent = (BlockViewGroup) findViewById(R.id.bvg_content);
        btStart = (Button) findViewById(R.id.bt_start);
        btClear = (Button) findViewById(R.id.bt_clear);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bvgContent.start();
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bvgContent.clear();
            }
        });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
