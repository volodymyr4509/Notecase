package com.example.volodymyr.notecase.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.example.volodymyr.notecase.util.MyOnDragListener;

import java.util.IllegalFormatCodePointException;

public class AddCostActivity extends Activity {

    private final String DOT = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        AutoCompleteTextView nameInput = (AutoCompleteTextView) findViewById(R.id.commodityName);
        final EditText priceInput = (EditText) findViewById(R.id.commodityPrice);

        Button categoryFood = (Button) findViewById(R.id.button_food);
        Button categoryAccommodation = (Button) findViewById(R.id.button_accommodation);
        Button categoryTransport = (Button) findViewById(R.id.button_transport);
        Button categoryTravel = (Button) findViewById(R.id.button_travel);
        Button categoryDinner = (Button) findViewById(R.id.button_dinner);
        Button categoryOther = (Button) findViewById(R.id.button_other);

        categoryFood.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 1, this));
        categoryAccommodation.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 2, this));
        categoryTransport.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 3, this));
        categoryTravel.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 4, this));
        categoryDinner.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 5, this));
        categoryOther.setOnDragListener(new MyOnDragListener(nameInput, priceInput, 6, this));


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

    public void closeKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
