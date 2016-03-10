package com.example.bsaraci.blitzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BestTabFragment extends Fragment {

    ListView listView;

    int [] profilePictures = {R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile,
            R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile,
            R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile,R.mipmap.ic_yellow_profile};
    String [] usernames = {"teasaraci","mikelv92","sarasaraci","jonagolemi","ergysmati","enderballa","ilirpirani","sidritdritorja","jv21"};
    String [] points = {"500","117","110","90","57","50","34","31","10"};
    int [] blitz = {R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz,
            R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz,
            R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz,R.mipmap.ic_yellow_blitz};

    ListAdapter listAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_tab_content, container, false);

        listView=(ListView)view.findViewById(R.id.bestList);

        int i=0;
        listAdapter=new ListAdapter(getActivity(),R.layout.blitzone_row_content);
        listView.setAdapter(listAdapter);
        for (String name :usernames) {
            ListDataProvider dataProvider=new ListDataProvider(profilePictures[i],name,points[i],blitz[i]);
            listAdapter.add(dataProvider);
            i++;

        }
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                Toast.makeText(getBaseContext(), position + "is selected", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(view.getContext(), second_class.class);
                startActivity(myIntent);
            }
        });*/

        return view;
    }
}