package com.workoutmanager.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.IdModel;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWorkoutsFragment extends Fragment implements DataHandler {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainViewModel mainViewModel;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_workouts, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_workouts);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        userId = new IdModel(getActivity()).getId();

        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        final RetrofitClient retrofitClient = new RetrofitClient();

        Call<List<Workout>> myWorkouts = retrofitClient.createClient().myWorkoutList(userId);

        myWorkouts.enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(@NonNull Call<List<Workout>> call,@NonNull Response<List<Workout>> response) {
                mAdapter = new WorkoutAdapter(response.body(), mainViewModel);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(@NonNull Call<List<Workout>> call,@NonNull Throwable t) {

            }
        });
    }
}
