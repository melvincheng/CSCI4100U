package com.example.melvincheng.a2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrowseProductsActivity extends AppCompatActivity {

    public static ProductDBHelper dbHelper;
    private List<Product> productList;
    private int item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_products_activity);

        dbHelper = new ProductDBHelper(this);
        item = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDb();
        check();
        if(productList.size() != 0) {
            showProduct(productList.get(item));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_products_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Checks if there are items before and after the current item
    //to determine whether to enable the buttons
    //Also checks if there are items in the list
    //to determine whether to enable the delete button
    private void check() {
        Button next_btn = (Button)findViewById(R.id.next_btn);
        Button prev_btn = (Button)findViewById(R.id.prev_btn);
        Button del_btn = (Button)findViewById(R.id.delete_btn);
        if(productList.size() == 0) {
            del_btn.setEnabled(false);
            next_btn.setEnabled(false);
            prev_btn.setEnabled(false);
            return;
        }
        del_btn.setEnabled(true);
        if(item == productList.size() - 1) {
            next_btn.setEnabled(false);
        } else {
            next_btn.setEnabled(true);
        }
        if(item == 0) {
            prev_btn.setEnabled(false);
        } else {
            prev_btn.setEnabled(true);
        }
    }

    //populates the edit texts
    //executes the async task to convert the prices
    private void showProduct(Product product) {

        new Convert().execute();

        EditText name_edit = (EditText)findViewById(R.id.name_edit);
        EditText des_edit = (EditText)findViewById(R.id.des_edit);
        EditText cad_edit = (EditText)findViewById(R.id.priceCad_edit);

        name_edit.setText(product.getName());
        des_edit.setText(product.getDescription());
        cad_edit.setText(Float.toString(product.getPrice()));
    }

    //Async task used to convert the price from CAD to BitCoin
    //onPostExecute is used to populate the BitCoin edit text
    private class Convert extends AsyncTask<Void, Void, String> {
        private Exception exception = null;
        @Override
        protected String doInBackground(Void... Params) {
            int result;
            HttpURLConnection conn;
            URL url;
            String btc = "0.0";
            try {
                String link = "https://blockchain.info/tobtc?currency=CAD&value=" + productList.get(item).getPrice();
                url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                conn.getResponseCode();
                result = conn.getResponseCode();
                if (result == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    btc = in.readLine();
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return btc;
        }

        protected void onPostExecute(String result) {
            if (exception != null) {
                exception.printStackTrace();
            }
            EditText bit_edit = (EditText)findViewById(R.id.priceBit_edit);
            bit_edit.setText(result);
        }
    }

    public void delete(View v) {
        dbHelper.deleteProductById(productList.get(item).getProductId());
        loadDb();
        item = 0;
        check();
        if (productList.size() != 0) {
            showProduct(productList.get(item));
        } else {
            clear();
        }

    }

    public void next(View v) {
        showProduct(productList.get(++item));
        check();
    }

    public void prev(View v) {
        showProduct(productList.get(--item));
        check();
    }

    public void addProduct(MenuItem item) {
        Intent intent = new Intent(this, AddProductActivity.class);
        this.startActivity(intent);
    }

    private void clear() {
        EditText name_edit = (EditText)findViewById(R.id.name_edit);
        EditText des_edit = (EditText)findViewById(R.id.des_edit);
        EditText cad_edit = (EditText)findViewById(R.id.priceCad_edit);
        EditText bit_edit = (EditText)findViewById(R.id.priceBit_edit);

        name_edit.setText("");
        des_edit.setText("");
        cad_edit.setText("");
        bit_edit.setText("");
    }

    private void loadDb() {
        productList = dbHelper.getAllProduct();
    }
}
