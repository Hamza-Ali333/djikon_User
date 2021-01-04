package com.Ikonholdings.ikoniconnects.CustomDialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Ikonholdings.ikoniconnects.GlobelClasses.KeyBoard;
import com.Ikonholdings.ikoniconnects.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReviewBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mBottomSheetListener;
    private float ratting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dailoge_give_review, container, false);

        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
        EditText edt_Review = v.findViewById(R.id.reivew);
        Button btn_Submit = v.findViewById(R.id.btn_submit);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratting = v;
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratting == 0.0){
                    KeyBoard.hideKeyboard(getActivity());
                    Toast.makeText(getContext(), "Please give some ratting first!", Toast.LENGTH_SHORT).show();
                }
                else  if(edt_Review.getText().toString().isEmpty()){
                    edt_Review.setError("Required");
                    edt_Review.requestFocus();
                }
                else {
                    KeyBoard.hideKeyboard(getActivity());
                    mBottomSheetListener.onReviewSubmit(edt_Review.getText().toString(), ratting);
                    dismiss();
                }

            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onReviewSubmit(String Review, Float rating);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            +"must implement bottomsheetListener");
        }
    }
}
