package com.workoutmanager.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.workoutmanager.Fragments.FindUserFragment;
import com.workoutmanager.Fragments.LikedWorkoutsFragment;
import com.workoutmanager.Fragments.LoginFragment;
import com.workoutmanager.Fragments.MainFragment;
import com.workoutmanager.Fragments.RoutineDetailFragment;
import com.workoutmanager.Fragments.StarFragment;
import com.workoutmanager.Fragments.UserFragment;
import com.workoutmanager.R;
import com.workoutmanager.Utils.GoogleAccount;
import com.workoutmanager.Utils.MenuInterface;
import com.workoutmanager.Utils.SharedPreferencesUtil;
import com.workoutmanager.ViewModel.MainViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import me.pushy.sdk.Pushy;

import static com.workoutmanager.Utils.PictureUtils.loadPicture;

public class MainActivity extends AppCompatActivity implements MenuInterface {


    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private MainViewModel mainViewModel;
    String CHANNEL_ID = "1987";

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
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        createNotificationChannel();
        Pushy.listen(this);

        selectDrawerContent(navigationView);

        changeFragments(new LoginFragment(), true);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        switch (item.getItemId()){

            case R.id.home:
                changeFragments(new MainFragment(), false);
                break;
            case R.id.find_users:
                changeFragments(new FindUserFragment(), false);
                break;
            case R.id.liked_workouts:
                changeFragments(new LikedWorkoutsFragment(), false);
                break;
            case R.id.star_users:
                changeFragments(new StarFragment(), false);
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
    public void setHeader() {
        View headerLayout = navigationView.getHeaderView(0);
        CircleImageView profilePicture = headerLayout.findViewById(R.id.picture_nav);
        String picture = sharedPreferencesUtil.readData(getString(R.string.picture));
        Log.d("TAAAAAAG", picture);
        if (picture != null || !picture.isEmpty())
            loadPicture(picture, 300,300, profilePicture);
        TextView username = headerLayout.findViewById(R.id.username_header);
        username.setText(sharedPreferencesUtil.readData(getString(R.string.username)));

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setUserId(sharedPreferencesUtil.readData(getString(R.string.id)));
                changeFragments(new UserFragment(), false);
            }
        });
    }

    @Override
    public void lockDrawer() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void unlockDrawer() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


}
