package com.example.dogapp.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dogapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder> {

    Context context;
    ArrayList<itemModel> arrayList;
    Activity activity;
    public static final int PERMISSION_WRITE = 0;

    public CustomAdapter(Context context, ArrayList<itemModel> arrayList,  Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public CustomAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getImage())
                .centerCrop()
                .into(viewHolder.image);

        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    new Downloading().execute(arrayList.get(position).getImage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView image, download;
        public viewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            download = (ImageView) itemView.findViewById(R.id.download);
        }
    }


    public class Downloading extends AsyncTask<String, Integer, String> {

        ProgressDialog dialog = new ProgressDialog(activity);
        @Override
        public void onPreExecute() {
            super .onPreExecute();
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/Dog");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/Dog", date + ".jpg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            dialog.dismiss();
            Toast.makeText(context, "Image Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }
}
