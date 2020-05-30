package io.pantheonsite.alphaoptimus369.mrwaste.home_module.views;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.SampleData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.Utils;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityMrWasteHomeBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.adapters.AdapterRvWasteItem;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.ProductItem;


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
                    ProductItem productItem =
                            SampleData.productsMarkedForSelling.get(adapterPosition);

                    AlertDialog alertDialog = new AlertDialog.Builder(MrWasteHomeActivity.this)
                            .setTitle(productItem.name)
                            .setMessage(
                                    Utils.getTotalPriceString(
                                            productItem.singleItemPriceInDollar,
                                            productItem.itemCount
                                    )
                            )
                            .setPositiveButton(
                                    R.string.close,
                                    (dialog, which) -> {}
                                    )
                            .setCancelable(true)
                            .create();
                    alertDialog.setCancelable(true);
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }
        ));
    }

    private void initListeners()
    {
    }

}
