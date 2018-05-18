package com.workoutmanager.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.workoutmanager.Adapters.ExerciseAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Exercise;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Exercise> exerciseListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        Intent intent = getIntent();
        final Context context = getApplicationContext();
        final int id = intent.getIntExtra(context.getString(R.string.routine_id), 0);
        final String name = intent.getStringExtra(context.getString(R.string.exercise_name));
        final String owner = intent.getStringExtra(context.getString(R.string.owner));
        mRecyclerView = findViewById(R.id.detail_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        RetrofitClient retrofit = new RetrofitClient();
        Call<Routine> call = retrofit.createClient().routineDetails(id);

        call.enqueue(new Callback<Routine>() {
            @Override
            public void onResponse(Call<Routine> call, Response<Routine> response){
                TextView text_name = findViewById(R.id.detail_name);
                TextView text_stars = findViewById(R.id.detail_stars);
                TextView text_owner = findViewById(R.id.detail_user);
                text_name.setText(context.getString(R.string.detail_name, response.body().getName()));
                text_owner.setText(context.getString(R.string.detail_owner, owner));
                mAdapter = new ExerciseAdapter(response.body().getExercise());
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<Routine> call, Throwable t){
                Toast.makeText(getApplicationContext(),
                        "Failure",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", t.getMessage());

            }
        });



    }
}
