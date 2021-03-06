package com.schoolpua.schoolmob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class grade extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<ArrayList<String>> gradeOfsubject;
    private ArrayList<String> subjectName;

    Map<String,Object> map;
    CollectionReference student;
    ListView subjectList;
    String phone,studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_grade);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_grade);
        navigationView.setNavigationItemSelectedListener(this);

        gradeOfsubject=new ArrayList<ArrayList<String>>();
        subjectName=new ArrayList<String>();

        subjectList=(ListView)findViewById(R.id.listsubject);

        Bundle extras = getIntent().getExtras();
        phone= extras.getString("phone");
        studentId= extras.getString("studentId");

        student = FirebaseFirestore.getInstance().collection("students").document(studentId).collection("subjects");
        student.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (DocumentSnapshot subject:documentSnapshots.getDocuments()) {
                    subjectName.add(subject.getId());
                    ArrayList<String>temp=new ArrayList<String>();
                    temp.add(String.valueOf(subject.getData().get("final")));
                    temp.add(String.valueOf(subject.getData().get("midterm")));
                    temp.add(String.valueOf(subject.getData().get("quizzes")));
                    gradeOfsubject.add(temp);
                }
                gradeAdapter gradeadapter=new gradeAdapter(grade.this,gradeOfsubject,subjectName);
                subjectList.setAdapter(gradeadapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_grade);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent i = new Intent(this, home.class);
            //i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(this, profile.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(this, attendance.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_grade) {
            /*Intent i = new Intent(this, grade.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();*/
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(this, timetable.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_activities) {
            Intent i = new Intent(this, activities.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_tracking) {
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_supervisor) {
            Intent i = new Intent(this, callSupervisor.class);
            i.putExtra("studentId", studentId);
            i.putExtra("phone", phone);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_grade);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
