package com.example.alanflores.practice;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alanflores.practice.DB.UserDataSource;
import com.example.alanflores.practice.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncTaskActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmptyView;
    private List<User> users;
    protected Handler handler;
    private UserDataSource userDataSource;
    private Button button;
    private EditText token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        token = (EditText) findViewById(R.id.text);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        users = new ArrayList<>();
        handler = new Handler();

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


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add null , so the adapter will check view_type and show progress bar at bottom
                        users.add(null);
                        mAdapter.notifyItemInserted(users.size());
                    }
                }, 0);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        //   remove progress item
                        users.remove(users.size() - 1);
                        mAdapter.notifyItemRemoved(users.size());

                        users.add(new User("User ", "androidstudent@gmail.com", Integer.toString(10)));
                        mAdapter.notifyItemInserted(users.size());
                        //add items one by one
                        /*int start = users.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            users.add(new User("User " + i, "androidstudent" + i + "@gmail.com", Integer.toString(i)));
                            mAdapter.notifyItemInserted(users.size());
                        }*/
                        Log.v("Entro","Entro");
                        mAdapter.setLoaded(false);
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetUser getUser = new GetUser();
                getUser.execute("hola");
            }
        });
    }



//doInBackgroundandresive/onProgressUpdate/onPostExecuteresultofdoInBackground
class GetUser extends AsyncTask<String,Object,List<User>>{



    //comunicate antes con la interfas
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        token = (EditText) findViewById(R.id.text);
        Toast.makeText(getApplicationContext(),"Iniciando descarga" + token.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        Toast.makeText(getApplicationContext(),"ya mero descarga" + token.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<User> doInBackground(String... strings) {
        Log.v("Aqui",strings[0]);
        //See progress connect with onProgressUpdate
        publishProgress();
        try {
            /*URL url = new URL("http://192.168.1.75:8080/api_job/admin/getUser");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+ token);
            connection.connect();*/

            URL url = new URL("http://192.168.43.21:8080/api_job/admin/getUser");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFsYW5fZmxvcmVzIiwicGFzc3dvcmQiOiIxMjM0NTYiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImV4cCI6MTQ4NDc2MDU5NX0.ejPnHPb7dvxG8SX8FaBAmc16x54jC4kXJG2N7SyKRvU");
            connection.connect();

            Log.v("Code", Integer.toString(connection.getResponseCode()));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder resultadoWebService = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                resultadoWebService.append(line);
            }
            bufferedReader.close();

            Log.v("Result",resultadoWebService.toString());
            return parseInformacion(resultadoWebService.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<User> parseInformacion(String resultadoJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(resultadoJson);
        JSONArray jsonArray = null;
        List<User> users = new ArrayList<>();
        String nombre , clave;
        try {
            jsonArray = jsonObject.getJSONArray("data");
            Log.v("Array data",jsonArray.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObjectUser = jsonArray.getJSONObject(i);
            User user = new User();
            user.setId(Integer.toString(jsonObjectUser.getInt("id")));
            user.setUsername(jsonObjectUser.getString("username"));
            user.setPassword(jsonObjectUser.getString("name") + " " + jsonObjectUser.getString("last_name"));
            users.add(user);
        }

        return users;

    }
    @Override
    protected void onPostExecute(List<User> list) {
        super.onPostExecute(list);
        Toast.makeText(getApplicationContext(),"ya quedo descarga",Toast.LENGTH_SHORT).show();

        mAdapter.setLoaded(false);

        for (User user : list) {
            users.add(user);
            mAdapter.notifyItemInserted(users.size());
        }

        if (users.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }
}
}
