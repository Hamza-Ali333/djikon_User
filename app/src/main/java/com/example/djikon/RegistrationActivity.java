package com.example.djikon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edt_Name, edt_LastName, edt_Email, edt_Password, edt_C_Password, edt_Refral_Code;
    private Button btn_SignUp;

    private  RegisterModel registerModel;
    JSONApiHolder jsonApiHolder;
    String BASEURL_DATA="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        createRefrences();

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                /*if(checkInfo()) {
                    sendDataToServer(setDataIntoModel());
                }*/

                sendDataToServer();
            }//if
        });

    }



    private RegisterModel setDataIntoModel(){
        Log.i("TAG", "set: Runing");
        registerModel = new RegisterModel(edt_Name.getText().toString(),
                edt_LastName.getText().toString(),
                edt_Email.getText().toString(),
                edt_Password.getText().toString(),
                edt_C_Password.getText().toString(),
                edt_Refral_Code.getText().toString(),
                1);
        return  registerModel;
    }

    private boolean checkInfo () {
        Log.i("TAG", "checkInfo: Runing");
        boolean result = true;
        if (edt_Name.getText().toString().isEmpty()) {
            edt_Name.setError("Please Enter Your First Name");
        } else if (edt_LastName.getText().toString().isEmpty()) {
            edt_LastName.setError("Please Enter Your First Name");
        } else if (edt_Email.getText().toString().isEmpty()) {
            edt_Email.setError("Please Enter Your First Name");
        }else if (edt_Password.getText().toString().isEmpty()) {
            edt_Password.setError("Please Enter Your First Name");
        }else if (edt_C_Password.getText().toString().isEmpty()) {
            edt_C_Password.setError("Please Enter Your First Name");
        }else if (edt_Refral_Code.getText().toString().isEmpty()) {
            edt_Refral_Code.setError("Please Enter Your First Name");
        }

        return result;
    }



    private void sendDataToServer() {
        Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl(BASEURL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

//        RegisterModel model1 = new RegisterModel("Hamza","Ali",
//                "Hamzaregardless333@gmail.com",
//                "12345678",
//                "12345678",
//                "sdfsd",1);


        Call<RegisterModel> call = feedJsonApi.registerUser("Hamza","Ali Sawal",
                "malikafzal420420@gmail.com",
                "12345678",
                "12345678",
                "46579899867",
                2);

        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (!response.isSuccessful()) {
                   // textViewResult.setText("Code: " + response.code());
                   // Log.i("TAG", "onFailed: "+response.code());
                    Log.i("TAG", "onRes: "+response.body().toString()+"  Code:  "+ response.code());
                    return;
                }
                else {
                   // Log.i("TAG", "onSuccess: "+response.code());
                    Log.i("TAG", "onRes: "+response.body().toString() +"  Code:  "+ response.code());
                }

            }

            @Override
            public void onFailure(Call<RegisterModel>call, Throwable t) {
               // textViewResult.setText(t.getMessage());
            }
        });

//        Call <RegisterModel> call = feedJsonApi.registerUser(model);
//
//        call.enqueue(new Callback<RegisterModel>() {
//            @Override
//            public void onResponse(Call<RegisterModel>call, Response <RegisterModel> response) {
//                if (!response.isSuccessful()) {
//                    Log.i("TAG", "onResp: ");
//                    return;
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call <RegisterModel> call, Throwable t) {
//
//            }
//        });
    }


            private void createRefrences(){
        edt_Name = findViewById(R.id.edt_first_name);
        edt_LastName = findViewById(R.id.edt_last_name);
        edt_Email = findViewById(R.id.edt_email);
        edt_Password = findViewById(R.id.edt_password);
        edt_C_Password = findViewById(R.id.edt_c_password);
        edt_Refral_Code  = findViewById(R.id.edt_refrel_code);
        btn_SignUp = findViewById(R.id.btn_sign_up);

    }

}
