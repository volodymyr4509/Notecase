package com.example.volodymyr.notecase.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.volodymyr.notecase.MyDragShadowBuilder;
import com.example.volodymyr.notecase.R;
import com.example.volodymyr.notecase.entity.Category;
import com.example.volodymyr.notecase.util.DBHandler;
import com.example.volodymyr.notecase.util.MyOnDragListener;

import java.util.List;

public class AddCostActivity extends Activity {

    private final String DOT = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        AutoCompleteTextView nameInput = (AutoCompleteTextView) findViewById(R.id.commodityName);
        final EditText priceInput = (EditText) findViewById(R.id.commodityPrice);

        LinearLayout left_block = (LinearLayout) findViewById(R.id.left_category_block);
        LinearLayout right_block = (LinearLayout) findViewById(R.id.right_category_block);
        left_block.setPadding(0, 20, 50, 20);
        right_block.setPadding(50, 20, 0, 20);
        right_block.setGravity(Gravity.RIGHT);
        left_block.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);
        params.width = 150;
        params.height = 150;

        DBHandler dbHandler = DBHandler.getDbHandler(this);
        List<Category> categoryList = dbHandler.getAllCategories();

        for (int i = 0; i < categoryList.size(); i++) {
            Button categoryButton = new Button(this);
            Category category = categoryList.get(i);
            categoryButton.setText(category.getName());
            categoryButton.setBackgroundColor(category.getColor());
            categoryButton.setPadding(20, 20, 20, 20);
            categoryButton.setLayoutParams(params);
            categoryButton.setId(category.getId());
            if (i % 2 == 0) {
                left_block.addView(categoryButton);
            } else {
                right_block.addView(categoryButton);
            }
            categoryButton.setOnDragListener(new MyOnDragListener(nameInput, priceInput, category.getId(), this));
        }

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Button moveButton = (Button) findViewById(R.id.move_button);
        final Activity activity = this;

        moveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard(activity);
                ClipData clipData = ClipData.newPlainText("mylabel", "mytext");
                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(linearLayout);
                v.startDrag(clipData, myShadow, null, 0);
                return false;
            }
        });

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String currInput = priceInput.getText().toString() + source;
                if (currInput.equals(".")) {
                    return "0.";
                }
                //close keyboard with .12 precision
                int dotIndex = currInput.indexOf(DOT);
                if (dotIndex != -1 && currInput.length() - dotIndex > 2) {
                    closeKeyboard(activity);
                }
                return null;
            }
        };
        priceInput.setFilters(new InputFilter[]{filter});

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
        switch (id) {
            case R.id.view_costs:
                Intent viewCostIntent = new Intent(this, ViewCostsActivity.class);
                startActivity(viewCostIntent);
                break;
            case R.id.manage_category:
                Intent manageCategoryIntent = new Intent(this, ViewCategoryActivity.class);
                startActivity(manageCategoryIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
