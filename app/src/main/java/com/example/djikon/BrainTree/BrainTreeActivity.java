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

    private static  final  int DROP_IN_REQUEST_CODE = 777;
    private EditText edt_amount;
    private Button pay;
    private String Token;
    private String amount;
    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tree);
        retrofit= ApiClient.retrofit(this);
        new GetBrainTreeToken().execute();
        pay = findViewById(R.id.pay);
        edt_amount = findViewById(R.id.edt_amount);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_amount != null){
                    amount = edt_amount.getText().toString().trim();
                    onBrainTreeSubmit();
                }
            }
        });
    }

    public void onBrainTreeSubmit() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(Token);

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DROP_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                postNonceToServer(paymentMethodNonce);

            } else if (resultCode == RESULT_CANCELED) {
                //the user canceled
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
        params.put("amount", amount);
        client.post("http://ec2-52-91-44-156.compute-1.amazonaws.com/api/chekout", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(BrainTreeActivity.this, "Done From" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(BrainTreeActivity.this, "Cancel From", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void getBrainTreeTokenFromServer(){
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.getBrainTreeToken();

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    SuccessErrorModel  successErrorModel = response.body();
                    Token = successErrorModel.getSuccess();
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

            }
        });
    }


    private class GetBrainTreeToken extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        getBrainTreeTokenFromServer();
        return null;
    }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(BrainTreeActivity.this, "Token Get", Toast.LENGTH_SHORT).show();
        }
    }


}