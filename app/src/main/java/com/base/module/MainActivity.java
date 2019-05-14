package com.base.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.base.module.util.GsonUtils;
import com.base.module.util.IntentHelper;
import com.base.module.util.PermissionUtil;
import com.google.gson.reflect.TypeToken;

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
