package com.flyjingfish.highlightview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.flyjingfish.highlightviewlib.HighlightLinearLayout;
import com.flyjingfish.highlightviewlib.HighlightTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HighlightTextView highlightTextView = findViewById(R.id.highlightTextView);
        HighlightLinearLayout highlightLinearLayout = findViewById(R.id.highlightLinearLayout);
        highlightTextView.setOnClickListener(v -> highlightTextView.getHighlightAnimHolder().stopHighlightEffect());

        highlightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highlightLinearLayout.getHighlightAnimHolder().isPaused()){
                    highlightLinearLayout.getHighlightAnimHolder().resumeHighlightEffect();
                }else {
                    highlightLinearLayout.getHighlightAnimHolder().pauseHighlightEffect();
                }
            }
        });

//        highlightTextView.getHighlightAnimHolder().setHighlightColor(Color.BLUE);
//        highlightTextView.getHighlightAnimHolder().setHighlightWidth(90);
//        highlightTextView.getHighlightAnimHolder().setDuration(2000);
//        highlightTextView.getHighlightAnimHolder().setHighlightRotateDegrees(60);
//        highlightTextView.getHighlightAnimHolder().setRepeatMode(HighlightAnimHolder.REVERSE);
//        highlightTextView.getHighlightAnimHolder().setRepeatCount(4);
//        highlightTextView.getHighlightAnimHolder().setStartDirection(HighlightAnimHolder.FROM_RIGHT);
//        highlightTextView.getHighlightAnimHolder().startHighlightEffect();
    }
}