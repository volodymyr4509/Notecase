package com.example.volodymyr.notecase.util;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volodymyr.notecase.entity.Product;

/**
 * Created by volodymyr on 15.11.15.
 */
public class MyOnDragListener implements View.OnDragListener {

    private EditText name;
    private EditText price;
    private int category;
    private Context applicationContext;
    private final String EMPTY_STRING = "";

    public MyOnDragListener(EditText name, EditText price, int category, Context applicationContext) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DROP:

                DBHandler dbHandler = new DBHandler(applicationContext, null, null, 1);
                String productName = null;
                double productPrice = 0;
                if (price!=null && price.getText()!=null){
                    productPrice = Double.parseDouble(price.getText().toString());
                }
                if (name!=null){
                    productName = name.getText().toString();
                }
                Product product = new Product(category, 1, productName, productPrice);
                dbHandler.addProduct(product);
                //clean input fields after save
                name.setText(EMPTY_STRING);
                price.setText(EMPTY_STRING);

                Toast.makeText(applicationContext, "Product: " + product.getName() + " was saved...", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public EditText getName() {
        return name;
    }

    public void setName(EditText name) {
        this.name = name;
    }

    public EditText getPrice() {
        return price;
    }

    public void setPrice(EditText price) {
        this.price = price;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        return "MyOnDragListener{" +
                "name=" + name +
                ", price=" + price +
                ", category=" + category +
                ", applicationContext=" + applicationContext +
                '}';
    }
}
