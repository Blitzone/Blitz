package com.example.bsaraci.blitzone.Options;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bsaraci.blitzone.R;

/**
 * Created by bsaraci on 5/6/2016.
 */
public class FragmentChangeUsername extends Fragment {
    String toolbarTitle;
    public static FragmentChangeUsername newInstance(String toolbarTitle){
       FragmentChangeUsername fragmentChangeUsername = new FragmentChangeUsername();
        Bundle args = new Bundle();
        args.putString("toolbarTitle", toolbarTitle);
        fragmentChangeUsername.setArguments(args);
        return fragmentChangeUsername;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle = getArguments().getString("toolbarTitle", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_username, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView toolbarTextView = (TextView)view.findViewById(R.id.fragment_username_change_toolbar_title);
        toolbarTextView.setText(toolbarTitle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
