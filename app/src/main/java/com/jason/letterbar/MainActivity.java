package com.jason.letterbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private LetterSideBar letterSideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
        letterSideBar = findViewById(R.id.letter_bar);

        letterSideBar.setOnSelectListener(new LetterSideBar.OnSelectListener() {
            @Override
            public void onSelect(String letter) {
                textView.setText(letter);
            }
        });
    }
}
