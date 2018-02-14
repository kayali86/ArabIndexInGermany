package kayali.developer.android.arabindexingermany;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;

    private RecyclerView companiesRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (authCurrentUser == null){
                    Intent signInIntent = new Intent(MainActivity.this, LoginActivity.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signInIntent);
                }
            }
        };

        companiesRecycler = findViewById(R.id.companies_list);
        companiesRecycler.setHasFixedSize(true);
        companiesRecycler.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<SummaryForCard,CardViewHolder> FBRA = new FirebaseRecyclerAdapter<SummaryForCard, CardViewHolder>(
                SummaryForCard.class,
                R.layout.activity_card,
                CardViewHolder.class,
                refCompanies
        )

        {
            @Override
            protected void populateViewHolder(CardViewHolder viewHolder, SummaryForCard model, int position) {


                final String company_key = getRef(position).getKey().toString();


                viewHolder.setCompanyName(model.getCompanyName());
                viewHolder.setCompanyServices(model.getCompanyServices());
                viewHolder.setAddress(model.getCompanyAddress());
                viewHolder.setImage(getApplicationContext(), model.getImagePath());

                if (!TextUtils.isEmpty(model.getCompanyRatingAverage())){
                    viewHolder.setCompanyRatingAverage(model.getCompanyRatingAverage());
                }


                if (!TextUtils.isEmpty(model.getCompanyRatingTimes())){
                    viewHolder.setCompanyRatingTimes(model.getCompanyRatingTimes());
                }


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent companyDetailsIntent = new Intent(MainActivity.this, CompanyDetailsActivity.class);
                        companyDetailsIntent.putExtra("CompanyID", company_key);
                        startActivity(companyDetailsIntent);
                    }
                });



            }
        };

        companiesRecycler.setAdapter(FBRA);
    }



    public static class CardViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public CardViewHolder (View itemView){
            super (itemView);
            mView = itemView;
        }

            // To set company name in CardView in SummaryForCard Class
        public void setCompanyName(String companyName){
            TextView cardCompanyName = mView.findViewById(R.id.card_company_name);
            cardCompanyName.setText(companyName);
        }

        // To set company Services in CardView in Class SummaryForCard
        public void setCompanyServices(String companyServices){
            TextView cardCompanyServices = mView.findViewById(R.id.card_services);
            cardCompanyServices.setText(companyServices);
        }

        // To set a username in CardView in Class SummaryForCard
        public void setAddress(String companyAddress){
            TextView cardAddress = mView.findViewById(R.id.card_address);
            cardAddress.setText(companyAddress);
        }

        public void setImage (Context ctx, String imagePath){
            ImageView cardImage = mView.findViewById(R.id.company_image);
            Picasso.with(ctx).load(imagePath).into(cardImage);
        }

        // To set a Rating Average in CardView in Class SummaryForCard
        public void setCompanyRatingAverage(String companyRatingAverage){
            RatingBar cardCompanyRatingAverage = mView.findViewById(R.id.average_rate_bar);
            float newCompanyRatingAverage = Float.parseFloat(String.valueOf(companyRatingAverage));
            cardCompanyRatingAverage.setRating(newCompanyRatingAverage);
        }


        // To set a Rating Times in CardView in Class SummaryForCard
        public void setCompanyRatingTimes(String companyRatingTimes){
            TextView cardCompanyRatingTimes = mView.findViewById(R.id.rating_times);
            cardCompanyRatingTimes.setText(companyRatingTimes);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.homeIcon).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            settingsIntent.putExtra("email", authCurrentUser.getEmail());
            startActivity(settingsIntent);
            return true;
        }

        // Add Button in Actionbar
        else if (id == R.id.addIcon){
            Intent intent = new Intent(MainActivity.this, AddCompanyActivity.class);
            startActivity(intent);
        }

        // Remembered Companies List Button in Actionbar
        else if (id == R.id.remembered){
            Intent intent = new Intent(MainActivity.this, RememberedCompaniesActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.logoutIcon){
            mAuth.signOut();
        }



        return super.onOptionsItemSelected(item);
    }



}
