package io.pantheonsite.alphaoptimus369.mrwaste.home_module.views;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.Constants;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.data.SampleData;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.ActivityStarter;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.Utils;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.views.BaseActivity;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.ActivityHomeBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.LayoutDialogBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.adapters.AdapterRvProductItem;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.ProductItem;


public class HomeActivity extends BaseActivity
{

    private ActivityHomeBinding binding;
    private boolean activityPaused = false;
    // Set to true ensures requestInstall() triggers installation if necessary.
    private boolean mUserRequestedInstall = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setLifecycleOwner(this);

        initComponents();
        initListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            int min = Math.min(permissions.length, grantResults.length);
            boolean allGranted = true;

            for (int i = 0; i < min; ++i) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
            }

            if (allGranted) {
                ActivityStarter.startAugmentedImageActivity(this, false);

            } else {
                Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityPaused = false;
        // maybeEnableArButton();
    }

    @Override
    protected void onPause() {
        activityPaused = true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        activityPaused = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        activityPaused = true;
        super.onDestroy();
    }

    /*private void maybeEnableArButton()
    {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            if (!activityPaused) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!activityPaused)
                            maybeEnableArButton();
                    }
                }, 1000);
            }
        }
        if (availability.isSupported()) {
            binding.setArAvailable(true);
            Log.d(Constants.LOG_TAG, "maybeEnableArButton: ArCoreApk.getInstance().checkAvailability(this).isSupported() = true");

        } else { // Unsupported or unknown.
            binding.setArAvailable(false);
            Log.w(Constants.LOG_TAG, "maybeEnableArButton: ArCoreApk.getInstance().checkAvailability(this).isSupported() = false");
        }
    }*/

    private void checkArServices()
    {
        // Make sure Google Play Services for AR is installed and up to date.
        try {
            switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                case INSTALLED:
                    checkForCameraPermissionAndGoToAR();
                    break;

                case INSTALL_REQUESTED:
                    // Ensures next invocation of requestInstall() will either return
                    // INSTALLED or throw an exception.
                    mUserRequestedInstall = false;
                    return;
            }

        } catch (UnavailableUserDeclinedInstallationException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "Google Play Services for AR could not be installed.", Toast.LENGTH_LONG).show();
            Log.e(Constants.LOG_TAG, "checkArServices: ", e);
            return;

        } catch (UnavailableDeviceNotCompatibleException e) {
            Toast.makeText(this, "Google Play Services for AR could not be installed.", Toast.LENGTH_LONG).show();
            Log.e(Constants.LOG_TAG, "checkArServices: ", e);
        }
    }

    private void initComponents()
    {
        binding.setArAvailable(false /*Build.VERSION.SDK_INT >= 24*/);

        binding.recyclerViewProductsRequested.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        });
        binding.recyclerViewProductsPrices.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        });

        binding.recyclerViewProductsRequested.setNestedScrollingEnabled(false);
        binding.recyclerViewProductsPrices.setNestedScrollingEnabled(false);

        binding.recyclerViewProductsRequested.setAdapter(new AdapterRvProductItem(
                this,
                SampleData.productsMarkedForSelling,
                true,
                (view, adapterPosition) -> {
                    ProductItem productItem =
                            SampleData.productsMarkedForSelling.get(adapterPosition);

                    AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
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

        binding.recyclerViewProductsPrices.setAdapter(new AdapterRvProductItem(
                this,
                SampleData.productsPrices,
                false,
                (view, adapterPosition) -> {
                    ProductItem productItem =
                            SampleData.productsPrices.get(adapterPosition);

                    LayoutDialogBinding dialogBinding = LayoutDialogBinding.inflate(LayoutInflater.from(this));
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(dialogBinding.getRoot());

                    dialogBinding.setTitle(productItem.name);
                    dialogBinding.setBody(
                            Utils.getTotalPriceString(
                                    productItem.singleItemPriceInDollar,
                                    productItem.itemCount
                            )
                    );
                    dialogBinding.setButtonOneText("-1");
                    dialogBinding.setButtonTwoText("+1");
                    dialogBinding.setButtonThreeText(getString(R.string.save));

                    dialogBinding.buttonOne.setOnClickListener(v -> {
                        if (productItem.itemCount > 1) {
                            --productItem.itemCount;
                            dialogBinding.setBody(
                                    Utils.getTotalPriceString(
                                            productItem.singleItemPriceInDollar,
                                            productItem.itemCount
                                    )
                            );
                        }
                    });

                    dialogBinding.buttonTwo.setOnClickListener(v -> {
                        ++productItem.itemCount;
                        dialogBinding.setBody(
                                Utils.getTotalPriceString(
                                        productItem.singleItemPriceInDollar,
                                        productItem.itemCount
                                )
                        );
                    });

                    dialogBinding.buttonThree.setOnClickListener(v -> {
                        SampleData.productsMarkedForSelling.add(productItem);
                        SampleData.productsPrices.remove(productItem);

                        if (binding.recyclerViewProductsPrices.getAdapter() != null)
                            binding.recyclerViewProductsPrices.getAdapter().notifyDataSetChanged();

                        if (binding.recyclerViewProductsRequested.getAdapter() != null)
                            binding.recyclerViewProductsRequested.getAdapter().notifyDataSetChanged();

                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                    });

                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();

                    if (dialog.getWindow() != null) {
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);
                    }
                }
        ));
    }

    private void initListeners()
    {
        binding.linearLayoutArButton.setOnClickListener(v -> {
            checkArServices();
        });
    }

    private void checkForCameraPermissionAndGoToAR()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.CAMERA
                    },
                    Constants.REQUEST_CODE_CAMERA
            );

        } else {
            ActivityStarter.startAugmentedImageActivity(this, false);
        }
    }

}
