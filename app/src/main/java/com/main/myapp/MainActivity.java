package com.main.myapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.provider.Settings.Secure;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback{


    WifiManager wifiMenager;
    WifiReciver wifiReciver;
    ListAdapter listAdapter;
    ListView wifiList;
    List myWifiList;

    public void onBackPressed() {
        //startActivity(new Intent(MainActivity.this, Back.class));
    }


    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);














        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;


        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;


        String device = "MODEL: " + Build.MODEL + "\nDEVICE: "+ Build.DEVICE + "\nHEIGHT:(px) "+ height +"\nWIDTH: " + width + "\nBatery % " + batteryPct + "\nRAM MB " + totalMemory/(1024*1024);
        d("Mario", "my: " + totalMemory);
        TextView ve = findViewById(R.id.info);
        ve.setText(device);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Shutdown", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                /*try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{ "su", "-c", "reboot" });
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/


                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                pm.reboot(null);


            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Aplikacije.class));


            }
        });





        wifiList = (ListView)findViewById(R.id.myListView);
        wifiMenager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciver = new WifiReciver();

        registerReceiver(wifiReciver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.REBOOT},0);

        }else{
            scanWifiList();
        }

    }
    private void scanWifiList(){
        wifiMenager.startScan();
        myWifiList = wifiMenager.getScanResults();
        setAdapter();
    }
    private void setAdapter(){
        listAdapter = new ListAdapter(getApplicationContext(),myWifiList);
        wifiList.setAdapter(listAdapter);
    }

    class WifiReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }



}
