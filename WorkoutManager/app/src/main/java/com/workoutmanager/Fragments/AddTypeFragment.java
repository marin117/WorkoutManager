package com.workoutmanager.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.workoutmanager.Adapters.AddTypeAdapter;
import com.workoutmanager.Models.Routine;
import com.workoutmanager.R;
import com.workoutmanager.ViewModel.AddExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddTypeFragment extends Fragment {
     private RecyclerView mRecyclerView;
     private RecyclerView.Adapter mAdapter;
     private RecyclerView.LayoutManager mLayoutManager;
     private ArrayList<String> items;
     private AddExerciseViewModel mViewModel;
     private Routine routine;
     private EditText routineName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_type_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.add_type_recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<String>(3);
        routine = new Routine();
        routineName = view.findViewById(R.id.edit_routine_name);
        items.add("");
        setHasOptionsMenu(true);

        mViewModel = ViewModelProviders.of(getActivity())
                .get(AddExerciseViewModel.class);

        mViewModel.getTypes().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> types) {
                mAdapter = new AddTypeAdapter( getActivity().getApplicationContext(), types, items);
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                if (items.size() <= 2) {
                    items.add("");
                    mAdapter.notifyItemInserted(items.size() - 1);
                }


                else {
                    Toast.makeText(getContext(), "You can't add more types.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.confirm:
                routine.setName(routineName.getText().toString());
                mViewModel.setRoutine(routine);
                mViewModel.setTypes(items);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.type_container ,new AddExerciseFragment());
                fragmentTransaction.commit();
                return true;

            default:
                break;
        }
        return false;
    }
}
