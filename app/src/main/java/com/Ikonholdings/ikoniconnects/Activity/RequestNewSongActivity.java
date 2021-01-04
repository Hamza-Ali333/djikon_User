package com.Ikonholdings.ikoniconnects.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.GlobelClasses.KeyBoard;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerSelectArtist;
import com.Ikonholdings.ikoniconnects.ResponseModels.SubscribeArtistModel;
import com.Ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestNewSongActivity extends AppCompatActivity implements RecyclerSelectArtist.onItemClickListner {

    private RecyclerView mRecyclerView;
    private RecyclerSelectArtist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText edt_Song_Name, edt_Requester_Name;
    private Button btn_Submit;

    private AlertDialog loadingDialog;

    private Integer artistId = null;

    private ProgressDialog progressDialog;
    private  Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    private ProgressBar loadingSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_song);
        getSupportActionBar().setTitle("Request A Song");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_artist);
        loadingSubscriber = findViewById(R.id.subscriberProgressbar);

        retrofit= ApiClient.retrofit(this);
        new GetSubscriberArtist().execute();

         edt_Song_Name = findViewById(R.id.edt_Song_Name);
         edt_Requester_Name = findViewById(R.id.reivew);
         btn_Submit = findViewById(R.id.btn_submit);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loadingSubscriber.getVisibility() == View.GONE){

                    if (!edt_Requester_Name.getText().toString().isEmpty() &&
                            !edt_Song_Name.getText().toString().isEmpty() && artistId != null) {
                        KeyBoard.hideKeyboard(RequestNewSongActivity.this);
                        progressDialog = DialogsUtils.showProgressDialog(RequestNewSongActivity.this,"Posting Request","Please Wait...");
                        postRequestSong(edt_Requester_Name.getText().toString(), edt_Song_Name.getText().toString());

                    }else {
                        if(artistId == null){
                            Toast.makeText(RequestNewSongActivity.this, "Please Select A Artist.", Toast.LENGTH_SHORT).show();
                        } else if(edt_Requester_Name.getText().toString().isEmpty()){
                            edt_Requester_Name.setError("Please enter your name");
                        }else {
                            edt_Song_Name.setError("Please enter song name");
                        }
                    }
                }else {
                    Toast.makeText(RequestNewSongActivity.this, "Please Wait while loading Artist List", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void postRequestSong (String UserName,String SongName) {
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String relativeUrl = "request_song/"+artistId;
        Call<SuccessErrorModel> call = jsonApiHolder.postSongRequest(relativeUrl,
                UserName,
                SongName
        );

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    edt_Requester_Name.getText().clear();
                    edt_Song_Name.getText().clear();
                    DialogsUtils.showSuccessDialog(RequestNewSongActivity.this,"Done","Your request is submitted successfully.",false);
                }else {
                    progressDialog.dismiss();
                    DialogsUtils.showResponseMsg(RequestNewSongActivity.this,false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                DialogsUtils.showResponseMsg(RequestNewSongActivity.this,true);
            }
        });
    }

    private void initializeRecycler (List<SubscribeArtistModel> ArtistList) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerSelectArtist(ArtistList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void add(Integer id) {
        artistId = id;
        KeyBoard.hideKeyboard(RequestNewSongActivity.this);
    }

    @Override
    public void remove(Integer id) {
        artistId = null;
    }

    private class GetSubscriberArtist extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = DialogsUtils.showLoadingDialogue(RequestNewSongActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<List<SubscribeArtistModel>> call = jsonApiHolder.getSubscribeArtist();

            call.enqueue(new Callback<List<SubscribeArtistModel>>() {
                @Override
                public void onResponse(Call<List<SubscribeArtistModel>> call, Response<List<SubscribeArtistModel>> response) {
                    loadingSubscriber.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        mRecyclerView.setVisibility(View.VISIBLE);

                        List<SubscribeArtistModel> artistModels = response.body();
                        if(artistModels.isEmpty()){

                            DialogsUtils.showAlertDialog(RequestNewSongActivity.this,false,
                                    "No Subscribed Artist Found",
                                    "it's seems like you didn't follow any artist!" +
                                            "\n You can't make request until You follow at least one artist.");
                        }
                        else{

                            initializeRecycler(artistModels);
                        }
                    }else {
                        DialogsUtils.showResponseMsg(RequestNewSongActivity.this,false);
                    }

                }

                @Override
                public void onFailure(Call<List<SubscribeArtistModel>> call, Throwable t) {
                    loadingSubscriber.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(RequestNewSongActivity.this,true);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingDialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}