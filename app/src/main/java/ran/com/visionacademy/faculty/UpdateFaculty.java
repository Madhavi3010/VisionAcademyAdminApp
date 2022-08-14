package ran.com.visionacademy.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import ran.com.visionacademy.R;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView physicsXiXii, chemistryXiXii , mathematicsXiXii , biologyXiXii , engineering;
    private LinearLayout phyNoData,matNoData,chemNoData,bioNoData,enggNoData;
    private List<TeacherData> list1,list2, list3, list4, list5;
    private TeacherAdapter adapter;
    
    private DatabaseReference reference , dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        physicsXiXii = findViewById(R.id.physicsXiXii);
        chemistryXiXii = findViewById(R.id.chemistryXiXii);
        mathematicsXiXii = findViewById(R.id.mathematicsXiXii);
        biologyXiXii = findViewById(R.id.biologyXiXii);
        engineering = findViewById(R.id.engineering);

        phyNoData = findViewById(R.id.phyNoData);
        chemNoData = findViewById(R.id.chemNoData);
        matNoData = findViewById(R.id.matNoData);
        bioNoData = findViewById(R.id.bioNoData);
        enggNoData = findViewById(R.id.enggNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        
        physicsXiXii();
        mathematicsXiXii();
        chemistryXiXii();
        biologyXiXii();
        engineering();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this, AddTeacher.class));

            }
        });
    }

    private void physicsXiXii() {
        dbRef = reference.child("PHYSICS XI-XII");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    phyNoData.setVisibility(View.VISIBLE);
                    physicsXiXii.setVisibility(View.GONE);
                }else {
                    phyNoData.setVisibility(View.GONE);
                    physicsXiXii.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    physicsXiXii.setHasFixedSize(true);
                    physicsXiXii.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1, UpdateFaculty.this, "PHYSICS XI-XII");
                    physicsXiXii.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void chemistryXiXii() {
        dbRef = reference.child("CHEMISTRY XI-XII");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    chemNoData.setVisibility(View.VISIBLE);
                    chemistryXiXii.setVisibility(View.GONE);
                }else {
                    chemNoData.setVisibility(View.GONE);
                    chemistryXiXii.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    chemistryXiXii.setHasFixedSize(true);
                    chemistryXiXii.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2, UpdateFaculty.this,"CHEMISTRY XI-XII");
                    chemistryXiXii.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void mathematicsXiXii() {
        dbRef = reference.child("MATHEMATICS XI-XII");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    matNoData.setVisibility(View.VISIBLE);
                    mathematicsXiXii.setVisibility(View.GONE);
                }else {
                    matNoData.setVisibility(View.GONE);
                    mathematicsXiXii.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    mathematicsXiXii.setHasFixedSize(true);
                    mathematicsXiXii.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3, UpdateFaculty.this,"MATHEMATICS XI-XII");
                    mathematicsXiXii.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void biologyXiXii() {
        dbRef = reference.child("BIOLOGY XI-XII");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    bioNoData.setVisibility(View.VISIBLE);
                    biologyXiXii.setVisibility(View.GONE);
                }else {
                    bioNoData.setVisibility(View.GONE);
                    biologyXiXii.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    biologyXiXii.setHasFixedSize(true);
                    biologyXiXii.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4, UpdateFaculty.this,"BIOLOGY XI-XII");
                    biologyXiXii.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void engineering() {
        dbRef = reference.child("ENGINEERING");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list5 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    enggNoData.setVisibility(View.VISIBLE);
                    engineering.setVisibility(View.GONE);
                }else {
                    enggNoData.setVisibility(View.GONE);
                    engineering.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    engineering.setHasFixedSize(true);
                    engineering.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list5, UpdateFaculty.this,"ENGINEERING");
                    engineering.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });

    }
}