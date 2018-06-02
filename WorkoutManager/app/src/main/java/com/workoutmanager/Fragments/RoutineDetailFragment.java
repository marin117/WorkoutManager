package com.workoutmanager.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.workoutmanager.Activity.MainActivity;
import com.workoutmanager.Adapters.ExerciseAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.AddRoutineModel;
import com.workoutmanager.Models.Exercise;
import com.workoutmanager.Models.IdModel;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.Utils.SharedPreferencesUtil;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineDetailFragment extends Fragment implements DataHandler{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView text_owner;
    private TextView text_name;
    private TextView text_stars;
    private MainViewModel mainViewModel;
    private MenuItem addItem;
    private String userId;
    private RetrofitClient retrofit;
    private Routine currentRoutine;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.routine_detail, container, false);

        mRecyclerView = view.findViewById(R.id.detail_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        text_owner = view.findViewById(R.id.detail_user);
        text_name = view.findViewById(R.id.detail_name);
        text_stars = view.findViewById(R.id.detail_stars);
        addItem = view.findViewById(R.id.new_routine);

        retrofit = new RetrofitClient();
        userId = new IdModel(getActivity()).getId();


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);

        getData();

    }

    @Override
    public void getData(){
        mainViewModel.getRoutineID().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {

                final String owner = workout.getOwner();
                Call<Routine> call = retrofit.createClient().routineDetails(workout.getRoutineId(),
                        userId);
                call.enqueue(new Callback<Routine>() {
                    @Override
                    public void onResponse(@NonNull Call<Routine> call, @NonNull Response<Routine> response){
                        text_name.setText(response.body().getName());
                        text_owner.setText(getString(R.string.detail_owner, owner));
                        mAdapter = new ExerciseAdapter(response.body().getExercise());
                        mRecyclerView.setAdapter(mAdapter);
                        if (!response.body().getIsmy()){
                            addItem.setVisible(true);
                        }
                        currentRoutine = response.body();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Routine> call, @NonNull Throwable t){
                        Toast.makeText(getContext(),
                                "Failure",
                                Toast.LENGTH_SHORT).show();
                        Log.e("TAG", t.getMessage());

                    }
                });

            }
        });
    }

    public void sendRoutine(){
        AddRoutineModel addRoutineModel = new AddRoutineModel(currentRoutine);
        addRoutineModel.getRoutine().setUserId(userId);
        Call<String> call = retrofit.createClient().addWorkout(addRoutineModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull  Call<String> call, @NonNull Response<String> response) {
                Toast.makeText(getContext(), "Workout added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

            }
        });
        addItem.setVisible(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        addItem = menu.findItem(R.id.new_routine);
        addItem.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_routine:
                sendRoutine();

        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_to_my_routine_menu, menu);
    }
}
