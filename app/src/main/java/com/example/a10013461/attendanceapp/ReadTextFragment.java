package com.example.a10013461.attendanceapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ReadTextFragment extends Fragment {

    ListView listView;

    ClassElement classElement = getArguments().getParcelable("classList");
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_read_text_fragment,container,false);

        listView = (ListView) fragmentView.findViewById(R.id.listOfNames);
        list = classElement.getPeople();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        adapter.notifyDataSetChanged();


        return fragmentView;
    }

}
