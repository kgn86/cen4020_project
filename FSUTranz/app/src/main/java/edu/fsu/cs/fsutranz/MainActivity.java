package edu.fsu.cs.fsutranz;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.fsu.cs.fsutranz.ui.parking.ParkingFragment;

public class MainActivity extends AppCompatActivity implements ParkingFragment.OnParkingFragmentInteractionListener {

    // debug to see values inside String[] data
    // basically just includes all useful information related to what we need

                                                     //Garages:
    int[] usefulIndexes = new int[]{ 1,  3,  4,  5,  //Call
                                    14, 16, 17, 18,  //Saint
                                    27, 29, 30, 31,  //Spirit
                                    40, 42, 43, 44,  //Traditions
                                    53, 55, 56, 57,  //Pensacola
                                    66, 68, 69, 70}; //Woodward

    private String[] buffer = new String[]{};
    private String[] data;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_parking, R.id.navigation_bus)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        refreshData();
    }

    //This sections adds the context menu to the application
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
        return true;
    }

    //This section determines what the context menu does depending on what you click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.announcements:
                Toast.makeText(this, "Announcement", Toast.LENGTH_LONG).show();
                break;
            case R.id.Info:
                Toast.makeText(this, "Info", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    public void refreshData() {
        buffer = new RetrieveParkingData().doInBackground();
        data = new String[buffer.length];

        // if no data is available, the website we are scraping from displays "NO RECORDS" and nothing else...
        if(!buffer[0].equals("NO RECORDS")) {
            for (int i = 0; i < usefulIndexes.length; i++) {
                data[i] = buffer[usefulIndexes[i]];
            }

            // index 9 holds last updated time for parking data
            time = buffer[9].substring(buffer[9].length()-20, buffer[9].length()-10);
            time += " ";
            time += buffer[9].substring(buffer[9].length()-9, buffer[9].length()-4);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm aa");       // format to only get hour(1-12) and minute
                try{
                    Date date = dateFormat.parse(time);
                    time = outputFormat.format(date);
                }
                catch(ParseException pe){}
        }
        else{
            data[0] = "NO RECORDS";
        }
    }

    public String[] getData(){
        return data;
    }

    public void setActionBarTitle() {
        getSupportActionBar().setTitle("Last updated: "+time);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.garnet));
    }
}
