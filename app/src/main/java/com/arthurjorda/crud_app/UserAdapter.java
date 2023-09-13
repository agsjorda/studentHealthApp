package com.arthurjorda.crud_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<User> arrayList;
    ArrayList<String> userDetailStrings;
    OnItemClickListener onItemClickListener;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public UserAdapter(MainActivity context, ArrayList<String> userDetailStrings) {
        this.context = context;
        this.userDetailStrings = userDetailStrings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = arrayList.get(position);

        // Format user details
        String name = user.getFirstName() + " " + user.getLastName();
        String phone = "phone: " + user.getPhone();
        String bio = "bio: " + user.getBio();

        // Set the formatted details in TextViews
        holder.name.setText(name);
        holder.phone.setText(phone);
        holder.bio.setText(bio);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(user));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, bio, phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            bio = itemView.findViewById(R.id.list_item_bio);
            phone = itemView.findViewById(R.id.list_item_phone);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(User user);


    }
}
