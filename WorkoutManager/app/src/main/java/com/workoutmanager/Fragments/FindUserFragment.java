package com.workoutmanager.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.workoutmanager.Adapters.FindUserAdapter;
import com.workoutmanager.HttpClient.RetrofitClient;
import com.workoutmanager.Models.User;
import com.workoutmanager.R;
import com.workoutmanager.Utils.DataHandler;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindUserFragment extends Fragment implements DataHandler {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String filter = null;
    private RetrofitClient retrofit;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainViewModel = ViewModelProviders.of(getActivity()).
                get(MainViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_user_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.find_user_recycler);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        retrofit = new RetrofitClient();
        getData();

        return view;
    }

    @Override
    public void getData() {
        Call<List<User>> users = retrofit.createClient().getPersons(filter);
        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call,@NonNull Response<List<User>> response) {
                mAdapter = new FindUserAdapter(response.body(), mainViewModel);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "No results", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter = query;
                if (query.equals(""))
                    filter = null;
                getData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filter = null;
                getData();
                return false;
            }
        });
    }
}
