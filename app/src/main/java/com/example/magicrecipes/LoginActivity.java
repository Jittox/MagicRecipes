package com.example.magicrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
    }
    public void loginAsGuest(View view) {

    showMessage("Гостевой вход выполнен.");
    navigateToMainActivity();

    }

    public void loginUser(View view) {
        // Получаем введенные пользователем данные
        String email = getEmail();
        String password = getPassword();

        // Проверяем, что email и пароль не пустые
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            // Если email или пароль пусты, выводим сообщение об ошибке
            showMessage("Пожалуйста, введите адрес электронной почты и пароль");
            return;
        }

        // Выполняем попытку входа в аккаунт Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Сохраняем состояние пользователя после успешной авторизации
                            saveUserState();
                            // Если авторизация успешна, переходим на главный экран
                            showMessage("Авторизация успешна.");
                            navigateToMainActivity();
                        } else {
                            // Если авторизация не удалась, выводим сообщение об ошибке
                            showMessage("Ошибка авторизации: " + task.getException().getMessage());
                        }
                    }
                });
    }

    // Метод для получения введенного пользователем email
    private String getEmail() {
        return emailEditText.getText().toString().trim();
    }

    // Метод для получения введенного пользователем пароля
    private String getPassword() {
        return passwordEditText.getText().toString().trim();
    }

    // Метод для отображения сообщения пользователю
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Метод для перехода на главный экран
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }


    public void goToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Проверяем состояние пользователя при запуске приложения
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Пользователь уже авторизован, проверяем подключение к интернету
            if (isInternetAvailable()) {
                navigateToMainActivity();
            } else {
                navigateToFavoriteActivity();
            }
        }
    }
    // Метод для перехода на экран избранного
    private void navigateToFavoriteActivity() {
        Intent intent = new Intent(LoginActivity.this, FavoriteActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    // Метод для проверки подключения к интернету
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    private void saveUserState() {
        // Здесь вы сохраняете состояние пользователя, например, его идентификатор или другие данные,
        // которые позволят вам идентифицировать пользователя в будущем.
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.putString("userId", mAuth.getCurrentUser().getUid());
        editor.apply();
    }

}

