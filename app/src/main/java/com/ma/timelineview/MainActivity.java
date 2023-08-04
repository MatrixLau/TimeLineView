package com.ma.timelineview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeLineView timeLineView = findViewById(R.id.timeLine);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == timeLineView.getTimeLineData().getData().size() - 1) count = 1;
                else count++;
                timeLineView.setCurrentTimeSection(count);
                Log.i(TAG, "onClick: setCurrentTimeSection=" + count);
            }
        });
    }
}