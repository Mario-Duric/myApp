package com.main.myapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;

public class Aplikacije extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apk_list);




        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
        String text = "";

        for(int i = 0; i< packageList.size();i++){
            PackageInfo packageInfo = packageList.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)== 0) {
                String name = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                File file = new File(packageInfo.applicationInfo.publicSourceDir);
                Long size = file.length()/(1024*1024);
                if(size == 0){
                    size = size + 1;
                }
                d("Mario","Ime: "+name + "s : " + size);
                text = text + " Ime: " + name + " Velicina: " + size+ "mb \n";




            }
        }
        TextView textView = findViewById(R.id.text);
        textView.setText(text);

    }

}
