package com.example.magicrecipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CatalogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        mAuth = FirebaseAuth.getInstance();

    }
    // Метод для отображения сообщения пользователю
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Кнопка выхода из аккаунта
    public void ExtAccBttn(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
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
        } else {
            Intent intent = new Intent(CatalogActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    public void FavouriteButton(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(CatalogActivity.this, FavoriteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            // Пользователь не авторизован, показать сообщение или выполнить другие действия
            showMessage("Вы не авторизованы.");
        }
    }

    public void CatalogButton(View view) {
        Intent intent = new Intent(CatalogActivity.this,CatalogActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    // Метод для выхода из аккаунта
    private void logout() {
        // выход из Firebase
        FirebaseAuth.getInstance().signOut();
        // Переход на экран загрузки
        Intent intent = new Intent(CatalogActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(CatalogActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void goToBreakfast(View view) {
        Intent intent = new Intent(CatalogActivity.this, BreakfastActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void goToLunch(View view) {
        Intent intent = new Intent(CatalogActivity.this, LunchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void goToDinner(View view) {
        Intent intent = new Intent(CatalogActivity.this,DinnerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void goToDessert(View view) {
        Intent intent = new Intent(CatalogActivity.this, DessertActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}