package com.example.garbagecollection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GarbageActivityAdapter extends RecyclerView.Adapter<GarbageActivityAdapter.GarbageViewHolder> {
    List<Garbage> garbageList;
    Context garbageListContext;

    public GarbageActivityAdapter(List<Garbage> garbageList, Context garbageListContext) {
        this.garbageList = garbageList;
        this.garbageListContext = garbageListContext;
    }

    @NonNull
    @Override
    public GarbageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View layout;
      layout= LayoutInflater.from(garbageListContext).inflate(R.layout.garbage_list_layout,parent,false);
      return new GarbageViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull GarbageViewHolder holder, int position) {
    holder.title.setText(garbageList.get(position).getTitle());
    holder.status.setText(garbageList.get(position).getStatus());
    holder.location.setText(garbageList.get(position).getLocation());
//    holder.img.setImageResource(garbageList.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return garbageList.size();
    }

    public class GarbageViewHolder extends RecyclerView.ViewHolder{
        TextView title,status,location;
        ImageView img;
        public GarbageViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            status=itemView.findViewById(R.id.status);
            location=itemView.findViewById(R.id.location);
            img=itemView.findViewById(R.id.img);
        }
    }
}
