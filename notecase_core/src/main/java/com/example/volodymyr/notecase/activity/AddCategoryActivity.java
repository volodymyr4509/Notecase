package com.example.volodymyr.notecase.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.volodymyr.notecase.R;
import com.example.volodymyr.notecase.entity.Category;
import com.example.volodymyr.notecase.util.DBHandler;

/**
 * Created by volodymyr on 22.11.15.
 */
public class AddCategoryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        final EditText categoryName = (EditText) findViewById(R.id.category_name);
        SeekBar colorSeekBar = (SeekBar) findViewById(R.id.color_picker);
        final View resultColor = (View) findViewById(R.id.result_color);
        resultColor.setBackgroundColor(Color.BLACK);
        Button saveButton = (Button) findViewById(R.id.save_category);

        colorSeekBar.setMax(256*7-1);
        colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256){
                        b = progress;
                    } else if(progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*3) {
                        g = 255;
                        b = progress%256;
                    } else if(progress < 256*4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if(progress < 256*6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }

                    resultColor.setBackgroundColor(Color.argb(255, r, g, b));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCategoryName = categoryName.getText().toString();
                int newCategoryColor = resultColor.getSolidColor();
                if (newCategoryName!=null){
                    Category category = new Category(newCategoryName, newCategoryColor);
                    DBHandler dbHandler = DBHandler.getDbHandler(getApplicationContext());
                    dbHandler.addCategory(category);
                    Toast.makeText(getApplicationContext(), "Category saved", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }
}
