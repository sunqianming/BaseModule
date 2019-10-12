package com.base.module;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.base.module.util.IntentHelper;
import com.base.module.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkPermission(this);
        TextView content = findViewById(R.id.tv_view);
        content.setText(IntentHelper.getCurrentLauncherPackageName(this));
    }
}
