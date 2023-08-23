package com.ma.timelineview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

        Button modeButton = findViewById(R.id.modeButton);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLineView.setTimeLineMode(timeLineView.getTimeLineMode() == 1 ? 2 : 1);
            }
        });

        timeLineView.setSectionClickListener(new TimeLineView.onSectionClickListener() {
            @Override
            public void onClick(boolean isClickOnSection, int sectionIndex) {
                if (isClickOnSection) {
                    Log.e(TAG, "onClick: click on section - " + sectionIndex);
                    Log.e(TAG, "onClick: click on section - " + timeLineView.getTimeLineData().getData().get(sectionIndex - 1) + " to " + timeLineView.getTimeLineData().getData().get(sectionIndex));
                } else {
                    if (timeLineView.getTimeLineMode() != 2) {
                        Log.e(TAG, "onClick: not in mode 2");
                    } else {
                        Log.e(TAG, "onClick: missing click !");
                    }
                }
            }
        });
    }
}