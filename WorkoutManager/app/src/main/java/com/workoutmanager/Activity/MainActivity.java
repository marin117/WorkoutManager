package com.workoutmanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.Fragments.AddTypeFragment;
import com.workoutmanager.Fragments.LoginFragment;
import com.workoutmanager.Fragments.MainFragment;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.GoogleAccount;
import com.workoutmanager.Utils.MenuInterface;
import com.workoutmanager.Utils.SharedPreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MenuInterface {


    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.draw_layout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_compass);
        navigationView = findViewById(R.id.nav_view);
        sharedPreferencesUtil = new SharedPreferencesUtil(this);

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.main_fragment, loginFragment).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments(new AddTypeFragment(), false);
            }
        });

        selectDrawerContent(navigationView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item){
        Fragment fragment = null;
        final Class fragmentClass;

        switch (item.getItemId()){

            case R.id.home:
                changeFragments(new MainFragment(), false);
                break;
            case R.id.personal_workouts:
                changeFragments(new AddTypeFragment(), false);
                break;
            case R.id.logout:
                GoogleAccount account = new GoogleAccount(getApplicationContext());
                account.getClient().signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sharedPreferencesUtil.clearData();
                        changeFragments(new LoginFragment(), true);
                    }
                });
                break;
            default:
                changeFragments(new MainFragment(),true);
                break;
        }

        item.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    private void changeFragments(Fragment fragment, boolean noBackStack){

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (noBackStack ) {
            fragmentManager.beginTransaction().
                    replace(R.id.main_fragment, fragment).
                    commit();

        }else {
            fragmentManager.beginTransaction().
                    replace(R.id.main_fragment, fragment).
                    addToBackStack(null).
                    commit();
        }

    }

    @Override
    public void lockDrawer() {
        getSupportActionBar().hide();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        getSupportActionBar().show();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }
}
