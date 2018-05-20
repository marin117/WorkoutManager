package com.workoutmanager.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        swipeRefresh = view.findViewById(R.id.swipe_main);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();

        return view;
    }

    private void getData() {
        RetrofitClient retrofit = new RetrofitClient();
        Call<List<Workout>> call = retrofit.createClient().workoutList();

        call.enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {

                mAdapter = new WorkoutAdapter(response.body(), mainViewModel);
                mRecyclerView.setAdapter(mAdapter);
                if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                //Toast.makeText(getApplicationContext(), "Failure",
                //       Toast.LENGTH_SHORT).show();

            }
        });
    }
}