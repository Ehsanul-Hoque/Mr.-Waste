package io.pantheonsite.alphaoptimus369.mrwaste.home_module.views;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityHomeBinding;


public class HomeActivity extends BaseActivity
{

    private ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setLifecycleOwner(this);
    }

}
