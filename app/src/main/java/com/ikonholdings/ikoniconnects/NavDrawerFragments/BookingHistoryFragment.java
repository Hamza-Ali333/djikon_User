package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerBookingHistory;
import com.ikonholdings.ikoniconnects.ResponseModels.BookingHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BookingHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private  AlertDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_booking_history,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewBookingHistory);

        getBookingHistory();

        return v;
    }

    private void getBookingHistory() {
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<BookingHistory>> call = jsonApiHolder.getBookingHistory();

        call.enqueue(new Callback<List<BookingHistory>>() {
            @Override
            public void onResponse(Call<List<BookingHistory>> call, Response<List<BookingHistory>> response) {
                if(response.isSuccessful()){
                    List<BookingHistory> bookingHistoryList = response.body();

                    if(bookingHistoryList.isEmpty()){
                        //if no data then show dialoge to user
                        loadingDialog.dismiss();
                        mRecyclerView.setVisibility(View.GONE);
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "No Booking Found","it's seems like you din't done any booking yet");
                    }
                    else{
                        initializeRecycler(bookingHistoryList);
                        loadingDialog.dismiss();
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }

                }else {

                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(),false);

                }
            }

            @Override
            public void onFailure(Call<List<BookingHistory>> call, Throwable t) {
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });
    }

    private void initializeRecycler (List<BookingHistory> bookingHistory) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerBookingHistory(bookingHistory);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
