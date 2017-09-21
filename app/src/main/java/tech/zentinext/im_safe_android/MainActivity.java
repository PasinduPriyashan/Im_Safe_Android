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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


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
                    if (mainActivity.mylastLocation!=null) {
                        String link = "https://imsafe.cf/location";

                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", "id");
                        jsonObject.put("value", 1296);
                        jsonArray.put(jsonObject);
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("name", "lat");
                        jsonObject1.put("value", mainActivity.mylastLocation.getLatitude());
                        jsonArray.put(jsonObject1);
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("name", "lon");
                        jsonObject2.put("value", mainActivity.mylastLocation.getLongitude());
                        jsonArray.put(jsonObject2);


                        String data = URLEncoder.encode("data", "UTF-8")+"="+URLEncoder.encode(jsonArray.toString(), "UTF-8");
                        URL url = new URL(link);
                        URLConnection conn = url.openConnection();
                        //System.out.println(data);
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write(data);
                        wr.flush();

                        BufferedReader reader = new BufferedReader(new
                                InputStreamReader(conn.getInputStream()));

                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        // Read Server Response
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        String results = sb.toString();
                        if (results.length() == 0) {

                        }
                        System.out.println(results);
                    }
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
