package com.example.melvincheng.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddProductActivity extends AppCompatActivity {

    EditText name_edit, des_edit, price_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);
        name_edit = (EditText)findViewById(R.id.add_name_edit);
        des_edit = (EditText)findViewById(R.id.add_des_edit);
        price_edit = (EditText)findViewById(R.id.add_price_edit);
    }

    public void save(View v) {
        BrowseProductsActivity.dbHelper.addNewProduct(name_edit.getText().toString(), des_edit.getText().toString(), Float.valueOf(price_edit.getText().toString()));
        clear();
        this.finish();
    }

    public void cancel(View v) {
        clear();
        this.finish();
    }

    private void clear() {
        name_edit.setText("");
        des_edit.setText("");
        price_edit.setText("");
    }
}
