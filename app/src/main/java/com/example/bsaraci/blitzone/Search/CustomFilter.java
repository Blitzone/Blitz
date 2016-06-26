package com.example.bsaraci.blitzone.Search;

/**This class makes the filtering of our search user list possible. It appears in SearchAdapter.
***********************************************************************************************
* BUGS : WE DON'T HAVE ANY BUGS FOR THE MOMENT
***********************************************************************************************
* AMELIORATION : NO AMELIORATION FOR THE MOMENT */

import android.widget.Filter;
import java.util.ArrayList;

public class CustomFilter extends Filter {

    SearchAdapter adapter;              //A SearchAdapter
    ArrayList<SearchModel> filterList;  //The filtered ArrayList

/**
    CONSTRUCTOR OF THE CUSTOM FILTER
    @param filterList, the filtered list
    @param adapter,the search adapter
*/
    public CustomFilter(ArrayList<SearchModel> filterList, SearchAdapter adapter) {
        this.adapter=adapter;
        this.filterList=filterList;

    }

/**
    METHOD THAT MAKES THE FILTERING POSSIBLE
    @param constraint, the letters we type
    @return the filtered result
*/
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //Check constraint validity
        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();  //Change to upper
            ArrayList<SearchModel> filteredSearchModels=new ArrayList<>(); //Store our filtered results

            for (int i=0;i<filterList.size();i++)
            {
                //Check
                if(filterList.get(i).getUser().getUsername().toUpperCase().contains(constraint))
                {
                    filteredSearchModels.add(filterList.get(i)); //Adds user to filtered results
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

/**
    METHOD THAT CHANGES THE LIST BY THE FILTERED ONE
    @param constraint, the letters we type
    @param results, the filtered result
*/
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        //noinspection unchecked
        adapter.users= (ArrayList<SearchModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}