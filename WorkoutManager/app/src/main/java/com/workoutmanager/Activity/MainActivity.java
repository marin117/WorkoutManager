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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.Fragments.AddTypeFragment;
import com.workoutmanager.Fragments.MainFragment;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;

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
        NavigationView navigationView = findViewById(R.id.nav_view);



        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.main_fragment, mainFragment).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddWorkoutActivity.class));
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
        Class fragmentClass;

        switch (item.getItemId()){

            case R.id.home:
                fragmentClass = MainFragment.class;
                break;
            case R.id.personal_workouts:
                fragmentClass = AddTypeFragment.class;
                break;

            case R.id.login:
                Intent login = new Intent(this,LoginActivity.class);
                startActivity(login);

            default:
                fragmentClass = MainFragment.class;
                break;
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_fragment, fragment).addToBackStack(null).commit();

        item.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

}
