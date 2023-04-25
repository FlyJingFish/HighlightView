package com.flyjingfish.highlightview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyjingfish.highlightviewlib.HighlightImageView;
import com.flyjingfish.highlightviewlib.HighlightLinearLayout;
import com.flyjingfish.highlightviewlib.HighlightTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayout llRoot = findViewById(R.id.ll_root);
        HighlightImageView highlightImageView = findViewById(R.id.highlightImageView);
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
        highlightImageView.setOnClickListener(new View.OnClickListener() {
            boolean isRemove;
            @Override
            public void onClick(View v) {
//                if (highlightImageView.getHighlightAnimHolder().isPaused()){
//                    highlightImageView.getHighlightAnimHolder().resumeHighlightEffect();
//                }else {
//                    highlightImageView.getHighlightAnimHolder().pauseHighlightEffect();
//                }
                if (isRemove){
                    llRoot.addView(highlightLinearLayout);
                }else {
                    llRoot.removeView(highlightLinearLayout);
                }
                isRemove = !isRemove;
            }
        });
        highlightTextView.getHighlightAnimHolder().addLifecycleObserver(this);
        highlightLinearLayout.getHighlightAnimHolder().addLifecycleObserver(this);
//        highlightTextView.getHighlightAnimHolder().setHighlightColor(Color.BLUE);
//        highlightTextView.getHighlightAnimHolder().setHighlightWidth(90);
//        highlightTextView.getHighlightAnimHolder().setDuration(2000);
//        highlightTextView.getHighlightAnimHolder().setHighlightRotateDegrees(60);
//        highlightTextView.getHighlightAnimHolder().setRepeatMode(HighlightAnimHolder.REVERSE);
//        highlightTextView.getHighlightAnimHolder().setRepeatCount(4);
//        highlightTextView.getHighlightAnimHolder().setStartDirection(HighlightAnimHolder.FROM_RIGHT);
//        highlightTextView.getHighlightAnimHolder().startHighlightEffect();

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new ItemAnimAdapter());
    }

    private static class ItemAnimAdapter extends RecyclerView.Adapter<ItemAnimAdapter.ItemAnimHolder> {

        @NonNull
        @Override
        public ItemAnimHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemAnimHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anim,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemAnimHolder holder, int position) {
            HighlightLinearLayout highlightLinearLayout = holder.itemView.findViewById(R.id.highlightLinearLayout);
            highlightLinearLayout.getHighlightAnimHolder().addLifecycleObserver((LifecycleOwner) holder.itemView.getContext());
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        private static class ItemAnimHolder extends RecyclerView.ViewHolder {

            public ItemAnimHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }
}