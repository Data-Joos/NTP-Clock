package com.example.ntp_clock;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ntp_clock.databinding.ActivityMainBinding;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private TextView textViewLabelTime;
    private TextView textViewClock;
    private Handler handler = new Handler();
    private SimpleDateFormat clock = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate the layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set up the ActionBar and navigation
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Initialize UI elements
        textViewLabelTime = findViewById(R.id.textViewLabelTime);
        textViewClock = findViewById(R.id.textViewClock);

        // Schedule a task to update the time every 1000 milliseconds (1 second)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateTimeOnNetWork();
            }
        }, 1000);
        super.onResume();
    }
    //Get the current system time
    private String getSystemTime(){
        clock.setTimeZone(TimeZone.getDefault());
        return clock.format(new Date());
    }
    //Get the time from an NTP server
    private String getNTPTime(){
        NTPUDPClient ntpClient = new NTPUDPClient();
        ntpClient.setDefaultTimeout(3000);
        try {
            InetAddress inetAddress = InetAddress.getByName("1.se.pool.ntp.org");
            TimeInfo timeInfoServer = ntpClient.getTime(inetAddress);
            long ntpTime = timeInfoServer.getReturnTime();
            TimeZone localTimeZone = TimeZone.getTimeZone("Europe/Stockholm");
            clock.setTimeZone(localTimeZone);
            Date localTime = new Date(ntpTime + localTimeZone.getOffset(ntpTime));

            return clock.format(new Date(ntpTime));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage(); // Handle errors and display an error message
        } finally {
            ntpClient.close(); //Close the NTP client
        }
    }
    //Checking if the network is available
    private boolean isMyNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network != null){
            return true; //Network is available
        }else {
            return false; //Network is not available
        }
    }
    //Update the time based on network availability
    private void updateTimeOnNetWork(){
        if (isMyNetworkAvailable()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String networkTimeText = getNTPTime(); //Get NTP time
                    updateUserInterface(networkTimeText, "Network time", Color.GREEN);
                }
            }).start();
        }else {
            String systemTimeText = getSystemTime(); //Get system time
            updateUserInterface(systemTimeText, "System time", Color.RED);
        }
    }
    //Update the UI with the retrieved time
    private void updateUserInterface(String time, String label, int labelColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewClock.setText(time); //Set the time on the UI
                textViewLabelTime.setText(label); //Set the label on the UI
                textViewLabelTime.setTextColor(labelColor); //Set the label color
            }
        });
    }}