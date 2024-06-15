package com.example.magicrecipes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class ProfileRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_dinner);
        Recipe recipe = getIntent().getParcelableExtra("recipe");
        TextView nameTextView = findViewById(R.id.recipeTitle);
        TextView descriptionTextView = findViewById(R.id.recipeText);
        ImageView previewImageView = findViewById(R.id.recipeImage);

        nameTextView.setText(recipe.getName());
        descriptionTextView.setText(recipe.getDescription());
        previewImageView.setImageResource(recipe.getImageResourceId());

    }

    public void goBack1(View view) {

            onBackPressed(); // Вызываем стандартное поведение кнопки "назад"


    }
}