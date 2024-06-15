package com.example.magicrecipes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Recipe implements Parcelable {
    private String name;
    private String description;
    private int imageResourceId;

    public Recipe(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageResourceId = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(imageResourceId);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Статический метод для создания объекта из JSON
    public static Recipe fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Recipe.class);
    }
}

