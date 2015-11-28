package com.example.volodymyr.notecase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.volodymyr.notecase.R;
import com.example.volodymyr.notecase.entity.Product;
import com.example.volodymyr.notecase.util.DBHandler;

import java.util.List;

/**
 * Created by volodymyr on 01.11.15.
 */
public class ViewCostsActivity extends Activity {

    public static final String PRODUCT_ID_KEY = "productId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_costs);

        ListView listView = (ListView) findViewById(R.id.costsList);

        DBHandler dbHandler = DBHandler.getDbHandler(this);
        List<Product> productList = dbHandler.getAllProducts();

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewCostsActivity.this, AddEditCategoryActivity.class);
                Product product = (Product)parent.getAdapter().getItem(position);
                intent.putExtra(PRODUCT_ID_KEY, product.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.add_costs:
                Intent addIntent = new Intent(this, AddCostActivity.class);
                startActivity(addIntent);
                break;
            case R.id.manage_category:
                Intent manageCategoryIntent = new Intent(this, ViewCategoryActivity.class);
                startActivity(manageCategoryIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
