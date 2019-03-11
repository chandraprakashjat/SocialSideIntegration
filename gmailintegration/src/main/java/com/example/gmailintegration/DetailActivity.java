package com.example.gmailintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {


    TextView textView;
    Button button,buttonVerifier;
    String idToken;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.button);
        buttonVerifier = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


               // signOutUser();

                revokeAccess();
            }
        });


        buttonVerifier.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             verifyTheIdToken();
            }
        });

    }



    private void revokeAccess()
    {

        GoogleSignInOptions  googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken("473289362777-pmu4lcie6vtj0nr307sp9m5fq62cenrh.apps.googleusercontent.com")
                .requestProfile()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this,googleSignInOptions);

         Task t =client.revokeAccess();

         t.addOnFailureListener(this, new OnFailureListener()
         {
             @Override
             public void onFailure(@NonNull Exception e)
             {
                 Log.e("onComplete",e.toString());
             }
         });

         t.addOnCompleteListener(this, new OnCompleteListener()
         {
             @Override
             public void onComplete(@NonNull Task task)
             {
             Log.e("onComplete",task.toString());
             finish();
             }
         });


    }


    private void signOutUser()
    {
      GoogleSignInOptions  googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken("473289362777-pmu4lcie6vtj0nr307sp9m5fq62cenrh.apps.googleusercontent.com")
                .requestProfile()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this,googleSignInOptions);

        if(client !=null)
        {



            Task<Void> s = client.signOut();

            s.addOnCompleteListener(this, new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                  Log.e("onComplete",task+"");

                  finish();
                }
            });

            s.addOnFailureListener(this, new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Log.e("onFailure",e.toString()+"");
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null && bundle.containsKey("account"))
        {

            GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount)bundle.getParcelable("account");

            if(googleSignInAccount!=null)
            {
               StringBuffer stringBuffer = new StringBuffer();
               stringBuffer.append( googleSignInAccount.getId()+" / ");
               stringBuffer.append( googleSignInAccount.getIdToken() + "/");




                stringBuffer.append( googleSignInAccount.getDisplayName()+"/");
                stringBuffer.append( googleSignInAccount.getFamilyName() + "/");

                stringBuffer.append( googleSignInAccount.getAccount()+" / ");
                stringBuffer.append( googleSignInAccount.getPhotoUrl()+" / ");
                stringBuffer.append( googleSignInAccount.getEmail()+" / ");

                Log.e("Account Detail",stringBuffer + "");




            }

        }
    }



    //Mark:- how we verify the Id Token
    private void verifyTheIdToken()
    {


        //Step:- we have idToken that we have to verify :- idToken

        //setp2:- we need out client_id :- 473289362777-pmu4lcie6vtj0nr307sp9m5fq62cenrh.apps.googleusercontent.com (from cofigration.json)



        //Step3:- create GoogleIdTokenVerifier

        HttpTransport httpTransport = new ApacheHttpTransport();

        JacksonFactory jsonFactory = new JacksonFactory();

        List client_id_list = new ArrayList();
        client_id_list.add("473289362777-pmu4lcie6vtj0nr307sp9m5fq62cenrh.apps.googleusercontent.com");


        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport,jsonFactory)
                .setAudience(client_id_list)
                .build();


        try {
            GoogleIdToken token = verifier.verify(idToken);

            if (token == null)
            {
               Log.e("NotValid Token","not valid token");
            }else
            {
                Log.e("ValidToken",token+"");
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
