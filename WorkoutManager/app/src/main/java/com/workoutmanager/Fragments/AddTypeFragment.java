package com.workoutmanager.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.workoutmanager.Utils.SharedPreferencesUtil;
import com.workoutmanager.ViewModel.AddExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddTypeFragment extends Fragment {
     private RecyclerView mRecyclerView;
     private RecyclerView.Adapter mAdapter;
     private RecyclerView.LayoutManager mLayoutManager;
     private ArrayList<String> items;
     private AddExerciseViewModel mViewModel;
     private EditText routineName;
     private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity())
                .get(AddExerciseViewModel.class);
        items = new ArrayList<String>(3);
        items.add("");
        init();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_type_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.add_type_recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        routineName = view.findViewById(R.id.edit_routine_name);
        sharedPreferencesUtil = new SharedPreferencesUtil(getActivity());
        setHasOptionsMenu(true);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAAAAAAAAG", String.valueOf(items.size()));
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_element_menu, menu);

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
                Routine routine = new Routine(sharedPreferencesUtil.readData(getString(R.string.id)),
                        routineName.getText().toString(), items);
                mViewModel.setRoutine(routine);
                mViewModel.setTypes(items);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment ,new AddExerciseFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                return true;

            default:
                break;
        }
        return false;
    }
    private void init(){
        mViewModel.getTypes().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> types) {
                mAdapter = new AddTypeAdapter( getActivity().getApplicationContext(), types, items);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

}
