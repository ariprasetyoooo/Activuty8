package com.example.kelascsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kelascsqlite.database.DBController;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class edit_data extends Activity {
    TextInputEditText Nama,Telepon;
    Button Save;
    String nma,tlp,id;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        Nama = findViewById(R.id.edNama);
        Telepon = findViewById(R.id.edtelp);
        Save = findViewById(R.id.simpanBtn);

        id = getIntent().getStringExtra("id");
        nma = getIntent().getStringExtra("nama");
        tlp = getIntent().getStringExtra("telpon");

        setTitle("Edit Data");
        Nama.setText(nma);
        Telepon.setText(tlp);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Nama.getText().toString().equals("") || Telepon.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Mohon isi data terlebih dahulu !!!", Toast.LENGTH_SHORT).show();
                }else{
                    nma = Nama.getText().toString();
                    tlp = Telepon.getText().toString();
                    HashMap<String,String> values = new HashMap<>();
                    values.put("id",id);
                    values.put("nama",nma);
                    values.put("telpon",tlp);
                    controller.UpdateData(values);
                }

            }
        });
    }

}