package com.example.volodymyr.notecase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volodymyr.notecase.R;
import com.example.volodymyr.notecase.entity.Category;
import com.example.volodymyr.notecase.entity.Product;
import com.example.volodymyr.notecase.util.DBHandler;

import java.util.List;

/**
 * Created by volodymyr on 22.11.15.
 */
public class EditCostActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cost_details);

        Intent intent = getIntent();
        int productId = intent.getIntExtra(ViewCostsActivity.PRODUCT_ID_KEY, -1);
        final DBHandler dbHandler = DBHandler.getDbHandler(this);
        final Product product = dbHandler.getProductById(productId);
        List<Category> categories = dbHandler.getAllCategories();


        final EditText name = (EditText) findViewById(R.id.editCommodityName);
        final EditText price = (EditText) findViewById(R.id.editCommodityPrice);
        TextView dateTime = (TextView) findViewById(R.id.dateTime);
        final Spinner categorySelector = (Spinner) findViewById(R.id.editCategory);
        Button saveButton = (Button) findViewById(R.id.save_button);

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        dateTime.setText(product.getCreated().toString());

        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categorySelector.setAdapter(dataAdapter);
        //set default category
        int oldCategoryIndex = -1;
        for (int i = 0; i<categories.size(); i++){
            if (categories.get(i).getId() == product.getCategoryId()){
                oldCategoryIndex = i;
            }
        }
        categorySelector.setSelection(oldCategoryIndex);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String newName = name.getText().toString().trim();
                    double newPrice = Double.parseDouble(price.getText().toString().trim());
                    product.setName(newName);
                    product.setPrice(newPrice);
                    Category selectedCategory = (Category)categorySelector.getSelectedItem();
                    if (selectedCategory != null){
                        product.setCategoryId(selectedCategory.getId());
                    }
                }catch (RuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Incorrect input", Toast.LENGTH_LONG).show();
                }
                dbHandler.updateProduct(product);
                finish();
            }
        });


    }
}
