package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity  extends AppCompatActivity {

    TextView txt_Create_new_account,txt_Forgot_Password , txt_signwith_face, txt_signwith_Finger,txt_signwith_PIN;



    Button btn_SignIn;

    ImageView img_face_Id, img_Finger_Print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);
        getSupportActionBar().hide();

        createReferencer();

        txt_Create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),RegistrationActivity.class));
                finish();
            }
        });

        txt_Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           openForgotDialogue();
            }
        });

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);



                Call<LoginModel> call = feedJsonApi.Login("hamzaregardless333@gmail.com","12345678");
                call.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if(!response.isSuccessful()){
                            Log.i("TAG", "fail: "+response.code());
                        }else {
                            Log.i("TAG", "succ: "+response.code());
                            LoginModel loginModel = response.body();
                            Toast.makeText(SignInActivity.this, loginModel.getEmail(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Log.i("TAG", "onFailure: "+t.getMessage());
                    }
                });



//                Intent i = new Intent(SignInActivity.this,
//                        MainActivity.class);
//                startActivity(i);
//                finish();

            }
        });

        txt_signwith_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openLoginWithFaceId();
            }
        });

        img_face_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWithFaceId();
            }
        });

        txt_signwith_Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWithFingerPrint();
            }
        });

        img_Finger_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWithFingerPrint();
            }
        });

        txt_signwith_PIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInWithPIN();
            }
        });

    }

    private void openForgotDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_password_forgot, null);


        EditText edtEmail = view.findViewById(R.id.edt_Email);
        Button btnResetPassword = view.findViewById(R.id.btn_Reset_Password);

        builder.setView(view);
        builder.setCancelable(true);


        final AlertDialog alertDialog =  builder.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                openVerfiyOTPDialogue();
            }
        });

        }



    private void openVerfiyOTPDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailogue_varification_code, null);

       EditText edt_pin1, edt_pin2, edt_pin3,edt_pin4;
        edt_pin1 = view.findViewById(R.id.pin1);
        edt_pin2 = view.findViewById(R.id.pin2);
        edt_pin3 = view.findViewById(R.id.pin3);
        edt_pin4 = view.findViewById(R.id.pin4);


        ImageView img_close = view.findViewById(R.id.close);

        Button btn_Verify_OTP = view.findViewById(R.id.btn_Verify_OTP);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog =  builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_Verify_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent( view.getContext(), ResetPasswordActivity.class);
                startActivity(i);
            }
        });

    }



    private void openLoginWithFaceId() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_faceid, null);



         Button btn_cancle_Face_Id = view.findViewById(R.id.cancel_faceid);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        btn_cancle_Face_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void openLoginWithFingerPrint() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_finger_print, null);



        Button btn_cancle_Face_Id = view.findViewById(R.id.cancel_fingerprint);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        btn_cancle_Face_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void openSignInWithPIN() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailogue_pin_code, null);


        EditText edt_Pin_Code = view.findViewById(R.id.pincode);
        Button btn_Login_Pin = view.findViewById(R.id.btn_login_pin);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog =  builder.show();

        btn_Login_Pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent( view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }


        private void createReferencer() {
        txt_Create_new_account = findViewById(R.id.txt_Create_new_account);
        txt_Forgot_Password= findViewById(R.id.txt_Forgot_Password);
        txt_signwith_Finger = findViewById(R.id.txt_finger_print);
        txt_signwith_face = findViewById(R.id.txt_face_id);
        txt_signwith_PIN = findViewById(R.id.txt_signInWithPIN);


        btn_SignIn = findViewById(R.id.btn_SignIn);
        img_face_Id = findViewById(R.id.img_face_id);
        img_Finger_Print = findViewById(R.id.img_finger_print);
        }

    }



