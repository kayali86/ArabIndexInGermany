package kayali.developer.android.arabindexingermany;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddCompanyActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser authCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference refCompanies;
    private DatabaseReference refUsers;
    private DatabaseReference refCurrentUser;
    private StorageReference storageReference;
    private UploadTask uploadTask;


    private static final int GALLERY_REQUEST = 0;
    private Uri uri = null;

    private EditText companyNameV;
    private ImageButton imageButtonV;
    private EditText addressV;
    private EditText paymentV;
    private EditText servicesV;
    private EditText otherServicesV;
    private EditText otherNotesV;
    private EditText openingHoursV;
    private EditText contactInfoV;

    private Switch parkingV;
    private Switch appointmentV;

    private Spinner categoriesSpinner;
    private Spinner statesSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        mAuth = FirebaseAuth.getInstance();
        authCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refCompanies = mDatabase.child("companies");
        refUsers = mDatabase.child("users");
        refCurrentUser = mDatabase.child("users").child(authCurrentUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference();


        companyNameV = findViewById(R.id.company_name);
        addressV = findViewById(R.id.address);
        servicesV = findViewById(R.id.services);
        paymentV = findViewById(R.id.payment);
        otherServicesV = findViewById(R.id.other_services);
        otherNotesV = findViewById(R.id.notes);
        openingHoursV = findViewById(R.id.opening_hours);
        contactInfoV = findViewById(R.id.contact_info);

        parkingV = findViewById(R.id.parking);
        appointmentV = findViewById(R.id.appointment);


        categoriesSpinner = findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.sCategories, android.R.layout.simple_spinner_item);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);


        statesSpinner = findViewById(R.id.states_spinner);
        ArrayAdapter<CharSequence> statesAdapter = ArrayAdapter.createFromResource(this,
                R.array.sStates, android.R.layout.simple_spinner_item);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(statesAdapter);

    }


    public void addImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("Image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();
            imageButtonV = findViewById(R.id.image_button);
            imageButtonV.setImageURI(uri);

        }
    }


    public void submitButtonClicked(View view) {
        final String companyNameValue = companyNameV.getText().toString().trim();
        final String addressValue = addressV.getText().toString().trim();
        final String servicesValue = servicesV.getText().toString().trim();
        final String paymentValue = paymentV.getText().toString().trim();
        final String otherServicesValue = otherServicesV.getText().toString().trim();
        final String otherNotesValue = otherNotesV.getText().toString().trim();
        final String openingHoursValue = openingHoursV.getText().toString().trim();
        final String contactInfoValue = contactInfoV.getText().toString().trim();

        final boolean parkingValue = parkingV.isChecked();
        final boolean appointmentValue = appointmentV.isChecked();

        final String categoriesValue = categoriesSpinner.getSelectedItem().toString();
        final String statesValue = statesSpinner.getSelectedItem().toString();

        final String companyRatingAverage = "0.0";
        final int companyRatingTimes = 0;


        if (!TextUtils.isEmpty(companyNameValue) &&
                !TextUtils.isEmpty(addressValue) &&
                !TextUtils.isEmpty(servicesValue) &&
                !TextUtils.isEmpty(contactInfoValue) &&
                !TextUtils.isEmpty(categoriesValue) &&
                !TextUtils.isEmpty(categoriesValue)
                ) {

            final StorageReference imagePath = storageReference.child("company_image").child(uri.getLastPathSegment());
            uploadTask = imagePath.putFile(uri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return imagePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        final Uri downloadUri = task.getResult();
                        final DatabaseReference newCompany = refCompanies.push();

                        refCurrentUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                newCompany.child("imagePath").setValue(downloadUri.toString());
                                newCompany.child("companyName").setValue(companyNameValue);
                                newCompany.child("companyAddress").setValue(addressValue);
                                newCompany.child("companyServices").setValue(servicesValue);
                                newCompany.child("companyOtherServices").setValue(otherServicesValue);
                                newCompany.child("companyNotes").setValue(otherNotesValue);
                                newCompany.child("companyOpeningHours").setValue(openingHoursValue);
                                newCompany.child("companyContactInfo").setValue(contactInfoValue);
                                newCompany.child("companyParking").setValue(parkingValue);
                                newCompany.child("companyPayment").setValue(paymentValue);
                                newCompany.child("companyAppointment").setValue(appointmentValue);
                                newCompany.child("uID").setValue(authCurrentUser.getUid());
                                newCompany.child("companyCategory").setValue(categoriesValue);
                                newCompany.child("companyState").setValue(statesValue);
                                newCompany.child("companyRatingAverage").setValue(companyRatingAverage);
                                newCompany.child("companyRatingTimes").setValue(companyRatingTimes);
                                refCurrentUser.child("my_company_id").setValue(newCompany.getKey());
                                newCompany.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                            Intent mainActivityIntent = new Intent(AddCompanyActivity.this, MainActivity.class);
                                            startActivity(mainActivityIntent);
                                        } else {
                                            Toast.makeText(AddCompanyActivity.this, "Can't save the Information!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        Toast.makeText(AddCompanyActivity.this, "Information saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddCompanyActivity.this, "Please make sure that all Fields with * are not empty!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}