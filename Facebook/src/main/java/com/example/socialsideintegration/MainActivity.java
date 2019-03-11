package com.example.socialsideintegration;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private LoginButton loginButton;

    private Button buttonLogout,buttonLoginCustomButton, buttonGetDetail,runTimePermissionButton;

    private ArrayList<String> fb_permission ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGetDetail = findViewById(R.id.button4);
        buttonLogout = findViewById(R.id.button2);
        buttonLoginCustomButton = findViewById(R.id.button3);
        runTimePermissionButton = findViewById(R.id.button5);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        createPermission();

        getUserLoginStatus();



        //Mark:- call back manager
        callbackManager = CallbackManager.Factory.create();

        loginManager = LoginManager.getInstance();






        //OnCLick
        buttonLoginCustomButton.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        buttonGetDetail.setOnClickListener(this);
        runTimePermissionButton.setOnClickListener(this);


        //Register callback

        registerCallBack();



        addPermissionForFaceBookLoginButton();
    }

    private void addPermissionForFaceBookLoginButton()
    {
        loginButton.setReadPermissions(fb_permission);

    }

    private void registerCallBack()
    {

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.e("Detail",loginResult.toString()+"");

                Log.e("Token", loginResult.getAccessToken().getToken()+"");

                Log.e("Permission", loginResult.getRecentlyDeniedPermissions()+"");

                Log.e("Denied", loginResult.getRecentlyGrantedPermissions()+"");

                GetUserDetail(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {


                Log.e("OnCancel","candel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("onError",error.toString() + "");
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.e("Detail",loginResult.toString()+"");

                Log.e("Token", loginResult.getAccessToken().getToken()+"");

                Log.e("Denied", loginResult.getRecentlyDeniedPermissions()+"");

                Log.e("Permission", loginResult.getRecentlyGrantedPermissions()+"");



                GetUserDetail(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {


                Log.e("OnCancel","candel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("onError",error.toString() + "");
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        Log.e("Detail",requestCode + "/ "+ resultCode + "/"+ data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Create Permission:-


    private void createPermission()
    {


        fb_permission = new ArrayList<>();
        fb_permission.add("user_birthday");
       fb_permission.add("user_events");
       fb_permission.add("user_age_range");
       fb_permission.add("groups_access_member_info");
//       fb_permission.add("user_friends");
//
//        fb_permission.add("user_likes");
//        fb_permission.add("user_hometown");
//        fb_permission.add("user_gender");
//        fb_permission.add("user_link");
//        fb_permission.add("user_photos");
//        fb_permission.add("user_tagged_places");
//        fb_permission.add("user_videos");

    }

    private void createReadPermission()
    {

        fb_permission.add("publish_to_groups");

    }

    private boolean getUserLoginStatus()
    {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();


        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        Log.e("IsLogin",isLoggedIn + " isLogin");

        return isLoggedIn;
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.button3:  //buttonLoginCustomButton

                   loginManager.logInWithReadPermissions(this,fb_permission);





                  break;


            case R.id.button4: //buttonGetDetail

            break;



            case R.id.button2:  //buttonLogout

                if(getUserLoginStatus())
                {
                    LoginManager.getInstance().logOut();
                }else
                {
                    Toast.makeText(this,"User not login",Toast.LENGTH_SHORT).show();
                }



                break;


            case R.id.button5:

                getRunTimePermission();

                break;

        }

    }




    //Mark:- get Detail
    private void GetUserDetail(AccessToken token)
    {


        //Mark:- require data

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,email,picture,first_name,last_name,birthday,events,age_range," +
        "friends,likes,hometown,gender,link,photos,tagged_places,videos");

        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {


                Log.e("Response",response.toString()+"");
            }
        });


        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();




    }



    //Mark:- run time permission
    public void getRunTimePermission()
    {


        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        loginManager.logInWithReadPermissions(this,accessToken.getDeclinedPermissions());
        Log.e("DeniedPermission",accessToken.getDeclinedPermissions()+"");




    }

}
