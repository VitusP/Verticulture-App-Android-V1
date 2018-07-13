package com.example.putra.verticulture_app_android_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.particle.android.sdk.cloud.ParticleCloudSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParticleCloudSDK.init(this);
    }
}
