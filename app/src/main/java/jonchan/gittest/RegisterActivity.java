package jonchan.gittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private RadioGroup radioGroup;
    private RadioButton rBtn;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mNameField = (EditText) findViewById(R.id.nameField);
        mRegisterBtn = (Button) findViewById(R.id.btnRegister);
        radioGroup = (RadioGroup) findViewById(R.id.rBtnGroup);
        mProgress = new ProgressDialog(this);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
            }
        });
    }

    private void startRegister(){

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        final String name = mNameField.getText().toString();
        int selectedID = radioGroup.getCheckedRadioButtonId();
        rBtn = (RadioButton) findViewById(selectedID);
        final String userType = rBtn.getText().toString();


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)){

            Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

        } else if(password.length() < 6) {

            Toast.makeText(RegisterActivity.this, "Password must contain at a least 6 characters", Toast.LENGTH_SHORT).show();

        } else {

                mProgress.setMessage("Signing Up...");
                mProgress.show();
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            database = FirebaseDatabase.getInstance();
                            mRef = database.getReference("Users");

                            //get the UID of the current logined user
                            String user_id = mAuth.getCurrentUser().getUid();

                            //create a new branch under "Users" where the branch name is the uid get above
                            DatabaseReference current_user_db = mRef.child(user_id);

                            current_user_db.child("Name").setValue(name);
                            current_user_db.child("UserType").setValue(userType);



                            mProgress.dismiss();

                            Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
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





