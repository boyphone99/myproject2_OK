package com.example.myproject2.Address;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myproject2.DashboardActivity;
import com.example.myproject2.Service.MySingleton;
import com.example.myproject2.R;
import com.example.myproject2.Session.SessionHandler;
import com.example.myproject2.Session.User;
import com.example.myproject2.Service.URLs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String KEY_STATUS = "status";
    private static final String KEY_USER = "username";
    private static final double KEY_LATITUDE = 0.00;
    private static final double KEY_LONGTIUDE = 0.00;
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_MESSAGE = "message";

    private SessionHandler session;
    private static final int REQUEST_CODE = 101;
    private double Latitude = 0.00;
    private double Longitude = 0.00;

    private ProgressDialog pDialog;


    private String username;
    private String detail;
    private String userid;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    TextView tvAddress;
    TextView tvUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        session = new SessionHandler(getApplicationContext());
        final User user = session.getUserDetails();

        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvUsername = (TextView) findViewById(R.id.tvUser);

        tvUsername.setText(user.getUsername());

        //ปุ่มยกเลิก
        Button buttoncancel = findViewById(R.id.butCancel);
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        //ปุ่มบันทึกที่อยู่
        Button buttonsave = findViewById(R.id.butSaveAddress);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username    = user.getUsername();
                Latitude    = currentLocation.getLatitude();
                Longitude   = currentLocation.getLongitude();
                detail      = getAddress(Latitude, Longitude);
                seveaddress();
            }
        });
}

    private void seveaddress() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USER, username);
            request.put(KEY_DETAIL, detail);
            request.put(String.valueOf(KEY_LATITUDE), Latitude);
            request.put(String.valueOf(KEY_LONGTIUDE), Longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, URLs.URL_SAVEADDRESS, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                session.loginUser(username);
                                loadDashboard();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void displayLoader() {
        ProgressDialog pDialog = new ProgressDialog(MapsActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), addressActivity.class);
        startActivity(i);
        finish();

    }


    //เช็คสิทธิ์การเข้าถึงว่ามีการขอหรือยัง
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {

            //ดึงตำแหน่งปัจจุบัน
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    //ปักหมุดสถานที่ และแสดงรายละเอียด
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Latitude = currentLocation.getLatitude();
        Longitude = currentLocation.getLongitude();
        LatLng latLng = new LatLng(Latitude, Longitude);
        tvAddress.setText(getAddress(Latitude, Longitude));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        googleMap.addMarker(markerOptions);
    }

    //ดึงข้อมูลรายละเอียดสถานที่ทั้งหมด
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    //ขอสิทธิ์เข้าถึงตำแหน่ง
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }}