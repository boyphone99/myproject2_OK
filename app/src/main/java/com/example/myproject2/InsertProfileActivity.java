package com.example.myproject2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class InsertProfileActivity extends AppCompatActivity {
    private static final String KEY_USERID = "username";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST = "last";
    private static final String KEY_AGE = "age";
    private static final String KEY_EMPTY = "";
    private EditText etName;
    private EditText etLast;
    private EditText etAge;
    private String name;
    private String last;
    private String age;
    private String userid;
    private ProgressDialog pDialog;
    private String insertadd_url = "https://finaltest001.000webhostapp.com/api/Insertprofile.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        final User user = session.getUserDetails();
        setContentView(R.layout.activity_insert_profile);

        etName = findViewById(R.id.etName);
        etLast = findViewById(R.id.etLast);
        etAge = findViewById(R.id.etAge);

        Button save = findViewById(R.id.butSave);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                last = etLast.getText().toString().trim();
                age = etAge.getText().toString().trim();
                userid = user.username;
                if (validateInputs()) {
                    insertAddress();
                }
            }
        });
    }

    private void insertAddress() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_USERID, userid);
            request.put(KEY_NAME, name);
            request.put(KEY_LAST, last);
            request.put(KEY_AGE, age);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, insertadd_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(InsertProfileActivity.this);
        pDialog.setMessage("กำลังบันทึก");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(name)) {
            etName.setError("กรุณากรอกข้อมูล");
            etName.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(last)) {
            etLast.setError("กรุณากรอกข้อมูล");
            etLast.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(age)) {
            etAge.setError("กรุณากรอกข้อมูล");
            etAge.requestFocus();
            return false;
        }

        return true;
    }
}

