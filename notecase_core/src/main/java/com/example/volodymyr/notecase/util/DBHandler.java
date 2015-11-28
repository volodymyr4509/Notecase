package com.example.volodymyr.notecase.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.volodymyr.notecase.entity.Category;
import com.example.volodymyr.notecase.entity.Product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by volodymyr on 25.10.15.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notecase.db";

    private static final String COLUMN_ID = "Id";

    //Product table
    public static final String TABLE_PRODUCT = "product";
    private static final String PRODUCT_CATEGORY = "CategoryId";
    private static final String PRODUCT_USER = "UserId";
    public static final String PRODUCT_NAME = "Name";
    private static final String PRODUCT_PRICE = "Price";
    private static final String PRODUCT_TIMESTAMP = "Created";

    //Category table
    public static final String TABLE_CATEGORY = "category";
    private static final String CATEGORY_NAME = "Name";
    private static final String CATEGORY_COLOR = "Color";

    //User table
    public static final String TABLE_USER = "user";
    private static final String USER_NAME = "Name";
    private static final String USER_EMAIL = "Email";
    private static final String USER_PASSWORD = "Password";

    //Queries
    private static final String CREATE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PRODUCT_USER + " INTEGER, " +
            PRODUCT_CATEGORY + " INTEGER, " +
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_PRICE + " REAL, " +
            PRODUCT_TIMESTAMP + " DATETIME);";

    private static final String CREATE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORY_NAME + " TEXT, " +
            CATEGORY_COLOR + " TEXT);";

    private static final String CREATE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_EMAIL + " TEXT);";



    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT);
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_USER);
        initDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_TIMESTAMP, product.getCreated().toString());
        values.put(PRODUCT_CATEGORY, product.getCategoryId());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public void updateProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());
        values.put(PRODUCT_TIMESTAMP, product.getCreated().toString());
        values.put(PRODUCT_CATEGORY, product.getCategoryId());
        db.update(TABLE_PRODUCT, values, "id"+" = "+product.getId(), null);
        db.close();
    }

    public Product getProductById(int productId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE id = " + productId + ";", null);
        Product product = null;
        if (cursor.moveToNext()) {
            product = new Product();
            product.setId(cursor.getInt(0));
            product.setCategoryId(cursor.getInt(1));
            product.setUserId(cursor.getInt(2));
            product.setName(cursor.getString(3));
            product.setPrice(cursor.getDouble(4));
            Timestamp timestamp = null;
            if (cursor.getString(5) != null) {
                timestamp = Timestamp.valueOf(cursor.getString(5));
            }
            product.setCreated(timestamp);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + ";", null);
        List<Product> products = new ArrayList();
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setUserId(cursor.getInt(1));
            product.setCategoryId(cursor.getInt(2));
            product.setName(cursor.getString(3));
            product.setPrice(cursor.getDouble(4));
            Timestamp timestamp = null;
            if (cursor.getString(5) != null) {
                timestamp = Timestamp.valueOf(cursor.getString(5));
            }
            product.setCreated(timestamp);
            products.add(product);
        }
        return products;
    }

    //TODO check sqlitedb parameter
    public void addCategory(Category category, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());
        values.put(CATEGORY_COLOR, category.getColor());

        db.insert(TABLE_CATEGORY, null, values);
    }

    public List<Category> getAllCategories() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY + ";", null);
        List<Category> categories = new ArrayList();
        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(0));
            category.setName(cursor.getString(1));
            category.setColor(cursor.getString(2));

            categories.add(category);
        }
        return categories;
    }

    private void initDefaultCategories(SQLiteDatabase db){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Food", "ff9966"));
        categoryList.add(new Category("Accommodation", "ee8b84"));
        categoryList.add(new Category("Transport", "6699FF"));
        categoryList.add(new Category("Travel", "114433"));
        categoryList.add(new Category("Dinner", "552233"));
        categoryList.add(new Category("Other", "666633"));

        for (Category category: categoryList){
            addCategory(category, db);
        }
    }


}
