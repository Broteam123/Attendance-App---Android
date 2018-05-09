package com.example.a10013461.attendanceapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ReadTextFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_read_text_fragment,container,false);

        listView = (ListView) fragmentView.findViewById(R.id.listOfNames);

        return fragmentView;
    }

}
