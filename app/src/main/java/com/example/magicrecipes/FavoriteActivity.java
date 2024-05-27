package com.example.magicrecipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }
    // Кнопка выхода из аккаунта
    public void ExtAccBttn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы хотите выйти из аккаунта?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Действие при подтверждении выхода из аккаунта
                logout(); // Метод для выхода из аккаунта
            }
        });
        builder.setNegativeButton("Нет", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void FavouriteButton(View view) {
        Intent intent = new Intent(FavouriteActivity.this, FavouriteActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void CatalogButton(View view) {
        Intent intent = new Intent(FavouriteActivity.this,CatalogActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    // Метод для выхода из аккаунта
    private void logout() {
        // выход из Firebase
        FirebaseAuth.getInstance().signOut();
        // Переход на экран загрузки
        Intent intent = new Intent(FavouriteActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(FavouriteActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }}