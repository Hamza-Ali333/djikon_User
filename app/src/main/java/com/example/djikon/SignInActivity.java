package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity  extends AppCompatActivity {

   private TextView txt_Create_new_account,txt_Forgot_Password , txt_signwith_face, txt_signwith_Finger,txt_signwith_PIN,txt_Error;



   private Button btn_SignIn;

   private ImageView img_face_Id, img_Finger_Print;
   private EditText edt_Email,edt_password;
   private PreferenceData preferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);
        getSupportActionBar().hide();

        createReferencer();


        preferenceData = new PreferenceData();



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
                if(isInfoRight()) {
                    isUserExits();
                }



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
        final View view = inflater.inflate(R.layout.dailoge_loading, null);


        //EditText edtEmail = view.findViewById(R.id.edt_Email);
        //Button btnResetPassword = view.findViewById(R.id.btn_Reset_Password);

        builder.setView(view);
        builder.setCancelable(true);


        final AlertDialog alertDialog =  builder.show();

//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                openVerfiyOTPDialogue();
//            }
//        });

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

private boolean isInfoRight() {

        boolean result = true;
         if (edt_Email.getText().toString().trim().isEmpty()) {
            edt_Email.setError("Please Enter Your Last Name");
            edt_Email.requestFocus();
            result = false;
        }
        else if (edt_Email.getText().toString().trim().isEmpty()) {
            edt_Email.setError("Please Enter Email");
            edt_Email.requestFocus();
            result = false;
        }
        else if (!isEmailValid(edt_Email.getText().toString().trim())) {
            edt_Email.setError("Not a Valid Email Address");
            edt_Email.requestFocus();
            result = false;
        }
         else if (edt_password.getText().toString().trim().isEmpty()) {
             edt_password.setError("Please Enter Password");
             edt_password.requestFocus();
             result = false;
         }
         else if (edt_password.getText().toString().trim().length() < 8) {
             edt_password.setError("Password Can't be less then 8 Digits!");
             edt_password.requestFocus();
             result = false;
         }

        return result;
    }


    private void isUserExits(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

        Call<SuccessToken> call = feedJsonApi.Login(edt_Email.getText().toString().trim(), edt_password.getText().toString().trim());

        call.enqueue(new Callback<SuccessToken>() {
            @Override
            public void onResponse(Call<SuccessToken> call, Response<SuccessToken> response) {
                if (response.isSuccessful()) {
                    Log.i("TAG", "onResponse: "+"token:>>  "+response.body().getSuccess());

                    preferenceData.setUserToken(SignInActivity.this,response.body().getSuccess());
                    preferenceData.setUserLoggedInStatus(SignInActivity.this,true);
                    txt_Error.setVisibility(View.GONE);
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                    finish();
                } else {
                    Log.i("TAG", "onResponse: "+response.code());
                    txt_Error.setVisibility(View.VISIBLE);
                    txt_Error.setText("Email or Password is in Correct");
                }
            }

            @Override
            public void onFailure(Call<SuccessToken> call, Throwable t) {
                Log.i("TAG", "onFailure: " + t.getMessage());
            }
        });


    }


    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void createReferencer() {
        txt_Create_new_account = findViewById(R.id.txt_Create_new_account);
        txt_Forgot_Password= findViewById(R.id.txt_Forgot_Password);
        txt_signwith_Finger = findViewById(R.id.txt_finger_print);
        txt_signwith_face = findViewById(R.id.txt_face_id);
        txt_signwith_PIN = findViewById(R.id.txt_signInWithPIN);
        txt_Error = findViewById(R.id.txt_error);

        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_Password);

        btn_SignIn = findViewById(R.id.btn_SignIn);
        img_face_Id = findViewById(R.id.img_face_id);
        img_Finger_Print = findViewById(R.id.img_finger_print);
        }




    }




