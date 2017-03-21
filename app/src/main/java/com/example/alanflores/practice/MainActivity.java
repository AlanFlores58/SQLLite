package com.example.alanflores.practice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alanflores.practice.DB.UserDataSource;
import com.example.alanflores.practice.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmptyView;
    private List<User> users;
    protected Handler handler;
    private UserDataSource userDataSource;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent goAsynkTask = new Intent(getApplicationContext(), AsyncTaskActivity.class);
                startActivity(goAsynkTask);
            }
        });


        tvEmptyView = (TextView) findViewById(R.id.empty_view);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        users = new ArrayList<>();
        handler = new Handler();
        createDB();
        loadData();

        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(users, mRecyclerView);


        mRecyclerView.setAdapter(mAdapter);


        if (users.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                users.add(null);
                mAdapter.notifyItemInserted(users.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        users.remove(users.size() - 1);
                        mAdapter.notifyItemRemoved(users.size());
                        //add items one by one
                        int start = users.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            users.add(new User("User " + i, "androidstudent" + i + "@gmail.com", Integer.toString(i)));
                            mAdapter.notifyItemInserted(users.size());
                        }
                        mAdapter.setLoaded(false);
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });





        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));




        /*mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = users.get(position);
                Toast.makeText(getApplicationContext(), user.getUsername() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /Toast.makeText(getApplicationContext(), "onScrollStateChanged " + Integer.toString(newState), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }


        });*/
    }

    // load initial data
    private void loadData2() {

        for (int i = 1; i <= 20; i++) {
            users.add(new User("User " + i, "androidstudent" + i + "@gmail.com", Integer.toString(i)));
        }
    }

    protected void createDB(){
        userDataSource = new UserDataSource(this);
    }

    protected void loadDataInsert(){
        userDataSource.open();
        for (int i = 1; i <= 20; i++) {
            userDataSource.insertUser("User " + i,"androidstudent" + i + "@gmail.com");
        }
        userDataSource.close();
    }

    protected void loadData(){
        loadDataInsert();
        userDataSource.open();
        users = userDataSource.getUsers();
        userDataSource.close();
    }

}
