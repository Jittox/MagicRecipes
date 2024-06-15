package com.example.magicrecipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        mAuth = FirebaseAuth.getInstance();

        if (isFirstTime()) {
            showInstructionsDialog();
        }
        // Загружаем избранные рецепты
        loadFavoriteRecipes();

    }

    // Отображение инструкции для избранного
    private void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Инструкции");
        builder.setMessage("Удерживайте понравившийся рецепт для добавления в избранное." +
                "\n\n\n\nПри удерживании избранного рецепта он будет удален из избранного.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Проверка первого захода в избранное
    private boolean isFirstTime() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }
        return isFirstTime;
    }

    // Загружаем избранные рецепты
    private void loadFavoriteRecipes() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String recipeName = entry.getKey();
            Recipe recipe = getRecipeByName(recipeName);
            if (recipe != null) {
                addRecipeToScrollView(recipe);
            }
        }
    }


    private Recipe getRecipeByName(String recipeName) {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(recipeName, null);
        if (json != null) {
            return Recipe.fromJson(json);
        }
        return null;
    }



    // Загружаем рецепты в скроллвью
    private void addRecipeToScrollView(final Recipe recipe) {
        LinearLayout layout = findViewById(R.id.linearLayout);

        // Создаем родительский макет для изображения и текста
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        // Создаем новый ImageView и устанавливаем параметры
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Ширина
                LinearLayout.LayoutParams.WRAP_CONTENT  // Высота
        );
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(recipe.getImageResourceId());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);

        // Создаем новый TextView и устанавливаем параметры
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(0, 16, 0, 0); // Добавляем верхний отступ
        textView.setLayoutParams(textParams);
        textView.setText(recipe.getName());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Добавляем обработчик нажатия на изображение
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем рецепт
                openRecipe(recipe);
            }
        });

        // Добавляем обработчик долгого нажатия на изображение
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Создаем диалоговое окно для подтверждения удаления
                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
                builder.setTitle("Удаление рецепта");
                builder.setMessage("Вы уверены, что хотите удалить этот рецепт из избранного?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Удаляем рецепт из избранного
                        removeFromFavorites(recipe.getName());

                        // Удаляем изображение и текст из ScrollView
                        LinearLayout layout = findViewById(R.id.linearLayout);
                        layout.removeView(itemLayout);
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ничего не делаем, закрываем диалоговое окно
                        dialog.dismiss();
                    }
                });
                // Показываем диалоговое окно
                builder.create().show();

                return true; // Возвращаем true, чтобы обработать событие длительного нажатия
            }
        });

        // Добавляем изображение и текст в родительский макет
        itemLayout.addView(imageView);
        itemLayout.addView(textView);

        // Добавляем родительский макет в ScrollView
        layout.addView(itemLayout);
    }




    // Открыть рецепт
    private void openRecipe(Recipe recipe) {
        // Открываем рецепт
        Intent intent = new Intent(FavoriteActivity.this, ProfileRecipe.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // Удалить из избранного
    private void removeFromFavorites(String recipeName) {
        // Удаляем рецепт из SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(recipeName);
        editor.apply();

        // Удаляем изображение и текст из ScrollView
        LinearLayout layout = findViewById(R.id.linearLayout);
        View viewToRemove = layout.findViewWithTag(recipeName); // Используйте тег для поиска соответствующего представления
        if (viewToRemove != null) {
            layout.removeView(viewToRemove);
        }

        Toast.makeText(FavoriteActivity.this, "Рецепт удален.", Toast.LENGTH_SHORT).show();
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
            // Пользователь не авторизован, показать сообщение или выполнить другие действия
            showMessage("Вы не авторизованы.");
        }
    }

    public void FavouriteButton(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            Intent intent = new Intent(FavoriteActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();

        }
    }

    public void CatalogButton(View view) {
        if (isInternetAvailable()) {
            Intent intent = new Intent(FavoriteActivity.this, CatalogActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            showNoInternetMessage();
        }
    }

    public void BackToMain(View view) {
        if (isInternetAvailable()) {
            Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            showNoInternetMessage();
        }
    }

    // Метод для проверки подключения к интернету
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // Метод для отображения сообщения об отсутствии интернета
    private void showNoInternetMessage() {
        Toast.makeText(this, "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
    }

    // Метод для выхода из аккаунта
    private void logout() {
        // выход из Firebase
        FirebaseAuth.getInstance().signOut();
        // Переход на экран загрузки
        Intent intent = new Intent(FavoriteActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    }