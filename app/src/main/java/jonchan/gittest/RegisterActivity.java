package jonchan.gittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private RadioGroup radioGroup;
    private RadioButton rBtn;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private RadioButton radioTeacher;
    private RadioButton radioStuder;
    private TextView txtTeacher;
    private TextView txtStudent;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Typeface tf;
    private Typeface tfl;
    private String selectedID;

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
        radioTeacher = (RadioButton) findViewById(R.id.rBtnTeacher);
        radioStuder = (RadioButton) findViewById(R.id.rBtnStudent);
        txtTeacher = (TextView) findViewById(R.id.txtTeacher);
        txtStudent = (TextView) findViewById(R.id.txtStudent);

        tf = Typeface.createFromAsset(getAssets(), "fonts/montserratsemibold.ttf");
        tfl = Typeface.createFromAsset(getAssets(), "montserratlight.ttf");

        mEmailField.setTypeface(tfl);
        mPasswordField.setTypeface(tfl);
        mNameField.setTypeface(tfl);
        mRegisterBtn.setTypeface(tf);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
            }
        });
    }

    protected void onStart() {



        super.onStart();

        radioStuder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioStuder.setAlpha(1f);
                radioTeacher.setAlpha(0.5f);
                txtStudent.setAlpha(1f);
                txtTeacher.setAlpha(0.5f);
                selectedID = "Student";
            }
        });

        radioTeacher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                radioTeacher.setAlpha(1f);
                radioStuder.setAlpha(0.5f);
                txtTeacher.setAlpha(1f);
                txtStudent.setAlpha(0.5f);
                selectedID = "Teacher";
            }
        });
    }

    private void startRegister(){

        final String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        final String name = mNameField.getText().toString();
        //int selectedID = radioGroup.getCheckedRadioButtonId();
        //rBtn = (RadioButton) findViewById(selectedID);
        final String userType = selectedID;


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

                            //create a new branch under "Users" w here the branch name is the uid get above
                            DatabaseReference current_user_db = mRef.child(user_id);

                            current_user_db.child("Name").setValue(name);
                            current_user_db.child("UserType").setValue(userType);
                            current_user_db.child("Email").setValue(email);



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





