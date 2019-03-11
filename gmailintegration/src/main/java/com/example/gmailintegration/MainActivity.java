package com.example.gmailintegration;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {


    private  final int SIGN_IN_REQUEST = 10;
    private Button button_login;
    private SignInButton signInButton;


    private GoogleSignInOptions googleSignInOptions;
     GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button_login = (Button)findViewById(R.id.button_login);
        signInButton = findViewById(R.id.login_button);
        button_login.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        //Check that user is previouly login or not


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);



        Log.e("GoogleSignInAccount",signInAccount+"/");

        if(signInAccount != null)
        {

            Log.e("GoogleSignInAccount","already login/");

            updateUI(signInAccount);
          //user already login
        }
        else
        {

           createGoogleSignInClient();

        }

    }


    //Mark:-
    private void createGoogleSignInClient()
    {

        //step:- 1
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken("473289362777-pmu4lcie6vtj0nr307sp9m5fq62cenrh.apps.googleusercontent.com")
                .requestProfile()
                .build();



        //Step:- 2
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.button_login:

                break;

            case R.id.login_button:

                 callForLoginUser();
                break;
        }
    }



    //Mark:- login user
    private void callForLoginUser()
    {
       createGoogleSignInClient();

         if(googleSignInClient != null)
         {
             Intent singInIntent = googleSignInClient.getSignInIntent();
             startActivityForResult(singInIntent,SIGN_IN_REQUEST);
         }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult",requestCode + "/"+data);

        if(requestCode == SIGN_IN_REQUEST)
        {

            Task<GoogleSignInAccount> signInAccountTask =  GoogleSignIn.getSignedInAccountFromIntent(data);

            if(signInAccountTask!=null)
            {
                handleTheResult(signInAccountTask);
            }else
            {
            Log.e("taskFail", signInAccountTask + "/fail");
            }




        }
    }


    //Mark:- handle onactivity result
    private void handleTheResult(Task<GoogleSignInAccount> signInAccountTask)
    {


        try
        {

            GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);


            //Mark:- update api
            updateUI(account);

        }catch (ApiException e)
        {
           Log.e("Exception",e.toString());
        }
    }


    //Mark:-
    private void updateUI(GoogleSignInAccount account)
    {


        Intent intent = new Intent(this,DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("account",account);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
