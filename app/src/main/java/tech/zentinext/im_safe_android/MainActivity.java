package tech.zentinext.im_safe_android;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.internal.lo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mLocationPermissionGranted;
    Location mylastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        new LocationTracker().execute(new Object[]{MainActivity.this,true});



    }

    public void firealarm(View view){
        System.out.println(mylastLocation);
    }

    public void getLoco(){
        getCurrentLocation();
    }

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getCurrentLocation() {
        //System.out.println("Doing it");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        mylastLocation = location;
                        //System.out.println(location);
                    }
                    //System.out.println("No location");


                }
            });
        }
        //System.out.println("No acesss");

    }
}

class LocationTracker extends AsyncTask{


    @Override
    protected Object doInBackground(Object[] params) {
        while (true){

            try {
                while((boolean)params[1]){
                MainActivity mainActivity = (MainActivity) params[0];
                mainActivity.getLoco();
                    System.out.println("New Location");
                Thread.sleep(4000);}
                return null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
