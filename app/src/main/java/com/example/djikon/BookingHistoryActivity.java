package com.example.djikon;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.ResponseModels.BookingHistory;
import com.example.djikon.RecyclerView.RecyclerBookingHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private  AlertDialog alertDialog;
    private RelativeLayout rlt_progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);

        mRecyclerView = findViewById(R.id.recyclerViewBookingHistory);
        rlt_progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        getSupportActionBar().setTitle("Booking History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);


        Retrofit retrofit= ApiClient.retrofit(this);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<BookingHistory>> call = jsonApiHolder.getBookingHistory();

        call.enqueue(new Callback<List<BookingHistory>>() {
            @Override
            public void onResponse(Call<List<BookingHistory>> call, Response<List<BookingHistory>> response) {
                if(response.isSuccessful()){
                    List<BookingHistory> bookingHistoryList = response.body();
                    rlt_progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    if(bookingHistoryList.isEmpty()){
                        //if no data then show dialoge to user
                       alertDialog = DialogsUtils.showAlertDialog(BookingHistoryActivity.this,false,
                                "No Booking Found","it's seems like you din't done any booking yet");
                    }
                    else
                        initializeRecycler(bookingHistoryList);

                }else {

                    rlt_progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    Toast.makeText(BookingHistoryActivity.this, "Some thing happened wrong try Again", Toast.LENGTH_SHORT).show();

                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BookingHistory>> call, Throwable t) {

            }
        });

    }

    private void initializeRecycler (List<BookingHistory> bookingHistory) {

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerBookingHistory(bookingHistory);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}