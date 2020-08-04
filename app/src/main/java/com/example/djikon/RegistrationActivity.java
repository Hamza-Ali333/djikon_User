package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    String BASEURL_DATA = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    ProgressDialog progressDailoge;
    CallbackManager mCallbackManager;
    private EditText edt_Name, edt_LastName, edt_Email, edt_Password, edt_C_Password, edt_Refral_Code;
    private Button btn_SignUp;
    private LoginButton btn_FBSignUp;
    private RadioButton radioButton;
    private PreferenceData preferenceData;

    private SignInButton btn_GoogleSignIn;
    private GoogleSignInClient mgoogleSignInClient;
    private static  final Integer RC_SIGN_IN = 736;

    private AlertDialog alertDialog;


    private NetworkChangeReceiver mNetworkChangeReceiver;

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
        createRefrences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        preferenceData = new PreferenceData();

        TextView textView = findViewById(R.id.term_info);


        btn_FBSignUp.setReadPermissions(Arrays.asList(EMAIL));
        mCallbackManager = CallbackManager.Factory.create();

        btn_FBSignUp.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String id = loginResult.getAccessToken().toString();

                Toast.makeText(RegistrationActivity.this, "id" + id, Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                try {
                                    // Application code
                                    String email = response.getJSONObject().getString("email");
                                    String Name = object.getString("first_name");

                                    textView.setText("Email : " + email + "\n Name" + Name + " ");
                                    Toast.makeText(RegistrationActivity.this, Name, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name,email,gender,birthday,location");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(RegistrationActivity.this, "FaceBook Login is Cancled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(RegistrationActivity.this, "Something Happend Wrong: try Again", Toast.LENGTH_SHORT).show();
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
                    progressDailoge = DialogsUtils.showProgressDialog(RegistrationActivity.this, "Checking Credentials", "Please Wait...");

                    sendDataToServer();
                }
            }//if
        });



        GoogleSignInOptions gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             //   .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);

        btn_GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


    }//onCreate

    private boolean isInfoRight() {
        Log.i("TAG", "checkInfo: Runing");
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
        } else if (edt_Refral_Code.getText().toString().trim().isEmpty()) {
            edt_Refral_Code.setError("Please Enter Referal Code");
            edt_Refral_Code.requestFocus();
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

    private void sendDataToServer() {

        Retrofit retrofit = ApiClient.retrofit(BASEURL_DATA, this);

        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

        Call<LoginRegistrationModel> call = feedJsonApi.registerUser(
                edt_Name.getText().toString().trim(),
                edt_LastName.getText().toString().trim(),
                edt_Email.getText().toString().trim(),
                edt_Password.getText().toString().trim(),
                edt_C_Password.getText().toString().trim(),
                edt_Refral_Code.getText().toString().trim(),
                2);

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                progressDailoge.dismiss();
                if (response.isSuccessful()) {


                    preferenceData.setUserToken(RegistrationActivity.this, response.body().getSuccess());

                    String id = String.valueOf(response.body().getId());

                    preferenceData.setUserId(RegistrationActivity.this, id);

                    preferenceData.setUserName(RegistrationActivity.this, response.body().getFirstname() + " " + response.body().getLastname());

                    preferenceData.setUserLoggedInStatus(RegistrationActivity.this, true);

                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

                } else if (response.code() == 409) {
                    Log.i("TAG", "onResponse" + " Email Already Exit \n" + response.code());
                    edt_Email.requestFocus();
                    edt_Email.setError("Email Already Exit");
                } else if (response.code() == 400) {
                    //reffral
                    edt_Refral_Code.requestFocus();
                    edt_Refral_Code.setError("Refferal not found");
                } else {
                    Toast.makeText(RegistrationActivity.this, "Somthing Happend Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: " + t.getMessage());
                progressDailoge.dismiss();
                alertDialog = DialogsUtils.showAlertDialog(RegistrationActivity.this,false,"No Internet","Please Check Your Internet Connection");
            }
        });


    }


    private void signInWithGoogle(){

        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private  void handleSignInResult(Task<GoogleSignInAccount> taskCompete){

        try{
            GoogleSignInAccount acc = taskCompete.getResult(ApiException.class);
            Log.i("TAG", "handleSignInResult: Done");

            Toast.makeText(this, "Google sign in success full", Toast.LENGTH_SHORT).show();//should remove

        }catch (ApiException e){

            Log.i("TAG", "handleSignInResult: Failed "+e.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void fetchProfileFromGoogle (GoogleSignInAccount acct ) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

           if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);

            }else {
                    mCallbackManager.onActivityResult(requestCode, resultCode, data);
                }

    }

    private void createRefrences() {

        edt_Name = findViewById(R.id.edt_first_name);
        edt_LastName = findViewById(R.id.edt_last_name);
        edt_Email = findViewById(R.id.edt_email);
        edt_Password = findViewById(R.id.edt_password);
        edt_C_Password = findViewById(R.id.edt_c_password);
        edt_Refral_Code = findViewById(R.id.edt_refrel_code);
        btn_SignUp = findViewById(R.id.btn_sign_up);
        btn_FBSignUp = findViewById(R.id.btn_fb_sign_up);
        btn_GoogleSignIn = findViewById(R.id.btn_google_sign_up);

        radioButton = findViewById(R.id.radiobutton_term);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}
