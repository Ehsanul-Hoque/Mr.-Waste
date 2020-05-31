package io.pantheonsite.alphaoptimus369.mrwaste.home_module.views;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.SampleData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMrWasteHomeBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.adapters.AdapterRvWasteItem;


public class MrWasteHomeActivity extends BaseActivity
{

    private ActivityMrWasteHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mr_waste_home);
        binding.setLifecycleOwner(this);

        initComponents();
        initListeners();
    }

    private void initComponents()
    {
        binding.recyclerViewWasteRequests.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        });

        binding.recyclerViewWasteRequests.setAdapter(new AdapterRvWasteItem(
                this,
                SampleData.wasteItems,
                (view, adapterPosition) -> {
                    ActivityStarter.startMrWasteGraphActivity(
                            this,
                            SampleData.wasteItems.get(adapterPosition),
                            false
                    );
                }
        ));
    }

    private void initListeners()
    {
    }

}
