package com.example.djikon.BrainTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.R;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class BrainTree extends AppCompatActivity {

    private TextInputLayout mTextInputLayout;
    private Button pay;
    String clientToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tree);

        pay = findViewById(R.id.pay);
        mTextInputLayout = findViewById(R.id.edt_amount);

        AsyncHttpClient client = new AsyncHttpClient();
        PreferenceData preferenceData = new PreferenceData();

       clientToken = preferenceData.getUserToken(this);

//        client.get("https://your-server/client_token", new TextHttpResponseHandler() {
//            private String clientToken;
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String clientToken) {
//                this.clientToken = clientToken;
//            }
//        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBraintreeSubmit(view);
            }
        });

    }

    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
        startActivityForResult(dropInRequest.getIntent(this), 777);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Toast.makeText(this, "Payment Done", Toast.LENGTH_SHORT).show();
                // use the result to update your UI and send the payment method nonce to your server
                postNonceToServer(String.valueOf(result));

            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
                Toast.makeText(this, "Payment Cancle", Toast.LENGTH_SHORT).show();
            } else {
                // handle errors here, an exception may be available in
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }else {
            Toast.makeText(this, "Result is not correct", Toast.LENGTH_SHORT).show();
        }
    }

    void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        client.post("http://your-server/checkout", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(BrainTree.this, "Done From" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(BrainTree.this, "Cancel From", Toast.LENGTH_SHORT).show();
                    }
                    //Your implementation here
                }
        );
    }


    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "5xrr5fzzm2j9wpt5",
            "pbh8534wdgvfwvx2",
            "90beaf12a6ed65f8708e44154203611e"
    );




}