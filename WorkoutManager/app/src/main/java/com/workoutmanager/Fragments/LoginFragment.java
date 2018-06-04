package com.workoutmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.GoogleToken;
import com.workoutmanager.R;
import com.workoutmanager.Utils.GoogleAccount;
import com.workoutmanager.Utils.MenuInterface;
import com.workoutmanager.Utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private MenuInterface menuInterface;
    private int RC_SIGN_IN = 2311;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            menuInterface = (MenuInterface) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement interface.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        super.onCreate(savedInstanceState);


        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        menuInterface.lockDrawer();
        signInButton.setOnClickListener(this);
        mGoogleSignInClient = new GoogleAccount(getContext()).getClient();


        GoogleAccount googleAccount = new GoogleAccount(getContext());
        sharedPreferencesUtil = new SharedPreferencesUtil(getActivity());
        mGoogleSignInClient = googleAccount.getClient();

        return view;
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

        }

    }


    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            sendToken(idToken);

            Log.d("TAAAAAAAG", idToken);

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAAAAAAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            mainFragmentChange();
        }
    }

    private void mainFragmentChange(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, new MainFragment());
        fragmentTransaction.commit();
    }

    private void sendToken(String tokenID) {
        RetrofitClient retrofit = new RetrofitClient();
        GoogleToken token = new GoogleToken(tokenID);
        Call<String> call = retrofit.createClient().sendToken(token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                Log.d("TAAAAAAG", response.body());
                sharedPreferencesUtil.writeData(getString(R.string.id), response.body());
                mainFragmentChange();
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                mGoogleSignInClient.signOut();
                Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
