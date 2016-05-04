package com.example.bsaraci.blitzone.Options;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bsaraci.blitzone.Blitzone.DividerItemDecoration;
import com.example.bsaraci.blitzone.R;

import java.util.ArrayList;
import java.util.List;

public class Options extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private String [] data = {"1","2","3"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //Your RecyclerView.Adapter
        mAdapter = new SimpleAdapter(this,data);


        //This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5,"Section 2"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(12,"Section 3"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(14,"Section 4"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"Section 5"));

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(this,R.layout.options_section,R.id.section_text,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }

    public void profileFromOptionsButtonCallback(View view){
        finish();
    }
}

