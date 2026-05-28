package com.example.progetto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;

public class DettaglioActivity extends AppCompatActivity {

    private TextView tvDettaglioData, tvDettaglioTesto;
    private Button btnCondividi, btnCercaInfo, btnSalvaFile;
    private String dataRicevuta, testoRicevuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio); // Ricordati di creare il rispettivo XML grafico

        tvDettaglioData = findViewById(R.id.tvDettaglioData);
        tvDettaglioTesto = findViewById(R.id.tvDettaglioTesto);
        btnCondividi = findViewById(R.id.btnCondividi);
        btnCercaInfo = findViewById(R.id.btnCercaInfo);
        btnSalvaFile = findViewById(R.id.btnSalvaFile);

        // Ricezione dati tramite Intent Extra (Requisito 3.1)
        Intent intentInresso = getIntent();
        dataRicevuta = intentInresso.getStringExtra("data_selezionata");
        testoRicevuto = intentInresso.getStringExtra("testo_promemoria");

        tvDettaglioData.setText("Data: " + dataRicevuta);
        tvDettaglioTesto.setText(testoRicevuto);

        // INTENT IMPLICITO 1: Condivisione del testo (Requisito 3.4)
        btnCondividi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Promemoria del " + dataRicevuta + ": " + testoRicevuto);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Condividi il tuo impegno con i compagni"));
            }
        });

        // INTENT IMPLICITO 2: Ricerca sul Web di informazioni sull'argomento (Requisito 3.4)
        btnCercaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "https://www.google.com/search?q=" + testoRicevuto;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
                startActivity(browserIntent);
            }
        });

        // INTERNAL STORAGE: Salva il promemoria in un file locale di testo (Requisito 3.5)
        btnSalvaFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeFile = "backup_nota.txt";
                String contenuto = "Data: " + dataRicevuta + " | Nota: " + testoRicevuto + "\n";

                try (FileOutputStream fos = openFileOutput(nomeFile, Context.MODE_APPEND)) {
                    fos.write(contenuto.getBytes());
                    Toast.makeText(DettaglioActivity.this, "Esportato in " + nomeFile + " (Internal Storage)", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DettaglioActivity.this, "Errore nel salvataggio del file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}