package com.example.alanflores.practice;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alanflores.practice.model.User;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by alan.flores on 1/12/17.
 */
public class MyAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<User> userList;
    private int visibleThreshold = 0;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    //private String[] mDataset;
    //private String[] mApe;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;
        public TextView password;
        public TextView idUser;

        public UserViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.username);
            password = (TextView) v.findViewById(R.id.password);
            idUser = (TextView) v.findViewById(R.id.idUser);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),username.getText().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
                    v.getContext().sendBroadcast(intent);
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    /*public MyAdapter(String[] myDataset,String[] mApe) {
        mDataset = myDataset;
        this.mApe = mApe;
    }*/

    public MyAdapter(List<User> userList) {
        this.userList = userList;
    }

    public MyAdapter() {
    }

    public MyAdapter(List<User> userList, RecyclerView recyclerView) {
        this.userList = userList;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            Log.v("MyAdapter", "adios");
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            //super.onScrolled(recyclerView, dx, dy);


                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }

                            Log.v("MyAdapter", "hola");
                            Log.v("MyAdapter", Integer.toString(totalItemCount));
                            Log.v("MyAdapter", Integer.toString(lastVisibleItem + visibleThreshold));
                        }
                    });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        RecyclerView.LayoutParams example = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(example);

        return new ViewHolder(v);*/
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.user_list_row, parent, false);

            vh = new UserViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof UserViewHolder){
            User user= userList.get(position);
            ((UserViewHolder)holder).username.setText(user.getUsername());
            ((UserViewHolder)holder).password.setText(user.getPassword());
            ((UserViewHolder)holder).idUser.setText(user.getId());
        }else
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    public void setLoaded(Boolean loading) {
        this.loading = loading;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}