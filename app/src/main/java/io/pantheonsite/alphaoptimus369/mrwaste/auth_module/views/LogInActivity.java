package io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.ConstantsAndStaticData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.models.UserItem;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityLogInBinding;


public class LogInActivity extends BaseActivity
{

    private ActivityLogInBinding binding;
    private Editable userEmail, userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean redirected = goHomeIfUserExists();
        if (redirected)
            return;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);
        binding.setLifecycleOwner(this);

        initListeners();
    }

    private boolean goHomeIfUserExists()
    {
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            ActivityStarter.startHomeActivity(this, true);
            return true;
        }

        return false;
    }

    private void initListeners()
    {
        binding.buttonLogIn.setOnClickListener(v -> {
            userEmail = binding.editTextEmail.getText();
            userPassword = binding.editTextPassword.getText();

            if (TextUtils.isEmpty(userEmail))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(userPassword))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();

            else
                getUserFromFirebase();
        });

        binding.buttonSignUp.setOnClickListener(v ->
                ActivityStarter.startSignUpActivity(this, false)
        );
    }

    private void getUserFromFirebase()
    {
        showProgressDialog();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(userEmail.toString(), userPassword.toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ConstantsAndStaticData.LOG_TAG, "signInWithEmail:success");
                            getInfoFromFirestore();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ConstantsAndStaticData.LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, R.string.auth_failed_no_net_or_not_reg,
                                    Toast.LENGTH_LONG).show();
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void getInfoFromFirestore()
    {
        hideProgressDialog();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String documentId = userEmail.toString();

        // Add a new document with a generated ID
        db.collection("users")
                .document(documentId)
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        hideProgressDialog();

                        Log.d(ConstantsAndStaticData.LOG_TAG, "getInfoFromFirestore:success. DocumentSnapshot received with ID: " + documentId);

                        if (documentSnapshot != null) {
                            Double latitude = documentSnapshot.getDouble("latitude");
                            Double longitude = documentSnapshot.getDouble("longitude");

                            if (latitude == null) latitude = 0.0;
                            if (longitude == null) longitude = 0.0;

                            ConstantsAndStaticData.currentUser = new UserItem(
                                    documentSnapshot.getString("email"),
                                    documentSnapshot.getString("contact"),
                                    documentSnapshot.getString("type"),
                                    documentSnapshot.getString("address"),
                                    latitude,
                                    longitude
                            );

                            ActivityStarter.startHomeActivity(LogInActivity.this, true);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseAuth.getInstance().signOut();
                        ConstantsAndStaticData.currentUser = null;

                        Log.w(ConstantsAndStaticData.LOG_TAG, "getInfoFromFirestore:failure", e);
                        Toast.makeText(LogInActivity.this, R.string.auth_failed_no_net_or_not_reg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
