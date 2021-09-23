package com.example.dogapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dogapp.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> breed = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        listView=root.findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item,breed);
        listView.setAdapter(adapter);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "https://dog.ceo/api/breeds/list/all", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    for (int i = 0; i < response.getJSONObject("message").names().length(); i++){
//                        String.valueOf(response.getJSONObject("message").names());
                        String data = String.valueOf(response.getJSONObject("message").names().getString(i));
                        breed.add(data);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_breed);
            }
        });
        return root;
    }
}