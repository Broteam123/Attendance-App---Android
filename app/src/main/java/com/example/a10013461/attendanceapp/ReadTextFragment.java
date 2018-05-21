package com.example.a10013461.attendanceapp;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ReadTextFragment extends Fragment {

    ListView listView;

    ClassElement classElement;
    ArrayList<String> list=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    sendItPls sendItPls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_read_text_fragment,container,false);

        classElement = getArguments().getParcelable("classList");
        listView = (ListView) fragmentView.findViewById(R.id.listOfNames);
        list = classElement.getPeople();
        classElement.setPeople(list);
        sendItPls.send(classElement);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.editname_dialog,null,false);
                final EditText editName = (EditText) v.findViewById(R.id.editName);
                editName.setText(list.get(position));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.set(position,editName.getText().toString());
                        classElement.setPeople(list);
                        sendItPls.send(classElement);
                        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setView(v);
                dialog.show();
            }
        });
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendItPls = (sendItPls) context;
    }

    public interface sendItPls{
        public void send(ClassElement classElement);
    }

}
