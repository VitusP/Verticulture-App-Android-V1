package com.example.putra.verticulture_app_android_v1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.ui.Toaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParticleCloudSDK.init(this);
        ParticleDeviceSetupLibrary.init(this.getApplicationContext());

        //logout from cloud
        ParticleCloudSDK.getCloud().logOut();

        //Widget Objects
        EditText email= findViewById(R.id.editText_Email);
        EditText password = findViewById(R.id.editText_Password);
        //On "button" click call start Photon setup
        findViewById(R.id.button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String ID = email.getText().toString();
                        String Password = password.getText().toString();
                        email.getText().clear();
                        password.getText().clear();
                        particle_Login(ID, Password);
                    }
                });

        findViewById(R.id.button2)
                .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiProcedure<ParticleCloud>() {
                                @Override
                                public Void callApi(ParticleCloud particleCloud)
                                        throws ParticleCloudException, IOException {
                                    List<ParticleDevice> devices = particleCloud.getDevices();
                                    for(ParticleDevice device:devices){
                                        System.out.println(device.getName());
                                    }
                                    for (ParticleDevice particleDevice : devices) {
                                        if ("Photon_Verticulture_1".equals(particleDevice.getName())) {
                                            try {
                                                //4
                                                int resultCode = particleDevice.callFunction("Pump_State", Arrays.asList("ON"));
                                                System.out.println("Calling function");
                                                //5
                                                if (resultCode == 1) {
                                                    //Toast.makeText(MainActivity.this, "Called a function on myDevice", Toast.LENGTH_SHORT).show();
                                                    System.out.println("Succeed");
                                                }
                                            } catch (ParticleDevice.FunctionDoesNotExistException e) {
                                                e.printStackTrace();
                                                System.out.println("Failure");
                                            }
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                public void onFailure(ParticleCloudException exception) {

                                }
                            });
                }
        });
    }

    public void particle_Login(String ID, String Password)
    {
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                //  LOG IN TO PARTICLE
                try {
                    // Log in to Particle Cloud using username and password
                    ParticleCloudSDK.getCloud().logIn(ID, Password);
                    return "Logged in!";
                }
                catch(ParticleCloudException e) {
                    return "Error logging in!";
                }
            }

            protected void onPostExecute(String msg) {
                // Show Toast containing message from doInBackground
                Toaster.s(MainActivity.this, msg);
            }
        }.execute();
    }

}
