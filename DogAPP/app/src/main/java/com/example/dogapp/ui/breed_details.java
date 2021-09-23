package com.example.dogapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogapp.R;
import com.example.dogapp.ui.gallery.CustomAdapter;
import com.example.dogapp.ui.gallery.itemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class breed_details extends Fragment {
    public class breed_detail extends Fragment {
        RecyclerView recyclerView;
        ArrayList<itemModel> arrayList;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View root = inflater.inflate(R.layout.fragment_breed_details, container, false);
            recyclerView= root.findViewById(R.id.rv_2);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            arrayList = new ArrayList<>();
            new image().execute();
            return root;
        }
        public class image extends AsyncTask<String, String, String> {

            @Override
            public void onPreExecute() {
                super .onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                arrayList.clear();
                String result = null;
                try {
                    URL url = new URL("https://dog.ceo/api/breed/shihtzu/images/random/10");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String temp;

                        while ((temp = reader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }
                        result = stringBuilder.toString();
                    }else  {
                        result = "error";
                    }

                } catch (Exception  e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void onPostExecute(String s) {
                super .onPostExecute(s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("message");

                    for (int i = 0; i < array.length(); i++) {

                        //JSONObject jsonObject = array.getJSONObject(i);
                        String image = array.getString(i);
                        itemModel model = new itemModel();
                        model.setImage(image);
                        arrayList.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CustomAdapter adapter = new CustomAdapter(getContext(), arrayList, getActivity());
                recyclerView.setAdapter(adapter);

            }
        }
    }
}