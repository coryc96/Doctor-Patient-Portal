package softwareengineering.doctor_patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    public String name;
    public String DorP;
    public String birth;
    public String gender;

    public EditText editName;
    public EditText editDorP;
    public EditText editBirth;
    public EditText editGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Firebase.setAndroidContext(this);

        prepareFields();

        CardView saveButton = (CardView) findViewById(R.id.save);
        CardView goHome = (CardView) findViewById(R.id.goHome);

        editName = (EditText) findViewById(R.id.editName);
        editDorP = (EditText) findViewById(R.id.editDorP);
        editBirth = (EditText) findViewById(R.id.editBirth);
        editGender = (EditText) findViewById(R.id.editGender);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = editName.getText().toString();
                if(!(tmp.length() == 0 || tmp.equals("") || tmp == null)) {
                    name = tmp;
                    String url = "https://doctor-patient-portal-83e4b.firebaseio.com/users.json";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/info");
                            reference.child("name").setValue(name);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                    rQueue.add(request);
                }
                tmp = editDorP.getText().toString();
                if(!(tmp.length() == 0 || tmp.equals("") || tmp == null)) {
                    DorP = tmp;
                    String url = "https://doctor-patient-portal-83e4b.firebaseio.com/users.json";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/info");
                            reference.child("DorP").setValue(DorP);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                    rQueue.add(request);
                }
                    tmp = editBirth.getText().toString();
                    if(!(tmp.length() == 0 || tmp.equals("") || tmp == null)) {
                        birth = tmp;
                        String url = "https://doctor-patient-portal-83e4b.firebaseio.com/users.json";
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/info");
                                reference.child("birth").setValue(birth);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                        RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                        rQueue.add(request);
                    }
                    tmp = editGender.getText().toString();
                    if(!(tmp.length() == 0 || tmp.equals("") || tmp == null)) {
                        gender = tmp;
                        String url = "https://doctor-patient-portal-83e4b.firebaseio.com/users.json";
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/info");
                                reference.child("gender").setValue(gender);
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {}
                        });
                        RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                        rQueue.add(request);
                }
                prepareFields();
                Toast.makeText(Profile.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    void prepareFields() {

        Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editName.setText(dataSnapshot.child("name").getValue(String.class));
                editDorP.setText(dataSnapshot.child("DorP").getValue(String.class));
                editBirth.setText(dataSnapshot.child("birth").getValue(String.class));
                editGender.setText(dataSnapshot.child("gender").getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}

