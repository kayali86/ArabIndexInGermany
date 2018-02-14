package kayali.developer.android.arabindexingermany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;
    private DatabaseReference refCurrentUser;
    private StorageReference storageReference;



/*
    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;
    private DatabaseReference refCurrentUser;
    private StorageReference storageReference;
    */



    private EditText loginMail;
    private EditText loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");
        refCurrentUser = refUsers.child(authCurrentUser.getUid());

        loginMail = findViewById(R.id.login_mail);
        loginPass = findViewById(R.id.login_pass);
    }


    public void login(View view) {
        String email = loginMail.getText().toString().trim();
        String pass = loginPass.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        checkUserExists();
                    }else {
                        Toast.makeText(LoginActivity.this, getString(R.string.sLoginFailed), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }






    public void checkUserExists (){
        final String userID = mAuth.getCurrentUser().getUid();
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userID)){
                    Intent loginIntent = new Intent (LoginActivity.this, MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                } else{
                    Toast.makeText(LoginActivity.this, getString(R.string.sWrongLogin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void signUpUser(View view) {
        Intent homeIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(homeIntent);
    }
}
