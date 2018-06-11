package com.workoutmanager.Adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workoutmanager.Fragments.UserFragment;
import com.workoutmanager.Models.User;
import com.workoutmanager.R;
import com.workoutmanager.ViewModel.MainViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.workoutmanager.Utils.PictureUtils.loadPicture;

public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.UserHolder> {

    private List<User> users;
    private MainViewModel mainViewModel;

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profilePicture;
        TextView username, stars;
        UserHolder(View view){
            super(view);
            profilePicture = view.findViewById(R.id.find_user_image);
            username = view.findViewById(R.id.find_user_username);
            stars = view.findViewById(R.id.find_user_star);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mainViewModel.setUserId(users.get(getAdapterPosition()).getId());
            FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    addToBackStack(null).
                    replace(R.id.main_fragment,new UserFragment()).commit();
        }
    }

    public FindUserAdapter(List<User> users, MainViewModel mainViewModel){
        this.users = users;
        this.mainViewModel = mainViewModel;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = users.get(position);
        loadPicture(user.getPicture(), 200, 200, holder.profilePicture);
        holder.username.setText(user.getUsername());
        holder.stars.setText(user.getStars().toString());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


}
