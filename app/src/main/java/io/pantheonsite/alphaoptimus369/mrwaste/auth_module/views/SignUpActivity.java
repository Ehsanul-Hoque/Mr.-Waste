package io.pantheonsite.alphaoptimus369.mrwaste.auth_module.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivitySignUpBinding;


public class SignUpActivity extends AppCompatActivity
{

    private ActivitySignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setLifecycleOwner(this);

        initListeners();
    }

    private void initListeners()
    {
        binding.buttonSignUp.setOnClickListener(v ->
                ActivityStarter.startMapsActivity(this, true)
        );

        binding.buttonLogIn.setOnClickListener(v ->
                ActivityStarter.startLogInActivity(this, false)
        );
    }

}
