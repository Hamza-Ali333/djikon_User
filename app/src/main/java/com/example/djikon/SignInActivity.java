package com.example.djikon;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;


import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.FingerPrintHandler;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.ResponseModels.LoginRegistrationModel;
import com.example.djikon.ResponseModels.SuccessErrorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


@RequiresApi(api = Build.VERSION_CODES.M)
public class SignInActivity extends AppCompatActivity {

    private TextView txt_Create_new_account, txt_Forgot_Password, txt_signWith_face, txt_signWith_PIN;

    private Button btn_SignIn;
    private RelativeLayout rlt_BiometricPrompt;

    private ImageView img_Finger_Print;
    private EditText edt_Email, edt_Password;
    private PreferenceData preferenceData;
    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;
    private AlertDialog alert_AND_forgetDailoge;//also using for show net error

    private int OTP = 0;
    private String EmailForOTP;
    private int seconds;//seconds for showing CountDown


    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    //finger print
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    //FireBase Authentication
    FirebaseAuth mFirebaseAuth;
    //Google Biometric Promot
    private Executor executor;
    public BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private AlertDialog alertDialog;


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
        //firebase Auth i
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        createReferencer();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        //Handel the Result When User Sign In Throw the BioMetric
        singInCallBackHandler();


        retrofit = ApiClient.retrofit( this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        preferenceData = new PreferenceData();

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

        txt_Create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegistrationActivity.class));
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
                if (isInfoRight()) {
                    isUserExits(edt_Email.getText().toString().trim(), edt_Password.getText().toString().trim());
                }
            }
        });


        rlt_BiometricPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //first check is user has enable this feature for login
                if(PreferenceData.getBiometricLoginState(SignInActivity.this)){
                    if(!PreferenceData.getUserEmail(SignInActivity.this).equals("no")
                            && !PreferenceData.getUserPassword(SignInActivity.this).equals("no") ){
                        showBiometricPrompt();
                    }else {
                        alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this,
                                false,"Note","Something happened Wrong please login usually and then enable this feature again");
                    }
                }else {
                    alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this,
                            false,"Note","You have to login usually for first " +
                                    "time and make sure after that you enable biometric option in app settings");
                }

            }
        });


            //sign in with face id
        /*txt_signWith_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWithFaceId();
            }
        });
*/
        //SignIn With FingerPrint
/*        img_face_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //openLoginWithFaceId();
            }
        });

        txt_signWith_Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openLoginWithFingerPrint();
            }
        });

        img_Finger_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWithFingerPrint();
            }
        });*/

        txt_signWith_PIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInWithPIN();
            }
        });
    }


    private Boolean isDeviceAbleForBiometricLogin(){
        Boolean result = false;
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");

                result = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                alertDialog = DialogsUtils.showAlertDialog(this,
                        false,
                        "Error",
                        "No biometric features available on this device.");
                result = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                alertDialog = DialogsUtils.showAlertDialog(this,
                        false,
                        "Error",
                        "Biometric features are currently unavailable.");
                result = false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                alertDialog = DialogsUtils.showAlertDialog(this,
                        false,
                        "Error",
                        "The user hasn't associated " +
                                "any biometric credentials with their account.");
                result = false;
                break;
        }
        return  result;
    }

    private void showBiometricPrompt(){
//        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Biometric login for my app")
//                .setSubtitle("Log in using your biometric credential")
//                .setNegativeButtonText("Use account Credentials")
//                .build();
        if(isDeviceAbleForBiometricLogin()){
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    //.setNegativeButtonText("Use account password")
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(true)
                    .build();

            biometricPrompt.authenticate(promptInfo);
        }
    }

    private void singInCallBackHandler () {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SignInActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
               isUserExits (PreferenceData.getUserEmail(SignInActivity.this),
                       PreferenceData.getUserPassword(SignInActivity.this) );
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
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


        alert_AND_forgetDailoge = builder.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmailValid(edtEmail.getText().toString().trim())) {
                    EmailForOTP = edtEmail.getText().toString().trim();

                    progressDialog = DialogsUtils.showProgressDialog(SignInActivity.this, "Checking Email", "Please Wait...");
                    getOTP(EmailForOTP);

                } else {
                    edtEmail.setError("Not An Valid Email");
                    edtEmail.requestFocus();
                }

            }
        });
    }

    private void openVerfiyOTPDialogue(boolean isRunningForINValidEmail) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_varification_code, null);

        EditText edt_pin1, edt_pin2, edt_pin3, edt_pin4;
        edt_pin1 = view.findViewById(R.id.pin1);
        edt_pin2 = view.findViewById(R.id.pin2);
        edt_pin3 = view.findViewById(R.id.pin3);
        edt_pin4 = view.findViewById(R.id.pin4);


        TextView error = view.findViewById(R.id.txt_error);

        TextView Heading = view.findViewById(R.id.heading);
        TextView Info = view.findViewById(R.id.info);

        if (isRunningForINValidEmail) {
            Heading.setText("Verify Your Email");
            Info.setText("Please check your email and enter OTP");
        }


        TextView OTP_Timmer = view.findViewById(R.id.otp_timmer);
        Button btn_Resend_OTP = view.findViewById(R.id.resend_otp);
        ImageView img_close = view.findViewById(R.id.close);

        img_close.setClickable(false);//Make User to Unable to close Dailoge Until the time End

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

                    progressDialog = DialogsUtils.showProgressDialog(SignInActivity.this, "Checking OTP", "Please Wait");

                    String builder = edt_pin1.getText().toString()
                            + edt_pin2.getText().toString()
                            + edt_pin3.getText().toString() +
                            edt_pin4.getText().toString();

                    edt_pin4.clearFocus();

                    OTP = Integer.parseInt(builder);

                    //check Running For new Email Verification or for Forget Password
                    if (isRunningForINValidEmail) {
                        Call<LoginRegistrationModel> verifyEmailCall = jsonApiHolder.verifyEmail(EmailForOTP, OTP);
                        verifyEmailCall.enqueue(new Callback<LoginRegistrationModel>() {
                            @Override
                            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                                if (response.isSuccessful()) {
                                    LoginRegistrationModel data = response.body();

                                    saveDataInPreferences(data.getSuccess(),
                                            String.valueOf(data.getId()),
                                            data.getFirstname()+" "+data.getLastname(),
                                            data.getProfile_image(),
                                            edt_Email.getText().toString().trim(),
                                            edt_Password.getText().toString().trim());

                                    lunchNextActivity();

                                } else {
                                    alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Not", "Some thing happened wrong please verify email again");
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {

                            }
                        });

                    } else {

                        Call<SuccessErrorModel> call = jsonApiHolder.confirmOTP(EmailForOTP, OTP);

                        call.enqueue(new Callback<SuccessErrorModel>() {
                            @Override
                            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                                if (response.isSuccessful()) {
                                    error.setVisibility(View.GONE);
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                    if (isRunningForINValidEmail) {

                                        startActivity(new Intent(SignInActivity.this, MainActivity.class));

                                    } else {
                                        openUpdatePasswrodDialoge();
                                    }

                                } else {
                                    error.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();

                                }
                            }

                            @Override
                            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });

                    }

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

                Call<SuccessErrorModel> call = jsonApiHolder.resendOTP(EmailForOTP);
                call.enqueue(new Callback<SuccessErrorModel>() {
                    @Override
                    public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                        if (response.isSuccessful()) {

                            Toast.makeText(SignInActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                            img_close.setClickable(false);//Make User to Unable to close Dailoge Until the time End
                            startTimer(OTP_Timmer, btn_Resend_OTP, img_close);
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                    }
                });
            }
        });
    }//openVerifyOTPDialoge

    private void startTimer(TextView otp_timmer, Button resentOTP, ImageView closeDailoge) {
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
                            resentOTP.setClickable(true);
                            closeDailoge.setClickable(true);
                        }
                        seconds--;
                        otp_timmer.setText("Resend OTP In " + String.format("%02d", seconds));
                    }
                });
            }
        }, 0, 1000);


    }


    private void openUpdatePasswrodDialoge() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_update_password, null);


        ShowHidePasswordEditText Password = view.findViewById(R.id.edt_new_password);
        ShowHidePasswordEditText confirmPassword = view.findViewById(R.id.edt_ConfirmPassword);

        Button btn_update = view.findViewById(R.id.btn_changepassword);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog = builder.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_update.setEnabled(false);
                if (checkUpdatePasswordInput(Password, confirmPassword)) {
                    progressDialog = DialogsUtils.showProgressDialog(SignInActivity.this, "Updating Password", "Please Wait...");
                    Call<SuccessErrorModel> call = jsonApiHolder.Updatepassword(EmailForOTP,
                            Password.getText().toString().trim());

                    call.enqueue(new Callback<SuccessErrorModel>() {
                        @Override
                        public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                            if (response.isSuccessful()) {
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Login With Your New Password", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                btn_update.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                            alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "No Internet", "Please Check Your Internet Connection");
                        }
                    });
                } else {
                    Toast.makeText(SignInActivity.this, "not Correct", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private boolean checkUpdatePasswordInput(EditText Password, EditText confirmPassword) {

        boolean result = true;
        if (Password.getText().toString().trim().isEmpty()) {
            Password.setError("Please Enter Your New Password");
            Password.requestFocus();
            result = false;
        } else if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Please Enter Your Password Again");
            confirmPassword.requestFocus();
            result = false;
        } else if (Password.getText().length() < 8) {
            Password.setError("Password can't be less then 8 digit");
            edt_Password.requestFocus();
            result = false;
        } else if (!Password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
            Password.setError("Password not matched");
            edt_Password.requestFocus();
            result = false;
        }
        return result;
    }


    private void openLoginWithFaceId() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_faceid, null);


        Button btn_cancle_Face_Id = view.findViewById(R.id.cancel_faceid);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog = builder.show();

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


        TextView Msg = view.findViewById(R.id.msg);
        ImageView img_fingerprint = view.findViewById(R.id.img_biometric);
        Button btn_cancle_Face_Id = view.findViewById(R.id.cancel_fingerprint);

        builder.setView(view);
        builder.setCancelable(false);

        //TODO Check 1: Android version should be greater or equal to Marshmallow
        //TODO Check 2: Device has Fingerprint Scanner
        //TODO Check 3: Have permission to use fingerprint
        //TODO Check 4: Lock Screen is secured with alteast 1 type of lock
        //TODO Check 5: Alteast 1 finger print is registered


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                Msg.setText("Fingerprint Scanner is not Detected");
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Msg.setText("Permission is not Granted");
            }
            //if Lock Screen is not secured with atleast 1 type of lock
            else if (!keyguardManager.isKeyguardSecure()) {

                Msg.setText("Please lock your screen with finger print");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Msg.setText("Please Enroll Atleast on fingerprint to Use this Feature");
            } else {

                Msg.setText("Can Use Finger Print to Login At Your Own Risk");

                try {
                    generateKey();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }

                if (cipherInit()) {
                    FingerPrintHandler fingerPrintHandler = new FingerPrintHandler(this, Msg, img_fingerprint);
                    fingerPrintHandler.startAuth(fingerprintManager, null);
                }
            }
        }


        final AlertDialog alertDialog = builder.show();

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
        final View view = inflater.inflate(R.layout.dailoge_pin_code, null);


        EditText edt_Pin_Code = view.findViewById(R.id.pincode);
        Button btn_Login_Pin = view.findViewById(R.id.btn_login_pin);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.show();

        btn_Login_Pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean isInfoRight() {

        boolean result = true;
        if (edt_Email.getText().toString().trim().isEmpty()) {
            edt_Email.setError("Please Enter Your Email");
            edt_Email.requestFocus();
            result = false;
        } else if (!isEmailValid(edt_Email.getText().toString().trim())) {
            edt_Email.setError("Not a Valid Email Address");
            edt_Email.requestFocus();
            result = false;
        } else if (edt_Password.getText().toString().trim().isEmpty()) {
            edt_Password.setError("Please Enter Password");
            edt_Password.requestFocus();
            result = false;
        } else if (edt_Password.getText().toString().trim().length() < 8) {
            edt_Password.setError("Password Can't be less then 8 Digits!");
            edt_Password.requestFocus();
            result = false;
        }

        return result;
    }


    private void isUserExits(String email,String password) {
        progressDialog = DialogsUtils.showProgressDialog(SignInActivity.this, "Checking Credentials", "Please Wait While Checking...");

        Call<LoginRegistrationModel> call = jsonApiHolder.Login(email,password, 2);

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if (response.isSuccessful()) {
                    LoginRegistrationModel data = response.body();

                    saveDataInPreferences(data.getSuccess(),
                            String.valueOf(data.getId()),
                            data.getFirstname()+" "+data.getLastname(),
                            data.getProfile_image(),
                            email,
                            password);

                    lunchNextActivity();

                } else if (response.code() == 402) {
                    EmailForOTP = edt_Email.getText().toString();
                    Call<SuccessErrorModel> resendOTPCall = jsonApiHolder.resendOTP(EmailForOTP);

                    resendOTPCall.enqueue(new Callback<SuccessErrorModel>() {
                        @Override
                        public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                            if (response.isSuccessful()) {
                                progressDialog.dismiss();
                                openVerfiyOTPDialogue(true);
                            } else {
                                progressDialog.dismiss();
                                alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Note", "Someting happend worng try again");
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                            progressDialog.dismiss();
                            alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Note", "Someting happend worng try again");
                        }
                    });


                } else if (response.code() == 403) {

                    alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Note", "This email is register as Subscriber can't use it here in User App");
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
                alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "No Internet", "Please Check Your Internet Connection");
            }
        });


    }

    private void getOTP(String Email) {
        Call<SuccessErrorModel> call = jsonApiHolder.sendOTP(Email);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                if (response.isSuccessful()) {
                    alert_AND_forgetDailoge.dismiss();
                    progressDialog.dismiss();
                    openVerfiyOTPDialogue(false);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Email Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private void generateKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {

        keyStore = KeyStore.getInstance("AndroidKeyStore");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        keyStore.load(null);
        keyGenerator.init(new
                KeyGenParameterSpec.Builder(KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT |
                        KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());
        keyGenerator.generateKey();

    }

    @RequiresApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", (Throwable) e);
        }

    }


    private void createReferencer() {
        txt_Create_new_account = findViewById(R.id.txt_Create_new_account);
        txt_Forgot_Password = findViewById(R.id.txt_Forgot_Password);
        rlt_BiometricPrompt = findViewById(R.id.biometricpromt);
//      txt_signWith_face = findViewById(R.id.txt_face_id);
        txt_signWith_PIN = findViewById(R.id.txt_signInWithPIN);


        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);

        btn_SignIn = findViewById(R.id.btn_SignIn);
        img_Finger_Print = findViewById(R.id.img_biometric);
    }

    private void lunchNextActivity(){
        Intent i = new Intent(SignInActivity.this,MainActivity.class);
        i.putExtra("come_from_registration",false);
        progressDialog.dismiss();
        startActivity(i);
        finish();
    }

    private void saveDataInPreferences(String userToken, String userId, String userName, String profileImage,String userEmail,String userPassword){
        preferenceData.setUserToken(SignInActivity.this, userToken);
        preferenceData.setUserId(SignInActivity.this, userId);
        preferenceData.setUserName(SignInActivity.this, userName);
        preferenceData.setUserImage(SignInActivity.this, profileImage);
        preferenceData.setUserLoggedInStatus(SignInActivity.this, true);
        preferenceData.setUserEmail(SignInActivity.this, userEmail);
        preferenceData.setUserPassword(SignInActivity.this, userPassword);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }



}
