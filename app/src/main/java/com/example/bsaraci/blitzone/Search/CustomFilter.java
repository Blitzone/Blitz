package com.example.bsaraci.blitzone.Search;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter{

    SearchAdapter adapter;              //A SearchAdapter
    ArrayList<SearchModel> filterList;  //The filtered ArrayList

    //CONSTRUCTOR WITH AN ArrayList AND A SearchAdapter
    public CustomFilter(ArrayList<SearchModel> filterList, SearchAdapter adapter) {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //METHOD THAT MAKES THE FILTERING POSSIBLE
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED USERS
            ArrayList<SearchModel> filteredSearchModels=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getUser().getUsername().toUpperCase().contains(constraint))
                {
                    //ADD USER TO FILTERED USERS
                    filteredSearchModels.add(filterList.get(i));
                }
            }

            results.count=filteredSearchModels.size();
            results.values=filteredSearchModels;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    //METHOD THAT CHANGES THE LIST BY THE FILTERED ONE
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.users= (ArrayList<SearchModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}