package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final RecycleViewInterface recycleViewInterface;
    ArrayList<Route> routeList;
    Context context;

    public MyAdapter(ArrayList<Route> routeList, Context context, RecycleViewInterface recycleViewInterface) {
        this.routeList = routeList;
        this.context = context;
        this.recycleViewInterface = recycleViewInterface;
    }

    public ArrayList<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.routeentry, parent, false);

        return new MyViewHolder(v,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Route route = routeList.get(position);

        holder.name.setText(route.getName());
        holder.start_date.setText(route.realStartDate());
        holder.duration.setText(route.getRealDuration(route.getDuration()));

    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView name, duration, start_date;


        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name);
            start_date = itemView.findViewById(R.id.trip_date);
            duration = itemView.findViewById(R.id.trip_duration);

            itemView.setOnClickListener(v -> {
                if(recycleViewInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        recycleViewInterface.onItemClick(position);
                    }
                }
            });

        }
    }
}
