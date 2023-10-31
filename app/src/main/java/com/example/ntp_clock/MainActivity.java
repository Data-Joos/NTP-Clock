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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        textViewLabelTime = findViewById(R.id.textViewLabelTime);
        textViewClock = findViewById(R.id.textViewClock);
        clock.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateTimeOnNetWork();
            }
        }, 1000);
        super.onResume();
    }

    private String getSystemTime(){
        return clock.format(new Date());
    }

    private String getNTPTime(){
        NTPUDPClient ntpClient = new NTPUDPClient();
        ntpClient.setDefaultTimeout(3000);
        try {
            InetAddress inetAddress = InetAddress.getByName("3.se.pool.ntp.org");
            TimeInfo timeInfoServer = ntpClient.getTime(inetAddress);
            long ntpTime = timeInfoServer.getReturnTime();
            return clock.format(new Date(ntpTime));

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            ntpClient.close();
        }
    }

    private boolean isMyNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network != null){
            return true;
        }else {
            return false;
        }
    }
    private void updateTimeOnNetWork(){
        if (isMyNetworkAvailable()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String networkTimeText = getNTPTime();
                    updateUserInterface(networkTimeText, "Network time", Color.GREEN);
                }
            }).start();
        }else {
            String systemTimeText = getSystemTime();
            updateUserInterface(systemTimeText, "System time", Color.RED);
        }
    }
    private void updateUserInterface(String time, String label, int labelColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewClock.setText(time);
                textViewLabelTime.setText(label);
                textViewLabelTime.setTextColor(labelColor);
            }
        });
    }}