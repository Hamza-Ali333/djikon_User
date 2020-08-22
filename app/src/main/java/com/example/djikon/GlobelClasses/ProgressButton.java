package com.example.djikon.GlobelClasses;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.djikon.R;

public class ProgressButton {
    private CardView mCardView;
    private ConstraintLayout mConstraintLayout;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public ProgressButton(Context ct, View view) {
     mCardView = view.findViewById(R.id.cardview);
     mConstraintLayout = view.findViewById(R.id.constrain);
     mTextView = view.findViewById(R.id.btn_title);
     mProgressBar = view.findViewById(R.id.btn_progress);
    }

    public void buttonActivated() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText("Please Wait...");
    }
    public void buttonFinished() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText("Done");
        //lunsh the Activity
    }
}
