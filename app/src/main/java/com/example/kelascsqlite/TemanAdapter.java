package com.example.kelascsqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kelascsqlite.app.AppController;
import com.example.kelascsqlite.database.DBController;
import com.example.kelascsqlite.database.EditTeman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listData;
    private Context control;

    public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
        private ArrayList<Teman> listData;

        public TemanAdapter(ArrayList<Teman> listData) {
            this.listData = listData;
        }

        private Context control;

        public TemanAdapter.TemanViewHolder onCreateTemanViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
            View v = layoutInf.inflate(R.layout.row_data_teman, parent, false);
            control = parent.getContext();
            return new TemanViewHolder(v);
        }


        @Override
        public TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutinf = LayoutInflater.from(parent.getContext());
            View view = layoutinf.inflate(R.layout.row_data_teman, parent, false);
            control = parent.getContext();
            return new TemanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TemanAdapter.TemanViewHolder holder, int position) {
            String nm, tlp, id;

            id = listData.get(position).getId();
            nm = listData.get(position).getNama();
            tlp = listData.get(position).getTelpon();
            DBController db = new DBController(control);


            holder.namaTxt.setTextSize(20);
            holder.namaTxt.setTextColor(Color.BLUE);
            holder.namaTxt.setText(nm);
            holder.telponTxt.setText(tlp);

            holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu pm = new PopupMenu(v.getContext(), v);
                    pm.inflate(R.menu.popup1);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    Bundle bendel = new Bundle();
                                    bendel.putString("kunci1", id);
                                    bendel.putString("kunci2", nm);
                                    bendel.putString("kunci3", tlp);
                                    Intent inten = new Intent(v.getContext(), EditTeman.class);
                                    inten.putExtras(bendel);
                                    v.getContext().startActivity(inten);
                                    break;

                                case R.id.hapus:
                                    AlertDialog.Builder alertdb = new AlertDialog.Builder(v.getContext());
                                    alertdb.setTitle("Yakin" + nm + "akan dihapus?");
                                    alertdb.setMessage("Tekan Ya untuk menghapus");
                                    alertdb.setCancelable(false);
                                    alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HapusData(id);
                                            Toast.makeText(v.getContext(), "Data" + id + "telah dihapus", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                                            v.getContext().startActivity(intent);
                                        }
                                    });
                                    AlertDialog adlg = alertdb.create();
                                    adlg.show();
                                    break;
                            }

                            return true;
                        }
                    });
                    pm.show();

                    return true;
                }
            });
        }

        private void HapusData(final String idx) {
            String url_update = "http://10.0.2.2/umyTI/deletetm.php";
            final String TAG = MainActivity.class.getSimpleName();
            final String TAG_SUCCESS = "success";
            final int[] sukses = new int[1];

            StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Respon: " + response.toString());
                    try {
                        JSONObject jObj = new JSONObject(response);
                        sukses[0] = jObj.getInt(TAG_SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", idx);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringReq);
        }

        @Override
        public int getItemCount() {
            return (listData != null) ? listData.size() : 0;
        }


    public class TemanViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView namaTxt, telponTxt;

        public TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.cardku);
            namaTxt = (TextView) view.findViewById(R.id.textNama);
            telponTxt = (TextView) view.findViewById(R.id.textTelpon);
        }
    }
}
