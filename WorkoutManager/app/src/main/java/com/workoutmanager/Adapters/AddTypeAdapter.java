package com.workoutmanager.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.workoutmanager.R;

import java.util.ArrayList;
import java.util.List;

public class AddTypeAdapter extends RecyclerView.Adapter<AddTypeAdapter.TypeHolder>{

    private List<String> types;
    private Context context;
    private ArrayList<String> items;
    private Button addButton;

    public class TypeHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Spinner spinner;
        Button delete;

        TypeHolder(View view) {
            super(view);
            spinner = view.findViewById(R.id.spinner);
            delete = view.findViewById(R.id.delete_type);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    items.set(getAdapterPosition(), parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAAAAG POSITION", String.valueOf(getAdapterPosition()));
                    items.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });


        }
    }

    public AddTypeAdapter(Context context, List<String> types,
                          ArrayList<String> items){
        this.types = types;
        this.context = context;
        this.items = items;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_type_item, parent, false);

        return new AddTypeAdapter.TypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TypeHolder holder, final int position) {
        ArrayAdapter<String> typeAdapter;
        typeAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_dropdown_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(typeAdapter);
        holder.spinner.setSelection(typeAdapter.getPosition(items.get(position)));
        if (position == 0){
            holder.delete.setVisibility(View.GONE);
        }
        else holder.delete.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
