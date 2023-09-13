package com.arthurjorda.crud_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button logoutButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        // check if user is logged in; if not, go to log in
        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else {
            textView.setText(user.getEmail());
            FirebaseApp.initializeApp(MainActivity.this);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            logoutButton = findViewById(R.id.logout);


            RecyclerView recyclerView = findViewById(R.id.recycler);

            FloatingActionButton add = findViewById(R.id.addUser);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AddUserActivity.class));
                }
            });
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<User> arrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = doc.toObject(User.class);
                            user.setId(doc.getId());
                            arrayList.add(user);
                        }
                        UserAdapter adapter = new UserAdapter(MainActivity.this, arrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(User user) {
                                App.user = user;
                                startActivity(new Intent(MainActivity.this, EditUserActivity.class));
                            }

                            @Override
                            public void onClick(com.google.firebase.firestore.auth.User user) {

                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
                }
            });

            FloatingActionButton refresh = findViewById(R.id.refresh);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<User> arrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    User user = doc.toObject(User.class);
                                    user.setId(doc.getId());
                                    arrayList.add(user);
                                }
                                UserAdapter adapter = new UserAdapter(MainActivity.this, arrayList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(User user) {
                                        App.user = user;
                                        startActivity(new Intent(MainActivity.this, EditUserActivity.class));
                                    }

                                    @Override
                                    public void onClick(com.google.firebase.firestore.auth.User user) {

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            //set onclick for logout button
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();

                    // close main activity ang go back to login
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            });
        }


    }
}