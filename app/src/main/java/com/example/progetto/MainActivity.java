package com.example.progetto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvSelectedDate;
    private EditText etReminder;
    private Button btnSave, btnGoToInfo; // Aggiunto bottone per terza Activity
    private ListView listViewReminders;

    private String selectedDate;
    private ArrayList<String> remindersList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        etReminder = findViewById(R.id.etReminder);
        btnSave = findViewById(R.id.btnSave);
        btnGoToInfo = findViewById(R.id.btnGoToInfo);
        listViewReminders = findViewById(R.id.listViewReminders);

        sharedPreferences = getSharedPreferences("SchoolCalendarPrefs", Context.MODE_PRIVATE);
        remindersList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        tvSelectedDate.setText("Impegni per il: " + selectedDate);

        // Carica i promemoria del giorno corrente
        loadReminders(selectedDate);

        // Gestione del cambio data nel Calendario
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, (month + 1), year);
                tvSelectedDate.setText("Impegni per il: " + selectedDate);
                loadReminders(selectedDate);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = etReminder.getText().toString().trim();

                if (!reminderText.isEmpty()) {
                    saveReminder(selectedDate, reminderText);
                    etReminder.setText("");

                    Intent serviceIntent = new Intent(MainActivity.this, ReminderService.class);
                    serviceIntent.putExtra("nota_testo", "Nuovo appunto aggiunto per il " + selectedDate + ": " + reminderText);
                    startService(serviceIntent);

                } else {
                    Toast.makeText(MainActivity.this, "Inserisci un testo per il promemoria", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfo = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intentInfo);
            }
        });

        listViewReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String promemoriaSelezionato = remindersList.get(position);

                if (!promemoriaSelezionato.equals("Nessun impegno scolastico per oggi.")) {
                    Intent intentDettaglio = new Intent(MainActivity.this, DettaglioActivity.class);
                    intentDettaglio.putExtra("data_selezionata", selectedDate);
                    intentDettaglio.putExtra("testo_promemoria", promemoriaSelezionato);
                    startActivity(intentDettaglio);
                }
            }
        });
    }

    private void loadReminders(String date) {
        remindersList.clear();
        Set<String> set = sharedPreferences.getStringSet(date, new HashSet<String>());
        remindersList.addAll(set);

        if (remindersList.isEmpty()) {
            remindersList.add("Nessun impegno scolastico per oggi.");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersList);
        listViewReminders.setAdapter(adapter);
    }

    private void saveReminder(String date, String reminder) {
        Set<String> existingSet = sharedPreferences.getStringSet(date, new HashSet<String>());
        Set<String> newSet = new HashSet<>(existingSet);
        newSet.add(reminder);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(date, newSet);
        editor.apply();

        Toast.makeText(this, "Promemoria salvato!", Toast.LENGTH_SHORT).show();
        loadReminders(date);
    }
}