package com.example.lendahand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.services.cognitoidentityprovider.model.StringAttributeConstraintsType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Database database;

    RecyclerView featuredServiceOpsRecyclerView;
    RecyclerView servesWeLoveRecyclerView;
    RecyclerView helpCommunityRecylerView;
    String titles_featured[];
    String subtitles_featured[];
    int images_featured[] = {R.drawable.build_day_image, R.drawable.pancake_image, R.drawable.kids_image};
    private ArrayList<String> ids_servesWelove = new ArrayList<>();
    private ArrayList<String> names_servesWelove = new ArrayList<>();
    private ArrayList<String> subtitles_servesWelove = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = new Database();
        database.init();
        final Database db = new Database();
        db.init();


        //Featured Service Op Banners
        featuredServiceOpsRecyclerView = findViewById(R.id.featured_Service_Ops);
        titles_featured = getResources().getStringArray(R.array.featured_service_ops_titles);
        subtitles_featured = getResources().getStringArray(R.array.featured_service_ops_subtitles);

        FeaturedServeOpsAdaptor featuredAdaptor = new FeaturedServeOpsAdaptor(this, titles_featured, subtitles_featured, images_featured);
        featuredServiceOpsRecyclerView.setAdapter(featuredAdaptor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");



        getServesWeLoveFromDatabase();


        //Help Your Community Recycler View
//        helpCommunityRecylerView = findViewById(R.id.help_your_community_recycler_view);
//        HomescreenCardAdaptor helpYourCommunityAdaptor = new HomescreenCardAdaptor(this);
//        helpCommunityRecylerView.setAdapter(helpYourCommunityAdaptor);
//        helpCommunityRecylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        addTemporaryButtons();
    }

    

    private void addTemporaryButtons() {

        //Adding button to Sign Up A Volunteer
        //STEP 1: Add reference to button using R.id
        MaterialButton signupVolunteer = findViewById(R.id.signup_a_volunteer);

        //STEP 2: Set onClickListener for YOUR button
        signupVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //STEP 3: Create Intent for your class
                Intent volunteerSignupScreen = new Intent(v.getContext(), Volunteer_Signup.class);
                //STEP 4: Start your Activity
                startActivity(volunteerSignupScreen);
            }
        });


        //Adding button to Create Service Opportuntity
        MaterialButton createServiceOp = findViewById(R.id.create_service_opportunity);
        createServiceOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createServiceOpScreen = new Intent(v.getContext(), createServiceOpGenInfo.class);
                startActivity(createServiceOpScreen);

            }
        });

        //Adding button to Create Service Organization
        MaterialButton createServiceOrg = (MaterialButton) findViewById(R.id.create_service_org);

        createServiceOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createServiceOrgScreen = new Intent(v.getContext(), org_signup1.class);
                startActivity(createServiceOrgScreen);

            }
        });

        //Adding button to Search for Service Organization
        MaterialButton searchServiceOp = (MaterialButton) findViewById(R.id.search_service_opportunity);

        searchServiceOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchServiceOpScreen = new Intent(v.getContext(), SearchServiceOpByName.class);
                startActivity(searchServiceOpScreen);

            }
        });

        //Adding button to Login
        MaterialButton Login = (MaterialButton) findViewById(R.id.login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginScreen = new Intent(v.getContext(), Login.class);
                startActivity(LoginScreen);

            }
        });

        MaterialButton SignOut = (MaterialButton) findViewById(R.id.signOut);

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                updateUI(mAuth.getCurrentUser());

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profileIcon:
                Intent LoginScreen = new Intent(MainActivity.this, Login.class);
                startActivity(LoginScreen);
                return true;

            default:
                return this.onOptionsItemSelected(item);

        }
    }

    private void getServesWeLoveFromDatabase() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("servesWeLove")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Regular", document.getId() + " => " + document.getData());


                                ids_servesWelove.add(document.getString("id"));
                                names_servesWelove.add(document.getString("name"));
                                subtitles_servesWelove.add(document.getString("subtitles"));

                            }
                            initServesWeLoveRecylcerView();
                        }
                    }
                });


    }

    private void initServesWeLoveRecylcerView() {
        servesWeLoveRecyclerView = findViewById(R.id.serves_we_love_recycler_view);
        HomescreenCardAdaptor servesWeLoveAdaptor = new HomescreenCardAdaptor(this, ids_servesWelove, names_servesWelove, subtitles_servesWelove);
        servesWeLoveRecyclerView.setAdapter(servesWeLoveAdaptor);
        RecyclerView.ItemDecoration itemDecoration = new LinearSpacesItemDecoration(50);

    }

}
