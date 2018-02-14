package kayali.developer.android.arabindexingermany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;
    private DatabaseReference refCurrentUser;

    EditText emailV;
    EditText email2V;
    EditText passV;
    EditText pass2V;

    private String currentEmail;
    private String newEmail;
    private String newPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");
        refCurrentUser = refUsers.child(authCurrentUser.getUid());

        currentEmail = authCurrentUser.getEmail().toString();

        emailV = findViewById(R.id.newEmailV);
        email2V = findViewById(R.id.newEmail2V);
        passV = findViewById(R.id.newPassV);
        pass2V = findViewById(R.id.newPass2V);

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

        // Back Button in Actionbar
        if (id == R.id.homeIcon){
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);

        // Logout Button in Actionbar
        }else if (id == R.id.logoutIcon){
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateEmail (View view){

        if (emailV.getText().toString().equals(email2V.getText().toString())){
                newEmail = emailV.getText().toString();


            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentEmail, "password1234");

            // Prompt the user to re-provide their sign-in credentials
            authCurrentUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            authCurrentUser.updateEmail(newEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Email Address updated", Toast.LENGTH_SHORT).show();
                                                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                                                startActivity(settingsIntent);

                                            }
                                        }
                                    });
                        }
                    });
        }else {
            Toast.makeText(SettingsActivity.this, "Email Addresses not matching", Toast.LENGTH_SHORT).show();
        }
    }


    public void changePass(View view) {


        if (passV.getText().toString().equals(pass2V.getText().toString())){
            newPass = passV.getText().toString();


            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentEmail, "password1234");

            // Prompt the user to re-provide their sign-in credentials
            authCurrentUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            authCurrentUser.updatePassword(newPass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
        }else {
            Toast.makeText(SettingsActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteAccount(View view) {


            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentEmail, "password1234");

            // Prompt the user to re-provide their sign-in credentials
        authCurrentUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            authCurrentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });






    }
}
