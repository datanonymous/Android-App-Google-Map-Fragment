package com.example.alex.javagooglemapapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.alex.javagooglemapapplication.R.id.euroDollarsTextView;
import static com.example.alex.javagooglemapapplication.R.id.yenDollarsTextView;

public class MainActivity2 extends AppCompatActivity {

//    public void buttonPressed(View view){
//        //Toast.makeText(MapsActivity.this,"Button Pressed",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//        startActivity(intent);
//    }

    public void convertFunction(View view){
        EditText yenEditText = (EditText) findViewById(R.id.yenEditText);
        EditText euroEditText = (EditText) findViewById(R.id.euroEditText);

        Double yenAmountDouble = 0.0;
        Double euroAmountDouble = 0.0;

        if(yenEditText.length()>0){
            yenAmountDouble = Double.parseDouble(yenEditText.getText().toString());
        } else{
            yenAmountDouble = 0.0;
        }
        if(euroEditText.length()>0){
            euroAmountDouble = Double.parseDouble(euroEditText.getText().toString());
        } else{
            euroAmountDouble = 0.0;
        }

//        yenAmountDouble = Double.parseDouble(yenEditText.getText().toString());
//        euroAmountDouble = Double.parseDouble(euroEditText.getText().toString());

//        if(yenAmountDouble == ""){
//            yenAmountDouble = 0.0;
//            return;
//        } else{
//            Double yenDollarAmount = yenAmountDouble * 0.0089;
//            TextView yenDollarsTextView = (TextView) findViewById(yenDollarsTextView);
//        }
//
//        if(euroAmountDouble == ""){
//            euroAmountDouble = 0.0;
//            return;
//        } else{
//            Double euroDollarAmount = euroAmountDouble * 1.15;
//            TextView euroDollarsTextView = (TextView) findViewById(euroDollarsTextView);
//        }



        Double yenDollarAmount = yenAmountDouble * 0.0089;
        Double euroDollarAmount = euroAmountDouble * 1.15;

        TextView yenDollarsTextView = (TextView) findViewById(R.id.yenDollarsTextView);
        TextView euroDollarsTextView = (TextView) findViewById(R.id.euroDollarsTextView);

        yenDollarsTextView.setText("Dollars $ = " + yenDollarAmount);
        euroDollarsTextView.setText("Dollars $ =  " + euroDollarAmount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
