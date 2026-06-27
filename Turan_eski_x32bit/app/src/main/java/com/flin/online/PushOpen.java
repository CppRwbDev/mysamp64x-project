package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.flin.online.install.InstallClient;
import com.flinc.core.R;

import java.util.List;

public class PushOpen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_open);

        Intent mainIntent = getIntent();
        if (mainIntent!=null && mainIntent.getData()!=null
                && (mainIntent.getData().getScheme().equals("flingame"))){
            Uri data = mainIntent.getData();
            List<String> pathSegments = data.getPathSegments();
            if(pathSegments.size()>0) {
                String prefix = pathSegments.get(0); // This will give you prefix as path
                System.out.println("МИХАИЛ  DEEPLINK:"+ prefix);
            }
        }

        Uri URIdata = getIntent().getData();
        System.out.println("МИХАИЛ  DEEPLINK URIdata:"+ URIdata);
        if(URIdata != null){
            String scheme = URIdata.getScheme();
            System.out.println("МИХАИЛ  DEEPLINK scheme:"+ scheme);
            String host = URIdata.getHost();
            String hostPath = URIdata.getPath();
            System.out.println("МИХАИЛ  DEEPLINK hostPath:"+ hostPath+"|");
            System.out.println("МИХАИЛ  DEEPLINK host:"+ host);
            if(hostPath.equals("/dimkov")){
                Intent intent = new Intent(PushOpen.this, SetingsActivity.class);
                startActivity(intent);
                return;
            }
            List params = URIdata.getPathSegments();



        }
        Intent intent = new Intent(PushOpen.this, DonateActivity.class);
        startActivity(intent);

    }
}