package kayali.developer.android.arabindexingermany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent mainIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }


    }

    public void toLoginAct(View view) {
        Intent loginIntent = new Intent (HomeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void toSignUpUserAct(View view) {
        Intent signUpUserIntent = new Intent (HomeActivity.this, SignUpActivity.class);
        startActivity(signUpUserIntent);
    }

}
