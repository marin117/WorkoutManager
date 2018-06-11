package com.workoutmanager.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.workoutmanager.Adapters.FindUserAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.Models.User;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineLikeFragment extends Fragment implements DataHandler {
    private MainViewModel mainViewModel;
    private RetrofitClient retrofit;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.star_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.star_recylcer);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        retrofit = new RetrofitClient();

        getData();
        return view;

    }

    @Override
    public void getData() {
        mainViewModel.getRoutineId().observe(getActivity(), new Observer<Routine>() {
            @Override
            public void onChanged(@Nullable Routine routineId) {
                Call<List<User>> starsList = retrofit.createClient().likeRoutineUserList(routineId.getId());
                starsList.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        mAdapter = new FindUserAdapter(response.body(), mainViewModel);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call,@NonNull Throwable t) {

                    }
                });

            }
        });


    }
}
