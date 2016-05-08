package com.example.bsaraci.blitzone.Search;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter{

    SearchAdapter adapter;
    ArrayList<SearchModel> filterList;


    public CustomFilter(ArrayList<SearchModel> filterList,SearchAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<SearchModel> filteredSearchModels=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
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

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.players= (ArrayList<SearchModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}