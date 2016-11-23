package com.example.melvincheng.a2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_FILENAME = "products.db";

    private static final String CREATE_STATEMENT = "" +
            "create table products(" +
            " productId integer primary key autoincrement," +
            " name varchar(100) not null," +
            " description varchar(100) not null," +
            " price decimal not null)";

    private static final String DROP_STATEMENT = "" +
            "drop table products";

    public ProductDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_STATEMENT);
        db.execSQL(DROP_STATEMENT);
    }

    public void deleteProductById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("products", "productId = ?", new String[] {"" + id});
    }

    public Product addNewProduct(String name, String description, float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        long id = db.insert("products", null, values);

        Product product = new Product(id, name, description, price);
        return product;
    }

    public List<Product> getAllProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> results = new ArrayList<>();

        String[] columns = new String[] {
                "productId",
                "name",
                "description",
                "price"
        };
        String where = "";
        String[] whereArgs = new String[] {};
        String groupBy = "";
        String groupArgs = "";
        String orderBy = "productId";
        Cursor cursor = db.query("products", columns, where, whereArgs, groupBy, groupArgs, orderBy);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            float price = cursor.getFloat(3);
            results.add(new Product(id, name, description, price));
            cursor.moveToNext();
        }
        return results;
    }
}
