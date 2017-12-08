package jonchan.gittest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddStudentActivity extends AppCompatActivity {

    private Button btnSendRequest;
    private EditText etxtStudentEmail;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    DatabaseReference mRfRequest;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("ADD CONTACT");
        etxtStudentEmail = (EditText) findViewById(R.id.etxtStudentEmail);
        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");
        etxtStudentEmail.setTypeface(tfmsb);
        btnSendRequest.setTypeface(tfml);


        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = etxtStudentEmail.getText().toString();

                if(TextUtils.isEmpty(email)){

                    Toast.makeText(AddStudentActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

                } else {

                    mAuth = FirebaseAuth.getInstance();
                    final String user_id = mAuth.getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    DatabaseReference studentWithThatEmail;
                    studentWithThatEmail = database.getReference("Users");
                    studentWithThatEmail.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int check = 0;


                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                if(postSnapshot.child("Email").getValue(String.class).equals(email)){
                                    final String student_uid = postSnapshot.getKey();
                                    check = 1;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(AddStudentActivity.this);
                                    alert.setTitle("Confirmation");
                                    alert.setMessage("Are you sure you want to add " + postSnapshot.child("Name").getValue(String.class) + " as student?");

                                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                            mRfRequest = database.getReference().child("Request");
                                            mRfRequest.child(student_uid).setValue(user_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AddStudentActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                            Intent myIntent = new Intent(AddStudentActivity.this, DisplayStudentActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            try{
                                                startActivity(myIntent);
                                            }catch(android.content.ActivityNotFoundException e){
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    alert.show();
                                }

                            }

                            if(check == 0){
                                Toast.makeText(AddStudentActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }





                }
                /*newLesson.child("Date").setValue(date);
                newLesson.child("Teacher").setValue(teacher_id);
                newLesson.child("Student").setValue(student_uid);

                Intent myIntent = new Intent(view.getContext(), DisplayLessonActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }*/


        });



    }
}
