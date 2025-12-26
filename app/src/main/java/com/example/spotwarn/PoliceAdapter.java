package com.example.spotwarn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PoliceAdapter extends RecyclerView.Adapter<PoliceAdapter.PoliceViewHolder> {

    private List<PoliceModel> policeList;

    public PoliceAdapter(List<PoliceModel> policeList) {
        this.policeList = policeList;
    }

    @NonNull
    @Override
    public PoliceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_police_info, parent, false);
        return new PoliceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoliceViewHolder holder, int position) {
        PoliceModel p = policeList.get(position);
        holder.name.setText("Name: " + p.getName());
        holder.designation.setText("Designation: " + p.getDesignation());
        holder.email.setText("Email: " + p.getEmail());
        holder.phone.setText("Phone: " + p.getPhone());
    }

    @Override
    public int getItemCount() {
        return policeList.size();
    }

    static class PoliceViewHolder extends RecyclerView.ViewHolder {
        TextView name, designation, email, phone;

        public PoliceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            designation = itemView.findViewById(R.id.textDesignation);
            email = itemView.findViewById(R.id.textEmail);
            phone = itemView.findViewById(R.id.textPhone);
        }
    }
}