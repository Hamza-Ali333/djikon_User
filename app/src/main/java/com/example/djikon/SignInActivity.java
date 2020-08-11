package com.example.djikon;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.Models.LoginRegistrationModel;
import com.example.djikon.Models.SuccessErrorModel;
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

    private TextView txt_Create_new_account, txt_Forgot_Password, txt_signwith_face, txt_signwith_Finger, txt_signwith_PIN, txt_Error;


    private Button btn_SignIn;

    private ImageView img_face_Id, img_Finger_Print, img_Error_Sign;
    private EditText edt_Email, edt_password;
    private PreferenceData preferenceData;
    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;
    private AlertDialog alert_AND_forgetDailoge;//also using for show net error

    private static final String BASEURL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";

    private int OTP = 0;
    private String EmailForOTP;
    private int seconds;//seconds for showing CountDown


    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    private Executor executor;
    public BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private NetworkChangeReceiver mNetworkChangeReceiver;


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);
        getSupportActionBar().hide();
        createReferencer();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);


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
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

//        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Biometric login for my app")
//                .setSubtitle("Log in using your biometric credential")
//                .setNegativeButtonText("Use account Credentials")
//                .build();


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                //.setNegativeButtonText("Use account password")
                .setConfirmationRequired(false)
                .setDeviceCredentialAllowed(true)
                .build();

        // biometricPrompt.authenticate(promptInfo);


//        BiometricManager biometricManager = BiometricManager.(this);
//        switch (biometricManager.canAuthenticate()) {
//            case BiometricManager.BIOMETRIC_SUCCESS:
//                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                Log.e("MY_APP_TAG", "No biometric features available on this device.");
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                Log.e("MY_APP_TAG", "The user hasn't associated " +
//                        "any biometric credentials with their account.");
//                break;
//        }


        //AlertDialog alertDialog = DialogsUtils.showAlertDailog(this,false,"Sing In Alert","Please do the right thing");


        retrofit = ApiClient.retrofit(BASEURL, this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);


        preferenceData = new PreferenceData();

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_password.setError(null);
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
                    progressDialog = DialogsUtils.showProgressDialog(SignInActivity.this, "Checking Credentials", "Please Wait While Cheking...");
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

        startTimer(OTP_Timmer, btn_Resend_OTP);


        ImageView img_close = view.findViewById(R.id.close);


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


                    if (isRunningForINValidEmail) {
                        Call<LoginRegistrationModel> verifyEmailCall = jsonApiHolder.verifyEmail(EmailForOTP,OTP);
                        verifyEmailCall.enqueue(new Callback<LoginRegistrationModel>() {
                            @Override
                            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                                if (response.isSuccessful()) {

                                    Log.i("TAG", "onResponse: " + "token:>>  " + response.body().getSuccess());

                                    preferenceData.setUserToken(SignInActivity.this, response.body().getSuccess());

                                    String id = String.valueOf(response.body().getId());

                                    preferenceData.setUserId(SignInActivity.this, id);


                                    preferenceData.setUserName(SignInActivity.this, response.body().getFirstname() + " " + response.body().getLastname());
                                    Log.i("TAG", "onResponse: first " + response.body().getFirstname() + " " + response.body().getLastname());

                                    preferenceData.setUserImage(SignInActivity.this, response.body().getProfile_image());

                                    preferenceData.setUserLoggedInStatus(SignInActivity.this, true);

                                    txt_Error.setVisibility(View.GONE);
                                    img_Error_Sign.setVisibility(View.GONE);
                                    progressDialog.dismiss();


                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish();

                                }else {
                                    alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this,false,"Not","Some thing happened wrong please verify email again");
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
                            startTimer(OTP_Timmer, btn_Resend_OTP);
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                    }
                });
            }
        });

    }//openVerifyOTPDialoge

    private void startTimer(TextView otp_timmer, Button resentOTP) {
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
            edt_password.requestFocus();
            result = false;
        } else if (!Password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
            Password.setError("Password not matched");
            edt_password.requestFocus();
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
        ImageView img_fingerprint = view.findViewById(R.id.img_finger_print);
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
        } else if (edt_password.getText().toString().trim().isEmpty()) {
            edt_password.setError("Please Enter Password");
            edt_password.requestFocus();
            result = false;
        } else if (edt_password.getText().toString().trim().length() < 8) {
            edt_password.setError("Password Can't be less then 8 Digits!");
            edt_password.requestFocus();
            result = false;
        }

        return result;
    }


    private void isUserExits() {

        Call<LoginRegistrationModel> call = jsonApiHolder.Login(edt_Email.getText().toString().trim(), edt_password.getText().toString().trim(), 2);

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if (response.isSuccessful()) {

                    Log.i("TAG", "onResponse: " + "token:>>  " + response.body().getSuccess());

                    preferenceData.setUserToken(SignInActivity.this, response.body().getSuccess());

                    String id = String.valueOf(response.body().getId());

                    preferenceData.setUserId(SignInActivity.this, id);


                    preferenceData.setUserName(SignInActivity.this, response.body().getFirstname() + " " + response.body().getLastname());
                    Log.i("TAG", "onResponse: first " + response.body().getFirstname() + " " + response.body().getLastname());

                    preferenceData.setUserImage(SignInActivity.this, response.body().getProfile_image());

                    preferenceData.setUserLoggedInStatus(SignInActivity.this, true);

                    txt_Error.setVisibility(View.GONE);
                    img_Error_Sign.setVisibility(View.GONE);
                    progressDialog.dismiss();


                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();

                } else if (response.code() == 402) {
                    EmailForOTP = edt_Email.getText().toString();
                    Call<SuccessErrorModel> resendOTPCall = jsonApiHolder.resendOTP(EmailForOTP);

                    resendOTPCall.enqueue(new Callback<SuccessErrorModel>() {
                        @Override
                        public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                            if (response.isSuccessful()) {
                                openVerfiyOTPDialogue(true);
                                progressDialog.dismiss();
                            } else {

                                alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Note", "Someting happend worng try again");
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                            alert_AND_forgetDailoge = DialogsUtils.showAlertDialog(SignInActivity.this, false, "Note", "Someting happend worng try again");
                        }
                    });


                } else if (response.code() == 403) {

                    txt_Error.setText("This Email is not Belongs To User App");
                    txt_Error.setVisibility(View.VISIBLE);
                    img_Error_Sign.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();

                } else {

                    Log.i("TAG", "onResponse: " + response.code());
                    txt_Error.setVisibility(View.VISIBLE);
                    img_Error_Sign.setVisibility(View.VISIBLE);
                    txt_Error.setText("Email or Password is in Correct");
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
        txt_signwith_Finger = findViewById(R.id.txt_finger_print);
        txt_signwith_face = findViewById(R.id.txt_face_id);
        txt_signwith_PIN = findViewById(R.id.txt_signInWithPIN);
        txt_Error = findViewById(R.id.txt_error);

        edt_Email = findViewById(R.id.edt_Email);
        edt_password = findViewById(R.id.edt_Password);

        btn_SignIn = findViewById(R.id.btn_SignIn);
        img_face_Id = findViewById(R.id.img_face_id);
        img_Finger_Print = findViewById(R.id.img_finger_print);
        img_Error_Sign = findViewById(R.id.img_error_sign);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }

}
