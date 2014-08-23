package com.github.ksoichiro.gag.samples.consumer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.simplealertdialog.SimpleAlertDialogSupportFragment;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });
    }

    private void popup() {
        new SimpleAlertDialogSupportFragment.Builder()
                .setMessage("Hello, world!")
                .create().show(getSupportFragmentManager(), "dialog");
    }

}
