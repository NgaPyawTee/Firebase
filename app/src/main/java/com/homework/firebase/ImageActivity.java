package com.homework.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements AdapterForRE.Listener {
    private RecyclerView recyclerView;
    private AdapterForRE adapter;
    private List<Upload> list = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    private ValueEventListener valueEventListener;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterForRE(ImageActivity.this,list);
        adapter.setListener(ImageActivity.this);
        recyclerView.setAdapter(adapter);

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    list.add(upload);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this, "Normal click on position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoWhateverClicked(int position) {
        Toast.makeText(this, "Do whatever click on position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(int position) {
        Upload selectedItem = list.get(position);
        String selectedKey = selectedItem.getKey();

        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageURL());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(ImageActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}