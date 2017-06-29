package nl.adeda.sharelocation.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.ContactListAdapter;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.Interfaces.GroupAddCallback;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 8-6-2017.
 */

public class AddGroupActivity extends AppCompatActivity implements GroupAddCallback {

    private DateTime dateTime;
    private EditText groupNameField;
    private ListView contactListToAdd;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toevoegen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toevoegen_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        // OnClickListener for back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        groupNameField = (EditText) findViewById(R.id.group_name);

        final EditText emailField = (EditText) findViewById(R.id.contact_to_add);
        final Button toevBtn = (Button) findViewById(R.id.contact_add_btn);
        contactListToAdd = (ListView) findViewById(R.id.contactlist_to_add);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_go_to_time);

        userList = new ArrayList<>();
        FirebaseHelper.userIds = new ArrayList<>();

        // OnClickListener for add button
        toevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();

                FirebaseHelper.groupAddDelegate = AddGroupActivity.this;
                // Check if user that has been typed in exists
                FirebaseHelper.addUserIfExists(email);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickers(dateTime);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_toevoegen_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_make_group) {
            String groupName = groupNameField.getText().toString();
            ArrayList<String> usersToAdd = FirebaseHelper.returnAddedKeys();

            FirebaseHelper.pushToFirebaseOnAddingGroup(groupName, dateTime, usersToAdd);

            Toast.makeText(AddGroupActivity.this, "Groep aangemaakt!", Toast.LENGTH_SHORT).show();

            finish();
            //onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPickers(final DateTime setDateTime) {
        final Calendar calendar = Calendar.getInstance();

        int year, month, day;

        // If user has already set a date, show it.
        if (setDateTime == null) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = setDateTime.getYear();
            month = setDateTime.getMonth();
            day = setDateTime.getDay();
        }

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(AddGroupActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTime = new DateTime(dayOfMonth, month + 1, year);
                showTimePicker(calendar, dateTime, setDateTime);
                Log.e("DATE", dayOfMonth + "-" + month + "-" + year);

            }
        }, year, month, day);

        datePickerDialog.show();


    }

    private void showTimePicker(Calendar calendar, final DateTime dateTime, DateTime setDateTime) {
        int hour, minute;

        // If user has already set a time, show it.
        if (setDateTime == null) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            hour = setDateTime.getHour();
            minute = setDateTime.getMinute();
        }

        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddGroupActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.e("TIME", hourOfDay + ":" + minute);
                dateTime.setHour(hourOfDay);
                dateTime.setMinute(minute);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void addUserToList(User user, boolean isInList) {
        if (isInList) {
            Toast.makeText(this, "Gebruiker is al toegevoegd!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contactListToAdd.getAdapter() == null) { // Set adapter
            ContactListAdapter adapter = new ContactListAdapter(getApplicationContext(), userList);
            contactListToAdd.setAdapter(adapter);
            adapter.add(user);
        } else { // Keep old adapter
            ContactListAdapter adapter = (ContactListAdapter) contactListToAdd.getAdapter();
            adapter.add(user);
        }
    }
}
