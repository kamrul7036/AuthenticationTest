package com.contacthrd.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText signInusernameEdittext,signinpasswordEdittext;
    Button signinButton,buttonSignUp;

//    FirebaseAuth fAuthor; //create object
//
//    private String getCode;
    Database_login_Helper database_login_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize obj
//        fAuthor = FirebaseAuth.getInstance();

        //edittext
        signInusernameEdittext = (EditText)findViewById(R.id.userNameEditID);
        signinpasswordEdittext = (EditText)findViewById(R.id.passwordEditId);
        //button
//        buttonGetVarification = findViewById(R.id.sendvariFButtonID);
        signinButton = findViewById(R.id.singinButtonID);
        buttonSignUp = findViewById(R.id.signUpButtonID);

//        buttonGetVarification.setOnClickListener(this);
        signinButton.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);


        database_login_helper = new Database_login_Helper(this);
    }

    @Override
    public void onClick(View v) {
        String username = signInusernameEdittext.getText().toString();
        String password = signinpasswordEdittext.getText().toString();
        switch (v.getId()){
            case R.id.singinButtonID:
                if( username.equals("") ){
                    signinButton.setError("empty");
                    return;
                }
                 if(password.equals("")){
                     signinpasswordEdittext.setError("Empty");
                 }


                boolean result = database_login_helper.findPassword(username,password);
                if(result == true){
                    Intent intent = new Intent(this,Succesfull_Login.class);
                 //   intent.putExtra("puttext","You are successfully login...");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Username or Password Incorrect",Toast.LENGTH_LONG).show();
                }


                break;

            case R.id.signUpButtonID:
                Intent intent = new Intent(getApplicationContext(), SignUpActivitiy.class);
                startActivity(intent);
                break;

        }
    }

    //call from sing in button
//    private void signInMethod() {
//
//        String ownCode = .getText().toString();
//        if(ownCode.matches("")){
//            numberEditText.setError("empty!");
//            return;
//        }
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getCode, ownCode);
//        signInWithPhoneAuthCredential(credential);
//    }

////    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
////        fAuthor.signInWithCredential(credential)
////                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
////                        if (task.isSuccessful()) {
////                            Intent intent = new Intent(getApplicationContext(), SignUpActivitiy.class);
////                            startActivity(intent);
////                        } else {
////                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
////                                Toast.makeText(getApplicationContext(),"Code incorrect",Toast.LENGTH_SHORT).show();
////                                if(!codeEditText.getText().toString().isEmpty()){
////                                    codeEditText.setText("");
////                                }
////                            }
////
////                        }
////                    }
////                });
//    }


//    private void sendVariCode() {
//        String phoneNumber = numberEditText.getText().toString();
//
//         if(phoneNumber.matches("")){
//             numberEditText.setError("empty!");
//             return;
//         }
//         if (phoneNumber.length() <11){
//             numberEditText.setError("invalid");
//             return;
//         }
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//               "+880"+phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//
//    }
//
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//            Log.d("code", "onVerificationCompleted: ");
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//        //    e.printStackTrace();
//            Log.d("code", "onVerificationFailed: "+e);
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            Log.e("code", "onCodeSent: "+s );
//            getCode = s;
//        }
//    };

}
