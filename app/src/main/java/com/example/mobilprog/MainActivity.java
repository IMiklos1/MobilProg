package com.example.mobilprog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        Log.d("MainActivity", "Context: " + this);
try {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class); // stops here
        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
        });
} catch (Exception e){
    e.printStackTrace();
    Log.e("MainActivity", "Error in ViewModel setup: " + e.getMessage());
}


        FloatingActionButton buttonAddTask = findViewById(R.id.button_add_task);
        try {
            buttonAddTask.setOnClickListener(v -> openAddTaskDialog());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openAddTaskDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Reference to the input fields
        EditText editTaskName = dialogView.findViewById(R.id.edit_task_name);
        EditText editTaskDescription = dialogView.findViewById(R.id.edit_task_description);
        Button buttonAdd = dialogView.findViewById(R.id.button_add);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle the add task button click
        buttonAdd.setOnClickListener(v -> {
            String taskName = editTaskName.getText().toString();
            String taskDescription = editTaskDescription.getText().toString();

            // Basic validation to ensure fields are not empty
            if (taskName.isEmpty() || taskDescription.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter task details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Task object and save it using ViewModel
            Task newTask = new Task(taskName, taskDescription, false);
            taskViewModel.insert(newTask);

            // Dismiss the dialog after task is added
            dialog.dismiss();
        });
    }
}
