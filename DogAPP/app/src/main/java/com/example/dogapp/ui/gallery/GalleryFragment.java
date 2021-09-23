package com.example.dogapp.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.dogapp.MainActivity;
import com.example.dogapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<itemModel> arrayList;
    public static final int PERMISSION_WRITE = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

//            protected void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                setContentView(R.layout.activity_main);
//
                checkPermission();
                recyclerView= root.findViewById(R.id.rv_1);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                arrayList = new ArrayList<>();
                new fetchData().execute();
//            }
        return root;
    }
    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            arrayList.clear();
            String result = null;
            try {
                URL url = new URL("https://dog.ceo/api/breeds/image/random/10");
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
    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }
}
