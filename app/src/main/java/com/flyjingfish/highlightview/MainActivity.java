package com.flyjingfish.highlightview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.flyjingfish.highlightviewlib.HighlightTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HighlightTextView highlightTextView = findViewById(R.id.highlightTextView);
        highlightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTextView.setText("1111");
            }
        });
    }
}