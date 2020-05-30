package io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivitySignUpBinding;


public class SignUpActivity extends BaseActivity
{

    private ActivitySignUpBinding binding;
    private Editable userEmail, userContactNo, userPassword;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean redirected = goHomeIfUserExists();
        if (redirected)
            return;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
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
        binding.buttonSignUp.setOnClickListener(v -> {
            userEmail = binding.editTextEmail.getText();
            userContactNo = binding.editTextPhone.getText();
            userType = getCheckedUserType();
            userPassword = binding.editTextPassword.getText();

            // Toast.makeText(this, "checked = " + binding.radioGroupUserType.getCheckedRadioButtonId(), Toast.LENGTH_LONG).show();

            if (TextUtils.isEmpty(userEmail))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(userContactNo))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(userType))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(userPassword))
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

            else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();

            else if (userPassword.length() < 6)
                Toast.makeText(this, R.string.password_too_short, Toast.LENGTH_SHORT).show();

            else
                ActivityStarter.startMapsActivityForSignUp(
                        this,
                        true,
                        userEmail.toString(),
                        userContactNo.toString(),
                        userType,
                        userPassword.toString()
                );
        });

        binding.buttonLogIn.setOnClickListener(v ->
                ActivityStarter.startLogInActivity(this, false)
        );
    }

    private String getCheckedUserType()
    {
        int checkedRadioButtonId = binding.radioGroupUserType.getCheckedRadioButtonId();
        if (checkedRadioButtonId < 0)
            return "";

        RadioButton radioButton = binding.radioGroupUserType.findViewById(checkedRadioButtonId);
        return radioButton.getText().toString();
    }

}
