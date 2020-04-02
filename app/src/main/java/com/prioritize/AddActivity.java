package com.prioritize;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prioritize.adapters.ItemsAdapter;
import com.prioritize.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private static final String tag = "AddActivity";

    private EditText etTitle;
    private EditText etDescription;
    private RadioGroup rgPriority;
    private EditText etDueDate;
    private Button btnAddTask;
    private ImageButton ivHome;
    private ItemsAdapter itemsAdapter;
    private int year, month, day;
    private int radioPriority;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        radioPriority = 0;
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        rgPriority = findViewById(R.id.rgPriority);
        etDueDate = findViewById(R.id.etDueDate);
        btnAddTask = findViewById(R.id.btnAddTask);
        ivHome = findViewById(R.id.ivHome);

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class );
                startActivity(intent);
                displayMessage("Home");
            }
        });

        getCurrentDate();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDueDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        itemsAdapter = new ItemsAdapter(MainActivity.items);

        btnAddTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Task pendingTask = new Task();
                if (addTask(pendingTask)) {
                    MainActivity.items.add(pendingTask);
                    itemsAdapter.notifyItemInserted(MainActivity.items.size() - 1);
                    Log.d(tag, "Item was added");

                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                    displayMessage("Item was added");
                }
            }
        });

    }

    private boolean addTask(Task task) { //if task is invalid, returns false
        if (etTitle.getText().toString().trim().isEmpty()) {
            displayMessage("Task must have a title");
        } else if (radioPriority == 0) {
            displayMessage("Task must have a priority number");
        } else if (etDueDate.getText().toString().trim().isEmpty()) {
            displayMessage("Task must have a due date");
        } else {
            task.setTitle(etTitle.getText().toString().trim());
            task.setDescription(etDescription.getText().toString().trim());
            Date date = stringToDate(etDueDate.getText().toString().trim());
            task.setDueDate(date);
            return true;
        }
        return false;
    }

    public void onRadioButtonClicked(View v) {
        int selectedId = rgPriority.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        radioPriority = Integer.parseInt(radioButton.getText().toString());
        Log.d(tag, "Priority is " + radioPriority);

    }

    private void getCurrentDate() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);
    }

    private Date stringToDate(String tmp) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(tmp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void displayMessage(String message) { //convenience method for toasts
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}