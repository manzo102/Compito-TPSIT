package com.example.progetto;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) { // <--- Ora è corretto!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView tvCrediti = findViewById(R.id.tvCrediti);
        // Personalizza con i veri nomi del tuo gruppo da inserire nel README e nella relazione tecnica!
        tvCrediti.setText("Applicazione sviluppata da:\n- Studente 1\n- Studente 2\n- Studente 3\n\nClasse 4AI - Progetto TPSIT 2026");
    }
}