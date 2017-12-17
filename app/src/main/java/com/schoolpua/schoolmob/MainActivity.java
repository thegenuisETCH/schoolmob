package com.schoolpua.schoolmob;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Button btnlog;
    private EditText inputPhone, inputPassword;

    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* phone="01223456789";
        childrenAdapter.studentId="1";
        startActivity(new Intent(MainActivity.this,MapsActivity.class));
        finish();*/

        inputPhone=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.password);
        btnlog=(Button)findViewById(R.id.btn_login);

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputPhone.getText().toString().matches("")||inputPassword.getText().toString().matches("")){
                    Toast.makeText(MainActivity.this,"please insert username & password",Toast.LENGTH_LONG).show();
                }else{

                    phone=inputPhone.getText().toString();

                    final DocumentReference parents = FirebaseFirestore.getInstance().collection("parents").document(String.valueOf(inputPhone.getText()));
                    parents.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String, Object> map = task.getResult().getData();
                            if(task.isSuccessful()){
                                if (map.get("password").equals(inputPassword.getText().toString())){

                                    Map<String, Object> token = new HashMap<>();
                                    token.put("token", FirebaseInstanceId.getInstance().getToken());
                                    parents.set(token, SetOptions.merge());

                                    FirebaseMessaging.getInstance().subscribeToTopic("AllDevices");

                                    Intent i = new Intent(MainActivity.this, home.class);
                                    i.putExtra("phone", phone);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this,"invalid username or password try again",Toast.LENGTH_LONG).show();
                                    inputPassword.setText("");
                                }
                            }else{
                                Toast.makeText(MainActivity.this,"invalid username or password try again",Toast.LENGTH_LONG).show();
                                inputPassword.setText("");
                            }
                        }
                    });
                }
            }
        });
    }
}
