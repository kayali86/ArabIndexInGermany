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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;

    private EditText loginNameV;
    private EditText loginEmailV;
    private EditText loginPassV;
    private EditText loginPass2V;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");

        loginNameV = findViewById(R.id.signUp_name);
        loginEmailV = findViewById(R.id.signUp_mail);
        loginPassV = findViewById(R.id.signUp_pass);
        loginPass2V = findViewById(R.id.signUp_pass2);

    }


    public void signUp(View view) {
        final String  loginName = loginNameV.getText().toString().trim();
        String loginEmail = loginEmailV.getText().toString().trim();
        String loginPass = "";
        String[] ratedCompanies = {""};
        final List ratedCompaniesList = new ArrayList<String>(Arrays.asList(ratedCompanies));

        if (loginPassV.getText().toString().equals(loginPass2V.getText().toString())) {
            loginPass = loginPassV.getText().toString();
        } else {
            Toast.makeText(this,"Passwords are not match!", Toast.LENGTH_LONG).show();
        }

        if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass) ){
            mAuth.createUserWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userID = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = refUsers.child(userID);
                        current_user_db.child("login_name").setValue(loginName);
                        current_user_db.child("my_company_id").setValue("");
                        current_user_db.child("remembered_companies").setValue("default");
                        current_user_db.child("rated_companies").setValue(ratedCompaniesList);
                        Intent mainIntent = new Intent(SignUpActivity.this, HaveCompanyActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }

    }
}
