package com.example.djikon.BrainTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.Models.SuccessErrorModel;
import com.example.djikon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BrainTreeActivity extends AppCompatActivity {


    private EditText edt_amount;
    private Button pay;
    private String Token;
    private String amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tree);

//        new GetBrainTreeToken().execute();
//        pay = findViewById(R.id.pay);
//        edt_amount = findViewById(R.id.edt_amount);
//
//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(edt_amount != null){
//                    amount = edt_amount.getText().toString().trim();
//                    //onBrainTreeSubmit();
//                }
//            }
//        });
    }









}