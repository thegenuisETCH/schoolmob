package com.schoolpua.schoolmob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.Map;

public class home extends AppCompatActivity {

    Button btn_child;
    ArrayList<String> names,classs,studentIds,pics;
    ListView childlist;
    DocumentReference parents;
    Map<String,Object> map;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        childlist=(ListView)findViewById(R.id.childlist);
        names=new ArrayList<String>();
        classs=new ArrayList<String>();
        studentIds=new ArrayList<String>();
        pics=new ArrayList<String>();
        final int flag[]=new int[1];

        Bundle extras = getIntent().getExtras();
        phone= extras.getString("phone");


        parents = FirebaseFirestore.getInstance().collection("parents").document(String.valueOf(phone));
        parents.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                map = (Map<String, Object>) task.getResult().getData().get("children");
                for (int i=0;i<map.size();i++) {
                    // TODO: handle the post
                    String key=String.valueOf(map.keySet().toArray()[i]);
                    Map<String,String> map1= (Map<String, String>) map.get(key);
                    names.add(map1.get("name"));
                    classs.add(map1.get("class"));
                    pics.add(map1.get("pic"));
                    FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(map1.get("class")));
                    studentIds.add(key);
                    childrenAdapter childrenadapter=new childrenAdapter(home.this,names,classs,studentIds,pics,phone);
                    childlist.setAdapter(childrenadapter);
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_profile) {
            /*Intent i = new Intent(this, home.class);
            //i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();*/
            return true;
        }else if(id == R.id.action_settings_logout){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginDetails", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(this,login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
