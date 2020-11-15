package com.contacthrd.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivitiy extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpusername,signUppassword,inputPhonNumID;
    private Button signUpButton;

    User_Details_loginPage userDetails_loginPage;
    Database_login_Helper database_login_helper;
    private TextInputLayout textinputPassword;
    String YourEditTextValue;
     EditText editText;
    //firebase authentication

    FirebaseAuth fAuthor; //create object

    private String getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        fAuthor = FirebaseAuth.getInstance();

        signUpusername = (EditText)findViewById(R.id.signUpUsernameEdittextId);
        //   signUppassword = (EditText)findViewById(R.id.signUpPasswordEdittextId);

        textinputPassword = (TextInputLayout) findViewById(R.id.login_passwordId);
        inputPhonNumID = findViewById(R.id.inputPhonNumID);

        database_login_helper = new Database_login_Helper(this);
        userDetails_loginPage = new User_Details_loginPage();

        signUpButton = (Button)findViewById(R.id.signUpButtonId);
        signUpButton.setOnClickListener(this);


    }


    //for update data input text
    private  boolean validateUsername(){

        String username = signUpusername.getText().toString();

        if(TextUtils.isEmpty(username)) {
            signUpusername.setError("Field can't Empty!");
            return false;
        }else {
            signUpusername.setError(null);
            return true;
        }
    }

    private boolean validateNumber(){
        String inputNumber = inputPhonNumID.getText().toString();

        String phoneNumber = inputPhonNumID.getText().toString();

        if(inputNumber.matches("")){
            inputPhonNumID.setError("empty!");
            return false;
        }
        if (phoneNumber.length() <11){
            inputPhonNumID.setError("invalid");
            return false;
        }

        return true;
    }

    //for update data input text
    private  boolean validatePassword(){

//        String password = signUppassword.getText().toString();
//
//        if(TextUtils.isEmpty(password)) {
//            signUppassword.setError("Field can't Empty!");
//            return false;
//        }else {
//            signUppassword.setError(null);
//            return true;
//        }

        String passwordInput = textinputPassword.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            textinputPassword.setError("Field can't be Empty!");
            return false;
        }else {
            textinputPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        String username = signUpusername.getText().toString();
//        //  String password = signUppassword.getText().toString();
//        String password = textinputPassword.getEditText().getText().toString().trim();
//        String phoneNumber = inputPhonNumID.getText().toString();

        if( !validateUsername() || !validatePassword() || !validateNumber() ){
            return;
        }else{

            sendVariCode();
            editText = new EditText(getApplicationContext());
            alert.setMessage("Enter Verification Code");
//            alert.setTitle("Enter Your Title");

            alert.setView(editText);

            alert.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                 //   Editable YouEditTextValue = edittext.getText();
                    //OR
                     YourEditTextValue = editText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getCode, YourEditTextValue);
                    signInWithPhoneAuthCredential(credential);

                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            AlertDialog dialog = alert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

    }



    //send via code
    private void sendVariCode() {
        String phoneNumber = inputPhonNumID.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            Log.d("code", "onVerificationCompleted: ");
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            //    e.printStackTrace();
            Log.d("code", "onVerificationFailed: "+e);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.e("code", "onCodeSent: sss"+s );
            Log.e("code", "onCodeSent2: sss"+forceResendingToken );
            getCode = s;
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuthor.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String username = signUpusername.getText().toString();
                            //  String password = signUppassword.getText().toString();
                            String password = textinputPassword.getEditText().getText().toString().trim();
                            userDetails_loginPage.setUsername(username);
                            userDetails_loginPage.setPassword(password);

                            long rowId = database_login_helper.insertData(userDetails_loginPage);

                            if(rowId>0){
                                Toast.makeText(getApplicationContext(),"Row "+rowId+" is successfully inserted",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Row inserted failed",Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Code incorrect",Toast.LENGTH_SHORT).show();
                                if(!editText.getText().toString().isEmpty()){
                                    editText.setText("");
                                }
                            }

                        }
                    }
                });
    }
}
