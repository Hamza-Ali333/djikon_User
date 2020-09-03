package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ikonholdings.ikoniconnects.R;

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

    public void btnOnClick(String text) {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText(text);
    }

    public void btnOnCompelet(String txt) {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText(txt);
    }
}
