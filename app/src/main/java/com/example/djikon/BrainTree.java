package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.ClientToken;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nimbusds.jose.Header;

import static com.example.djikon.PreferenceData.getUserToken;
import static java.lang.System.err;

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
        startActivityForResult(dropInRequest.getIntent(this), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Toast.makeText(this, "Payment Done", Toast.LENGTH_SHORT).show();
                // use the result to update your UI and send the payment method nonce to your server
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

}