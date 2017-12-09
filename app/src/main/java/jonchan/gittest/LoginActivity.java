package jonchan.gittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Chung on 13/11/2017.
 */

public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private ProgressDialog mProgress;
    private Typeface tf;
    private Typeface tfl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.btnLogin);
        mRegisterBtn = (Button) findViewById(R.id.btnRegister);
        mProgress = new ProgressDialog(this);

        tf = Typeface.createFromAsset(getAssets(), "fonts/montserratsemibold.ttf");
        tfl = Typeface.createFromAsset(getAssets(), "montserratlight.ttf");

        mEmailField.setTypeface(tfl);
        mPasswordField.setTypeface(tfl);

        mLoginBtn.setTypeface(tf);
        mRegisterBtn.setTypeface(tf);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();

            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }

        });


    }

    private void startSignIn(){

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){   // if the email field or password field is empty

            Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

        } else {

            mProgress.setMessage("Login in progress...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {     // login fail if the email of the password is wrong
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();

                    }else{      // login is successful

                        mProgress.dismiss();
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try{
                            startActivity(myIntent);
                        }catch(android.content.ActivityNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
