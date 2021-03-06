package com.example.lendahand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.util.StringUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Volunteer_Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer__signup);

        //Create Outlets for all buttons and text fields
        MaterialButton startServingButton = findViewById(R.id.volunteerSignupNext);
        final TextInputEditText textVolunteerFirstName = findViewById(R.id.volunteerFirstNameText);
        final TextInputEditText textVolunteeerLastName = findViewById(R.id.volunteerLastNameText);
        final TextInputEditText textVolunteerEmail = findViewById(R.id.volunteerEmailText);
        final TextInputEditText textVolunteerPhone = findViewById(R.id.volunteerPhoneText);
        final TextInputEditText textVolunteerDateofBirth = findViewById(R.id.volunteerDateOfBirthText);
        final TextInputEditText textVolunteerPassword = findViewById(R.id.volunteerPasswordText);


        startServingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieve the values from each text Field
                String firstName = textVolunteerFirstName.getText().toString().trim();
                String lastName = textVolunteeerLastName.getText().toString().trim();
                String email = textVolunteerEmail.getText().toString().trim();
                String phone = textVolunteerPhone.getText().toString().trim();
                String dateOfBirth = textVolunteerDateofBirth.getText().toString().trim();
                String password = textVolunteerPassword.getText().toString().trim();

                //Check each text field for input errors and save the errors in a string
                InputChecker inputChecker = new InputChecker();
                String signupError= "";
                signupError += inputChecker.isBlank(firstName, "First Name");
                signupError += inputChecker.isBlank(lastName, "Last Name");
                signupError += inputChecker.isBlank(phone, "Phone");
                signupError += inputChecker.isEmailValid(email);
                signupError += inputChecker.isBlank(dateOfBirth, "Date Of Birth");
                signupError += inputChecker.isPasswordValid(password);

                if (StringUtils.isBlank(signupError)) {
                    //If no errors, create a new volunteer
                    Volunteer newVolunteer = new Volunteer(firstName,lastName,email,phone,dateOfBirth,password);
                    newVolunteer.displayVolunteer();

                    //Save the volunteer on disk
                    Intent tagsScreen = new Intent(v.getContext(), AddTagsForVolunteer.class);
                    Bundle volunteerBundle = new Bundle();
                    volunteerBundle.putSerializable("CurrentVolunteer", newVolunteer);
                    tagsScreen.putExtras(volunteerBundle);

                    //And go to the tags screen
                    startActivity(tagsScreen);

                } else {
                    Toast.makeText(v.getContext(), signupError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
