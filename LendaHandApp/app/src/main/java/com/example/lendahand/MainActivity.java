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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Database database;

    RecyclerView featuredServiceOpsRecyclerView;
    String titles_featured[];
    String subtitles_featured[];
    int images_featured[] = {R.drawable.build_day_image, R.drawable.pancake_image, R.drawable.kids_image};


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

        //Top Bar with Logo
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        //Serves We Love
        RecyclerView servesWeLove = findViewById(R.id.serves_we_love_recycler_view);
        ArrayList<String> ids_servesWelove = new ArrayList<>();
        ArrayList<String> names_servesWelove = new ArrayList<>();
        ArrayList<String> subtitles_servesWelove = new ArrayList<>();
        getServesFromDatabase("servesWeLove", servesWeLove, ids_servesWelove, names_servesWelove, subtitles_servesWelove);

        //Help Your Community
        RecyclerView helpYourCommunity = findViewById(R.id.help_your_community_recycler_view);
        ArrayList<String> ids_helpYourCommunity = new ArrayList<>();
        ArrayList<String> names_helpYourCommunity = new ArrayList<>();
        ArrayList<String> subtitles_helpYourCommunity = new ArrayList<>();
        getServesFromDatabase("helpCommunity", helpYourCommunity, ids_helpYourCommunity, names_helpYourCommunity, subtitles_helpYourCommunity);

        //Active Organizations
        RecyclerView activeOrganizations = findViewById(R.id.ActiveOrganizations_recycler_view);
        ArrayList<String> ids_activeOrganizations = new ArrayList<>();
        ArrayList<String> names_activeOrganizations = new ArrayList<>();
        getOrgsFromDatabase("activeOrgs", activeOrganizations, ids_activeOrganizations, names_activeOrganizations);

        //New to Lendahand
        RecyclerView newToLendahand = findViewById(R.id.NewToLendahand_recycler_view);
        ArrayList<String> ids_newToLendahand = new ArrayList<>();
        ArrayList<String> names_newToLendahand = new ArrayList<>();
        ArrayList<String> subtitles_newToLendahand = new ArrayList<>();
        getServesFromDatabase("newToLendahand", newToLendahand, ids_newToLendahand, names_newToLendahand, subtitles_newToLendahand);

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
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"Signed In",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"Not Signed In",Toast.LENGTH_LONG).show();
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

                if (currentUser != null) {
                    Intent volunteerPage = new Intent(MainActivity.this, VolunteerPage.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ID", currentUser.getEmail());

                    Log.d("main", "User email: " + currentUser.getEmail());

                    volunteerPage.putExtras(bundle);

                    startActivity(volunteerPage);


                } else {
                    Intent LoginScreen = new Intent(MainActivity.this, Login.class);
                    startActivity(LoginScreen);
                }

                return true;

            default:
                return this.onOptionsItemSelected(item);

        }
    }

    private void getOrgsFromDatabase(String path, final RecyclerView recyclerView, final ArrayList<String> ids, final ArrayList<String> names) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Regular", document.getId() + " => " + document.getData());


                                ids.add(document.getString("id"));
                                names.add(document.getString("name"));

                            }
                            initOrgRecyclerView(recyclerView, ids, names);
                        }
                    }
                });
    }

    private void getServesFromDatabase(String path, final RecyclerView recyclerView, final ArrayList<String> ids, final ArrayList<String> names, final ArrayList<String> subtitles ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Regular", document.getId() + " => " + document.getData());


                                ids.add(document.getString("id"));
                                names.add(document.getString("name"));
                                subtitles.add(document.getString("subtitles"));

                            }
                            initServeRecyclerView(recyclerView, ids, names, subtitles);
                        }
                    }
                });
    }

    private void initOrgRecyclerView(RecyclerView recyclerView, ArrayList<String> ids, ArrayList<String> names) {
        HomescreenServiceOrgAdaptor adaptor = new HomescreenServiceOrgAdaptor(this, ids, names);
        recyclerView.setAdapter(adaptor);
    }

    private void initServeRecyclerView(RecyclerView recyclerView, ArrayList<String> ids, ArrayList<String> names, ArrayList<String> subtitles) {
        HomescreenServiceOpAdaptor adaptor = new HomescreenServiceOpAdaptor(this, ids, names, subtitles);
        recyclerView.setAdapter(adaptor);
    }
}
