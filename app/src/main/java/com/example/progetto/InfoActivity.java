package com.example.progetto;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView tvCrediti = findViewById(R.id.tvCrediti);
        tvCrediti.setText("Applicazione sviluppata da:\n- Andrea Manzoni\n- Luca Baroni\n- Simone Busi\n\nClasse 4AI - Progetto TPSIT 2026");
    }
}