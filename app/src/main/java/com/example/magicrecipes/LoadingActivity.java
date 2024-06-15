package com.example.magicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

            // переходим на главный экран через 2.5 секунды
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Переход к основному экрану вашего приложения
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, 2000);
        }

}
