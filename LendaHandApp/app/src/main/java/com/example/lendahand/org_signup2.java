package com.example.lendahand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.util.StringUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class org_signup2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_signup2);

        Intent intent = getIntent();
        final ServiceOrganization newOrg = (ServiceOrganization)intent.getSerializableExtra("ServiceOrg");

        final TextInputEditText txtOrgDesc = (TextInputEditText)findViewById(R.id.orgDescText);
        final MaterialButton btnOrgSignUpNext2 = (MaterialButton) findViewById(R.id.orgSignupNext2);


        btnOrgSignUpNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgDesc = txtOrgDesc.getText().toString();
                //Do input checking
                InputChecker inputChecker = new InputChecker();
                String error = "";
                error+= inputChecker.isBlank(orgDesc, "Organization Description");

                if(StringUtils.isBlank(error)){
                    newOrg.setOrgDescription(orgDesc);
                    newOrg.displayServiceOrg();

                    Intent nextScreen = new Intent(v.getContext(),  org_signup3.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ServiceOrg", newOrg);
                    nextScreen.putExtras(bundle);
                    startActivityForResult(nextScreen, 0);
                }
                else{
                    Toast.makeText(v.getContext(), error, 10).show();
                }
            }
        });

    }
}
