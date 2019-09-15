package com.example.garbagecollection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class UserActivities extends Fragment {

RecyclerView garbageRecycleView;
List<Garbage>garbageList;
GarbageActivityAdapter garbageActivityAdapter;
View garbageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Garbage e=new Garbage("test","test","test",100);
        garbageView=inflater.inflate(R.layout.fragment_user_activities, container, false);
        garbageRecycleView=garbageView.findViewById(R.id.garbageRecycleView);
        garbageList=new ArrayList<>();

        garbageList.add(e);
        garbageList.add(e);
        garbageActivityAdapter=new GarbageActivityAdapter(garbageList,getActivity());
        garbageRecycleView.setAdapter(garbageActivityAdapter);
        garbageRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return garbageView;
    }


}
