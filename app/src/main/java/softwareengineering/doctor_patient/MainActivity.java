package softwareengineering.doctor_patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.widget.ListPopupWindow.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    final CardView[] notesCard = new CardView[50];
    final TextView[] notesText = new TextView[50];
    final ImageButton[] deleteNote = new ImageButton[50];

    public ArrayList<String> notesList;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if (dataSnapshot.hasChild("notes")){
                     prepareNotes();
                 }
             }
             @Override
             public void onCancelled(FirebaseError firebaseError) {}
         });

                linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        CardView userButton = (CardView) findViewById(R.id.userBtn);
        CardView profileButton = (CardView) findViewById(R.id.profile);

        ImageButton addNote = (ImageButton) findViewById(R.id.addNote);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Users.class));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });


    }

    void prepareNotes() {

        Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/notes");

        linearLayout.removeView(findViewById(R.id.cardView));


        //ArrayList<Student> studentList = db.getAllStudents();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notesList = new ArrayList<>();

                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    notesList.add(child.getValue(String.class));
                    fillNotes();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });


/*
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //editName.setText(dataSnapshot.child("name").getValue(String.class));
                //editDorP.setText(dataSnapshot.child("DorP").getValue(String.class));
                //editBirth.setText(dataSnapshot.child("birth").getValue(String.class));
                //editGender.setText(dataSnapshot.child("gender").getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
*/
    }

    public void goToNotePopup(View view){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_note, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));

        //alert.setTitle("ID");

        alert.setView(alertLayout);

        final EditText editNote = alertLayout.findViewById(R.id.editNote);
        //final Button btnID = alertLayout.findViewById(R.id.saveID);

        alert.setCancelable(true).setPositiveButton("Save Note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/notes");

                String tmp = editNote.getText().toString();
                if(!(tmp.length() == 0 || tmp.equals("") || tmp == null)) {
                    final String note = tmp;
                    reference.push().setValue(note);
                    prepareNotes();
                }
                dialogInterface.dismiss();
            }
        });

        //alert.setCancelable(false);

        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
        alertDialog.show();
    }

    void fillNotes(){

        linearLayout.removeAllViews();

        for(int i = 0; i < notesList.size(); i++){



            final String current = notesList.get(i);

            RelativeLayout relativeLayout = new RelativeLayout(this);

            notesCard[i] = new CardView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            notesCard[i].setLayoutParams(params);

            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) notesCard[i].getLayoutParams();
            layoutParams.setMargins(30,15,30,5);

            notesCard[i].requestLayout();
            notesCard[i].setRadius(15);
            notesCard[i].setPadding(25, 25, 25, 25);
            notesCard[i].setMaxCardElevation(30);
            notesCard[i].setMaxCardElevation(6);

            linearLayout.addView(notesCard[i]);
            notesCard[i].addView(relativeLayout);

            deleteNote[i] = new ImageButton(this);
            relativeLayout.addView(deleteNote[i]);
            RelativeLayout.LayoutParams btnParams = (RelativeLayout.LayoutParams)deleteNote[i].getLayoutParams();
            btnParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            btnParams.addRule(RelativeLayout.CENTER_VERTICAL);
            deleteNote[i].setLayoutParams(btnParams);
            deleteNote[i].setImageResource(R.drawable.ic_delete);
            deleteNote[i].setBackgroundDrawable(null);
            deleteNote[i].setBackgroundColor(00000);
            //delete.setBackgroundColor(getResources().getColor(#FFFF));
            deleteNote[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertPopup(current);

                }
            });



            notesText[i] = new TextView(this);
            notesText[i].setLayoutParams(params);
            notesText[i].setGravity(Gravity.LEFT);
            notesText[i].setTextSize(15);


            relativeLayout.addView(notesText[i]);

            String text = String.format(current);
            notesText[i].setText(text);

        }

    }

    public void alertPopup(final String toDelete){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = String.format("Delete this note?");
        builder.setMessage(message)
                .setTitle("Warning:");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Firebase reference = new Firebase("https://doctor-patient-portal-83e4b.firebaseio.com/users/" + UserDetails.username + "/notes");
                Query deleteQuery = reference.orderByValue().equalTo(toDelete);

                deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            child.getRef().removeValue();
                            prepareNotes();
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError databaseError) {}
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
