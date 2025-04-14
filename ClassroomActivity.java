package com.example.pfe;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassroomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassroomAdapter adapter;
    private List<Classroom> classroomList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        recyclerView = findViewById(R.id.recyclerViewClassrooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        classroomList = new ArrayList<>();
        adapter = new ClassroomAdapter(this, classroomList);
        recyclerView.setAdapter(adapter);

        // Référence Firebase pour "classrooms"
        databaseReference = FirebaseDatabase.getInstance().getReference("classrooms");

        // Charger les données depuis Firebase
        loadClassrooms();
    }

    private void loadClassrooms() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classroomList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Classroom classroom = snapshot.getValue(Classroom.class);
                    classroomList.add(classroom);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClassroomActivity.this, "Erreur de chargement : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
