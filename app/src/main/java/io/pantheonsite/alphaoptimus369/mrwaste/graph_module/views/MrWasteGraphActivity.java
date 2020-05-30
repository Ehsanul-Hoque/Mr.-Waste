package io.pantheonsite.alphaoptimus369.mrwaste.graph_module.views;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMrWasteGraphBinding;


public class MrWasteGraphActivity extends BaseActivity
{

    private ActivityMrWasteGraphBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mr_waste_graph);
        binding.setLifecycleOwner(this);

        initComponents();
    }

    private void initComponents()
    {
    }

}
