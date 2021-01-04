package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerBookingHistory;
import com.Ikonholdings.ikoniconnects.ResponseModels.BookingHistory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BookingHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerBookingHistory mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txtMsg;

    private List<BookingHistory> bookingHistoryList;

    private Spinner mFilterSpinner;
    private Boolean activityAlreadyRuned = false;

    private  AlertDialog loadingDialog;
    private String[] statusArray = {"Filter History", "Completed", "Rejected", "Pending"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_booking_history,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewBookingHistory);
        mFilterSpinner = v.findViewById(R.id.spinner_status);
        txtMsg = v.findViewById(R.id.msg);
        mFilterSpinner.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_gender_spinner, R.id.genders, statusArray);

        mFilterSpinner.setAdapter(adapter);
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int status, long l) {

                //SelectedGender = genderArray[i];
                if(activityAlreadyRuned){
                    switch (status){
                        default:
                        case 0:
                            mAdapter.filterList(bookingHistoryList);
                            break;
                        case 1:
                            filter("1");//Completed bookings
                            break;
                        case 2:
                            filter("2");//ReJected bookings
                            break;
                        case 3:
                            filter("0");//Pendings
                            break;
                    }
                }else {
                    activityAlreadyRuned = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                     bookingHistoryList = response.body();

                    if(bookingHistoryList.isEmpty()){
                        //if no data then show dialoge to user
                        loadingDialog.dismiss();
                        mFilterSpinner.setVisibility(View.GONE);
                       txtMsg.setVisibility(View.VISIBLE);
                    }
                    else{
                        mFilterSpinner.setVisibility(View.VISIBLE);
                        initializeRecycler(bookingHistoryList);
                        loadingDialog.dismiss();
                        txtMsg.setVisibility(View.GONE);
                    }

                }else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(),false);

                }
            }

            @Override
            public void onFailure(Call<List<BookingHistory>> call, Throwable t) {
                loadingDialog.dismiss();
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

    private void filter(String searchStatus){
        List<BookingHistory> filteredList = new ArrayList<>();
        String Status;
        for(BookingHistory item: this.bookingHistoryList) {
            Status = item.getStatus();
            if(Status.equals(searchStatus)){
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }


}
