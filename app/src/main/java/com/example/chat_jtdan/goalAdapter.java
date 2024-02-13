package com.example.chat_jtdan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_jtdan.MainActivity;
import com.example.chat_jtdan.goalModel;


import java.util.List;
public class goalAdapter extends RecyclerView.Adapter<goalAdapter.MyViewHolder> {

    private List<goalModel> goals;
    private MainActivity activity;

    public goalAdapter(MainActivity activity){
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_layout , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final goalModel item = goals.get(position);
        holder.mCheckBox.setText(item.getGoal());
        holder.mCheckBox.setChecked(item.getStatus());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    ;
                } else
                    ;

            }
        });

    }
    public Context getContext(){
        return activity;
    }

    public void editItem(int position){
        goalModel item = goals.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("goal", item.getGoal());

        AddNewGoal goal = new AddNewGoal();
        goal.setArguments(bundle);
        goal.show(activity.getSupportFragmentManager(), goal.getTag());



    }

    public void setGoals (List<goalModel> goals){
        this.goals =goals;
        notifyDataSetChanged();

    }

    public void deleteGoals (int position ){
        goalModel item = goals.get(position);
        goals.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
