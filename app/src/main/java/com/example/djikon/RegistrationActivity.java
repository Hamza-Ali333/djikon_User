package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.ResponseModels.LoginRegistrationModel;
import com.example.djikon.ResponseModels.SuccessErrorModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EMAIL = "email";

    private CallbackManager mCallbackManager;
    private EditText edt_Name, edt_LastName, edt_Email, edt_Password, edt_C_Password, edt_Referal_Code;

    private Button btn_SignUp;
    private LoginButton btn_FBSignUp;
    private SignInButton btn_GoogleSignIn;

    private RadioButton radioButton;
    private PreferenceData preferenceData;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private static final Integer RC_SIGN_IN = 736;

    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private int OTP = 0;
    private String EmailForOTP;
    private String Password;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    int seconds;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        createReferences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        preferenceData = new PreferenceData();

        btn_FBSignUp.setReadPermissions(Arrays.asList(EMAIL));
        mCallbackManager = CallbackManager.Factory.create();

        btn_FBSignUp.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                try {
                                    // Application code
                                    String email = response.getJSONObject().getString("email");
                                    String firstName = object.getString("first_name");
                                    firstName = firstName+" "+object.get("middle_name");
                                    String lastName = object.getString("last_name");

                                    signUpNewUser(true,
                                            firstName,
                                            lastName,
                                            email);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this,false,"Note","Something happened wrong please try again or SingUp with Formally");
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, middle_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this,false,
                        "Note",
                        "FaceBook Login is Cancled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this,false,
                        "Note",
                        "Something happened wrong please try again or SingUp with Formally");
                Log.i("TAG", "onError: "+exception);
            }
        });


        edt_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_Password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_C_Password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_C_Password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!radioButton.isSelected()) {
                    radioButton.setChecked(true);
                    radioButton.setSelected(true);
                } else {
                    radioButton.setChecked(false);
                    radioButton.setSelected(false);
                }
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInfoRight()) {
                    EmailForOTP = edt_Email.getText().toString().trim();
                    Password = edt_Password.getText().toString().trim();
                    signUpNewUser(false, edt_Name.getText().toString().trim(),
                            edt_LastName.getText().toString().trim(),
                            EmailForOTP);
                }
            }//if
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //   .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


    }//onCreate

    private boolean isInfoRight() {

        boolean result = true;
        if (edt_Name.getText().toString().trim().isEmpty()) {
            edt_Name.setError("Please Enter Your First Name");
            edt_Name.requestFocus();
            result = false;
        } else if (edt_LastName.getText().toString().trim().isEmpty()) {
            edt_LastName.setError("Please Enter Your Last Name");
            edt_LastName.requestFocus();
            result = false;
        } else if (edt_Email.getText().toString().trim().isEmpty()) {
            edt_Email.setError("Please Enter Email");
            edt_Email.requestFocus();
            result = false;
        } else if (!isEmailValid(edt_Email.getText().toString().trim())) {
            edt_Email.setError("Not a Valid Email Address");
            edt_Email.requestFocus();
            result = false;
        } else if (edt_Password.getText().toString().trim().isEmpty()) {
            edt_Password.setError("Please Enter Your Password");
            edt_Password.requestFocus();
            result = false;
        } else if (edt_Password.getText().toString().trim().length() < 8) {
            edt_Password.setError("Password Can't be less then 8 Digits!");
            edt_Password.requestFocus();
            result = false;
        } else if (edt_C_Password.getText().toString().trim().isEmpty()) {
            edt_C_Password.setError("Please Confirm Your Password");
            edt_C_Password.requestFocus();
            result = false;
        } else if (!edt_Password.getText().toString().trim().equals(edt_C_Password.getText().toString().trim())) {
            edt_Password.setError("Password Not Matched");
            edt_Password.requestFocus();
            result = false;
        } else if (edt_Referal_Code.getText().toString().trim().isEmpty()) {
            edt_Referal_Code.setError("Please Enter Referal Code");
            edt_Referal_Code.requestFocus();
            result = false;
        } else if (!radioButton.isChecked()) {
            Toast.makeText(this, "You are not Agree with Terms And Condition", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void signUpNewUser(Boolean isSignUpWithSocialMedia,String firstName,String lastName,String email) {
        progressDialog = DialogsUtils.showProgressDialog(RegistrationActivity.this, "Checking Credentials", "Please Wait...");
        retrofit = ApiClient.retrofit(this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<LoginRegistrationModel> call;
        if(isSignUpWithSocialMedia){
          call = jsonApiHolder.registerUserFromSocialMedia(
                    firstName,
                    lastName,
                    email,
                    1,
                    2);
        }else {
            call = jsonApiHolder.registerUser(
                    firstName,
                    lastName,
                    email,
                    Password,
                    edt_Referal_Code.getText().toString().trim(),
                    2);
        }

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                Log.i("TAG", "onResponse: "+response.code());
                if (response.isSuccessful()) {

                    //store the Email adrress for sending otp on it
                    if(!isSignUpWithSocialMedia){
                        progressDialog.dismiss();
                        EmailForOTP = edt_Email.getText().toString();
                        openVerfiyOTPDialogue();
                    }else {
                        progressDialog.dismiss();
                        LoginRegistrationModel data = response.body();

                        saveDataInPreferences(data.getSuccess(),
                                String.valueOf(data.getId()),
                                data.getFirstname()+" "+data.getLastname(),
                                data.getProfile_image(),
                                EmailForOTP,
                                Password);

                        lunchNextActivity();
                    }
                } else if (response.code() == 409) {
                    if(isSignUpWithSocialMedia){
                        progressDialog.dismiss();
                        alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this,
                                false,"Note","this account is already registered\n" +
                                        "You Can Sign in Now");
                    }else {
                        progressDialog.dismiss();
                        edt_Email.requestFocus();
                        edt_Email.setError("Email Already Exit");
                    }
                } else if (response.code() == 400) {
                    //reffral
                    if(!isSignUpWithSocialMedia){
                        progressDialog.dismiss();
                        edt_Referal_Code.requestFocus();
                        edt_Referal_Code.setError("Refferal not found");
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Somthing Happend Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
                alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this, false, "No Internet", "Please Check Your Internet Connection");
            }
        });
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> taskCompete) {
        try {
            GoogleSignInAccount acc = taskCompete.getResult(ApiException.class);
            progressDialog = DialogsUtils.showProgressDialog(RegistrationActivity.this, "Checking Credentials", "Please Wait...");
            Log.i("TAG", "handleSignInResult: Done");
            signUpNewUser(true,
                    acc.getGivenName(),
                    acc.getFamilyName(),
                    acc.getEmail());

        } catch (ApiException e) {
            progressDialog.dismiss();
            Log.i("TAG", "handleSignInResult: Failed " + e.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void fetchProfileFromGoogle(GoogleSignInAccount acct) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
    }

    private void openVerfiyOTPDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_varification_code, null);

        EditText edt_pin1, edt_pin2, edt_pin3, edt_pin4;
        edt_pin1 = view.findViewById(R.id.pin1);
        edt_pin2 = view.findViewById(R.id.pin2);
        edt_pin3 = view.findViewById(R.id.pin3);
        edt_pin4 = view.findViewById(R.id.pin4);

        TextView error = view.findViewById(R.id.txt_error);

        TextView OTP_Timmer = view.findViewById(R.id.otp_timmer);
        Button btn_Resend_OTP = view.findViewById(R.id.resend_otp);
        ImageView img_close = view.findViewById(R.id.close);

        img_close.setClickable(false);//Make User to Unable to close Dailoge Until the time End
        img_close.setEnabled(false);

        //this show a time to get email again if first one is not recieved unfortunetly
        startTimer(OTP_Timmer, btn_Resend_OTP, img_close);


        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        edt_pin1.setFocusable(true);

        edt_pin1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_pin1.length() == 1) {

                    edt_pin1.clearFocus();
                    edt_pin2.requestFocus();
                    edt_pin2.setCursorVisible(true);

                }

            }

            //textWatcher extra method not in use
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {

            }
        });


        edt_pin2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_pin2.length() == 1) {
                    edt_pin2.clearFocus();
                    edt_pin3.requestFocus();
                    edt_pin3.setCursorVisible(true);
                }

            }

            //textWatcher extra method not in use
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });


        edt_pin3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_pin3.length() == 1) {

                    edt_pin3.clearFocus();
                    edt_pin4.requestFocus();
                    edt_pin4.setCursorVisible(true);
                }

            }

            //textWatcher extra method not in use
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {


            }
        });


        edt_pin4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_pin4.length() == 1) {
                    progressDialog = DialogsUtils.showProgressDialog(RegistrationActivity.this, "Checking OTP", "Please Wait");

                    String builder = edt_pin1.getText().toString()
                            + edt_pin2.getText().toString()
                            + edt_pin3.getText().toString() +
                            edt_pin4.getText().toString();

                    edt_pin4.clearFocus();

                    OTP = Integer.parseInt(builder);

                    Call<LoginRegistrationModel> call = jsonApiHolder.verifyEmail(EmailForOTP, OTP);

                    call.enqueue(new Callback<LoginRegistrationModel>() {
                        @Override
                        public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {

                            if (response.isSuccessful()) {
                                error.setVisibility(View.GONE);
                                alertDialog.dismiss();
                                progressDialog.dismiss();

                                LoginRegistrationModel data = response.body();

                                saveDataInPreferences(data.getSuccess(),
                                        String.valueOf(data.getId()),
                                        data.getFirstname()+" "+data.getLastname(),
                                        data.getProfile_image(),
                                        EmailForOTP,
                                        Password);

                                lunchNextActivity();
                            } else {
                                error.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });


                }

            }

            //textWatcher extra method not in use
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {


            }
        });


        btn_Resend_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Resend_OTP.setClickable(false);
                //this class will send new email with new otp on EmailForOTP
                new ResendOTPToEmail(btn_Resend_OTP,img_close,OTP_Timmer).execute(EmailForOTP);
            }
        });

    }//openVerifyOTPDialoge

    private class  ResendOTPToEmail extends  AsyncTask<String,Void,Void> {
        Button btn_Resend_OTP;
        ImageView img_close;
        TextView OTP_Timmer;

        public ResendOTPToEmail(Button btn_Resend_OTP, ImageView img_close, TextView OTP_Timmer) {
            this.btn_Resend_OTP = btn_Resend_OTP;
            this.img_close = img_close;
            this.OTP_Timmer = OTP_Timmer;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Call<SuccessErrorModel> call = jsonApiHolder.resendOTP(strings[0]);
            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                                img_close.setClickable(false);
                                OTP_Timmer.setVisibility(View.VISIBLE);
                                startTimer(OTP_Timmer, btn_Resend_OTP, img_close);
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistrationActivity.this, "Failed To Send Email Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            return null;
        }
    }

    private void startTimer(TextView otp_timmer, Button resentOTP, ImageView closeDailog) {
        resentOTP.setVisibility(View.GONE);
        seconds = 30;
        Timer t = new Timer();    //declare the timer
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (seconds == 00) {
                            otp_timmer.setText(String.format("%02d", seconds));

                            t.cancel();
                            otp_timmer.setVisibility(View.GONE);
                            resentOTP.setVisibility(View.VISIBLE);

                            resentOTP.setClickable(true);//make User to click an request for new OTP
                            closeDailog.setClickable(true);//make user able to close the Dialoge
                            closeDailog.setEnabled(true);
                        }
                        seconds--;
                        otp_timmer.setText("if not get email then try again in " + String.format("%02d", seconds));
                    }
                });
            }
        }, 0, 1000);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void createReferences() {
        edt_Name = findViewById(R.id.edt_first_name);
        edt_LastName = findViewById(R.id.edt_last_name);
        edt_Email = findViewById(R.id.edt_email);
        edt_Password = findViewById(R.id.edt_password);
        edt_C_Password = findViewById(R.id.edt_c_password);
        edt_Referal_Code = findViewById(R.id.edt_refrel_code);
        btn_SignUp = findViewById(R.id.btn_sign_up);
        btn_FBSignUp = findViewById(R.id.btn_fb_sign_up);
        btn_GoogleSignIn = findViewById(R.id.btn_google_sign_up);

        radioButton = findViewById(R.id.radiobutton_term);
    }

    private void lunchNextActivity(){
        Intent i = new Intent(RegistrationActivity.this,MainActivity.class);
        i.putExtra("come_from_registration",true);
        startActivity(i);
        finish();
    }

    private void saveDataInPreferences(String userToken, String userId, String userName, String profileImage,String userEmail,String userPassword){
        preferenceData.setUserToken(RegistrationActivity.this, userToken);
        preferenceData.setUserId(RegistrationActivity.this, userId);
        preferenceData.setUserName(RegistrationActivity.this, userName);
        preferenceData.setUserImage(RegistrationActivity.this, profileImage);
        preferenceData.setUserLoggedInStatus(RegistrationActivity.this, true);
        preferenceData.setUserEmail(RegistrationActivity.this, userEmail);
        preferenceData.setUserPassword(RegistrationActivity.this, userPassword);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}
