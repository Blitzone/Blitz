package com.example.bsaraci.blitzone.Options;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bsaraci.blitzone.Blitzone.DividerItemDecoration;
import com.example.bsaraci.blitzone.Profile.RecyclerItemClickListener;
import com.example.bsaraci.blitzone.R;
import com.example.bsaraci.blitzone.ServerComm.JWTManager;
import com.example.bsaraci.blitzone.Start.LogIn;

import java.util.ArrayList;
import java.util.List;

public class Options extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private String [] data = {"Change username","Change password","Log out","Visit website"};
    private ProgressDialog pg;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_main);

        username = getIntent().getExtras().getString("username");

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
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"Account"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(3,"About"));
        /*sections.add(new SimpleSectionedRecyclerViewAdapter.Section(12,"Section 3"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(14,"Section 4"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"Section 5"));*/

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(this,R.layout.options_section,R.id.section_text,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        if (position==0){
                            return;
                        }
                        else{
                            clickListByPosition(position,data[position-1]);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                })
        );

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }

    public void profileFromOptionsButtonCallback(View view){
        finish();
    }

    public void disconnectCallback() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to log out ?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                final LogOutAsyncTask logOutAsyncTask = new LogOutAsyncTask();
                logOutAsyncTask.execute();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class LogOutAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //Create progress dialog here and show it
            pg = ProgressDialog.show(Options.this, "", "Logging out ...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            Intent intent = new Intent(Options.this, LogIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            JWTManager jwtManager = new JWTManager(getApplicationContext());
            jwtManager.delToken();
            startActivity(intent);

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //update your listView adapter here
            //Dismiss your dialog

            pg.dismiss();
        }

    }

    public void clickListByPosition(int position, String toolbarTitle){
        final Intent intent;
        try {
            if (position == 1) {
                intent = new Intent(Options.this, ActivityChangeUsername.class);
                intent.putExtra("toolbarTitle", toolbarTitle);
                intent.putExtra("username",username);
                startActivity(intent);
            } else if (position == 2) {
                intent = new Intent(Options.this, ActivityChangePassword.class);
                intent.putExtra("toolbarTitle", toolbarTitle);
                startActivity(intent);
            } else if (position == 3) {
                disconnectCallback();
            }
        }
        catch (IndexOutOfBoundsException e)
        {

        }


    }

}

