package kayali.developer.android.arabindexingermany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CompanyDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;
    private DatabaseReference refCurrentCompany;
    private DatabaseReference refCurrentUser;

    private float companyOldRatingAverage;
    private String companyOldRatingTimes;

    String company_key;

    private ImageView dImageView;
    private RatingBar dNewRatingBar;
    private RatingBar dAverageRatingBar;
    private Button rateButton;
    private TextView dCompanyNameV;
    private TextView dAddressV;
    private TextView dServicesV;
    private TextView dParkingV;
    private TextView dPaymentV;
    private TextView dAppointmentV;
    private TextView dOtherServicesV;
    private TextView dNotesV;
    private TextView dOpeningHoursV;
    private TextView dContactV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            company_key = bundle.getString("CompanyID");
        }

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");
        refCurrentCompany = refCompanies.child(company_key);
        refCurrentUser = refUsers.child(authCurrentUser.getUid());


        dImageView = findViewById(R.id.dImageView);
        dNewRatingBar = findViewById(R.id.new_rate_bar);
        dAverageRatingBar = findViewById(R.id.average_rate_bar);
        rateButton = findViewById(R.id.rating_button);
        dCompanyNameV = findViewById(R.id.dCompanyNameV);
        dAddressV = findViewById(R.id.dAddressV);
        dServicesV = findViewById(R.id.dServicesV);
        dParkingV = findViewById(R.id.dParkingV);
        dPaymentV = findViewById(R.id.dPaymentV);
        dAppointmentV = findViewById(R.id.dAppointmentV);
        dOtherServicesV = findViewById(R.id.dOtherServicesV);
        dNotesV = findViewById(R.id.dNotesV);
        dOpeningHoursV = findViewById(R.id.dOpeningHoursV);
        dContactV = findViewById(R.id.dContactV);


        refCompanies.child(company_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imagePath = (String) dataSnapshot.child("imagePath").getValue();
                companyOldRatingAverage = Float.parseFloat(String.valueOf(dataSnapshot.child("companyRatingAverage").getValue()));
                companyOldRatingTimes = (String) String.valueOf(dataSnapshot.child("companyRatingTimes").getValue());


                String uID = (String) dataSnapshot.child("uID").getValue();

                String [] rated_companies = (String[]) dataSnapshot.child("rated_companies").getValue();

                String companyName = (String) dataSnapshot.child("companyName").getValue();
                String companyAddress = (String) dataSnapshot.child("companyAddress").getValue();
                String companyServices = (String) dataSnapshot.child("companyServices").getValue();

                String companyParking;
                boolean companyParkingAvailable = (boolean) dataSnapshot.child("companyParking").getValue();
                if (companyParkingAvailable)
                    companyParking = "Yes";
                else companyParking = "No";


                String companyAppointment;
                boolean mustCompanyAppointment = (boolean) dataSnapshot.child("companyAppointment").getValue();
                if (mustCompanyAppointment)
                    companyAppointment = "Yes";
                else companyAppointment = "No";

                String companyPayment = (String) dataSnapshot.child("companyPayment").getValue();
                String companyOtherServices = (String) dataSnapshot.child("companyOtherServices").getValue();
                String companyNotes = (String) dataSnapshot.child("companyNotes").getValue();
                String companyOpeningHours = (String) dataSnapshot.child("companyOpeningHours").getValue();
                String companyContactInfo = (String) dataSnapshot.child("companyContactInfo").getValue();
                Picasso.with(CompanyDetailsActivity.this).load(imagePath).into(dImageView);

                dAverageRatingBar.setRating(companyOldRatingAverage);


                dCompanyNameV.setText(companyName);
                dAddressV.setText(companyAddress);
                dServicesV.setText(companyServices);
                dParkingV.setText(companyParking);
                dPaymentV.setText(companyPayment);
                dAppointmentV.setText(companyAppointment);
                dOtherServicesV.setText(companyOtherServices);
                dNotesV.setText(companyNotes);
                dOpeningHoursV.setText(companyOpeningHours);
                dContactV.setText(companyContactInfo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String oldRatingAverage = String.valueOf(companyOldRatingAverage);
                final float oldRatingAverageFl = Float.parseFloat(oldRatingAverage);
                final Integer oldRatingTimes = Integer.valueOf(companyOldRatingTimes);
                int oldRatingTimesInt = oldRatingTimes.intValue();
                float currentRating = dNewRatingBar.getRating();

                final float newRatingAverage = ((oldRatingAverageFl*oldRatingTimesInt)+currentRating)/ (oldRatingTimesInt+1);
                final int newRatingTimesInt = oldRatingTimesInt +1;


                refCompanies.child(company_key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        refCurrentCompany.child("companyRatingAverage").setValue(String.valueOf(newRatingAverage));
                        refCurrentCompany.child("companyRatingTimes").setValue(newRatingTimesInt);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.addIcon).setVisible(false);
        menu.findItem(R.id.remembered).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Settings Button in Actionbar
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(CompanyDetailsActivity.this, SettingsActivity.class);
            settingsIntent.putExtra("email", authCurrentUser.getEmail());
            startActivity(settingsIntent);

        }else if(id == R.id.backIcon){
            Intent backIntent = new Intent(CompanyDetailsActivity.this, MainActivity.class);
            startActivity(backIntent);

        }else if (id == R.id.logoutIcon){
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }
    
}
