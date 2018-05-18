package com.workoutmanager.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
;

import com.workoutmanager.Fragments.AddTypeFragment;
import com.workoutmanager.R;

public class AddWorkoutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        //final Spinner typeSpinner = findViewById(R.id.type_spinner);

        AddTypeFragment typeFragment = new AddTypeFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.type_container, typeFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_element_menu, menu);
        return true;
    }
}
