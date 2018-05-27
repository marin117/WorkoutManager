package com.workoutmanager.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.workoutmanager.Adapters.WorkoutAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Workout;
import com.workoutmanager.R;
import com.workoutmanager.Utils.MenuInterface;
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
    private MenuInterface menuInterface;

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
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        menuInterface.unlockDrawer();

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
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
