package io.pantheonsite.alphaoptimus369.mrwaste.commons.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.pantheonsite.alphaoptimus369.mrwaste.R;


abstract public class BaseActivity extends AppCompatActivity
{

    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onPause() {
        hideProgressDialog();
        super.onPause();
    }

    @Override
    protected void onStop() {
        hideProgressDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }


    protected void showProgressDialog()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.working));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    protected void hideProgressDialog()
    {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.hide();

            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
