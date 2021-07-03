package com.example.helloandroid.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloandroid.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class calculator_fragment extends Fragment {
    View inflate;
    Context context;
    TextView exprTextView, resultTextView;

    // Number buttons for inputting numbers
    Button one, two, three, four, five, six, seven;
    Button eight, nine, zero, dot, double_zero, del, ce;
    Button mod,div,mul,sub,add,equal;

    public calculator_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_calculator_fragment, container, false);
        context = getActivity();

        initializeButtons();
        handleButtonClicks();
        exprTextView = inflate.findViewById(R.id.textView1);
        resultTextView = inflate.findViewById(R.id.textView2);

        return inflate;
    }

    /**
     * Helper method to bind all the buttons in the fragment view to a Button object
     */
    private void initializeButtons() {
        one = inflate.findViewById(R.id.button1);
        two = inflate.findViewById(R.id.button2);
        three = inflate.findViewById(R.id.button3);
        four = inflate.findViewById(R.id.button4);
        five = inflate.findViewById(R.id.button5);
        six = inflate.findViewById(R.id.button6);
        seven = inflate.findViewById(R.id.button7);
        eight = inflate.findViewById(R.id.button8);
        nine = inflate.findViewById(R.id.button9);
        zero = inflate.findViewById(R.id.button0);
        double_zero = inflate.findViewById(R.id.button00);
        dot = inflate.findViewById(R.id.button_dot);
        ce = inflate.findViewById(R.id.button_c);
        del = inflate.findViewById(R.id.button_x);
        div = inflate.findViewById(R.id.button_div);
        mul = inflate.findViewById(R.id.button_mul);
        mod = inflate.findViewById(R.id.button_mod);
        sub = inflate.findViewById(R.id.button_minus);
        equal = inflate.findViewById(R.id.button_eq);
        add = inflate.findViewById(R.id.button_add);
    }

    @SuppressLint("SetTextI18n")
    private void handleButtonClicks(){
        // onClick() Handlers for each of the button widgets so they can respond to click events
        one.setOnClickListener(v -> addToTextView("1",true));
        two.setOnClickListener(v -> addToTextView("2",true));
        three.setOnClickListener(v -> addToTextView("3",true));
        four.setOnClickListener(v -> addToTextView("4",true));
        five.setOnClickListener(v -> addToTextView("5",true));
        six.setOnClickListener(v -> addToTextView("6",true));
        seven.setOnClickListener(v -> addToTextView("7",true));
        eight.setOnClickListener(v -> addToTextView("8",true));
        nine.setOnClickListener(v -> addToTextView("9",true));
        dot.setOnClickListener(v -> addToTextView(".",true));
        zero.setOnClickListener(v -> addToTextView("0",true));
        double_zero.setOnClickListener(v -> addToTextView("00",true));
        del.setOnClickListener(v -> {
            String text = exprTextView.getText().toString();
            int length = text.length();
            if (!text.isEmpty()) exprTextView.setText(text.substring(0,length-1));
            resultTextView.setText("");
        });

        div.setOnClickListener(v -> addToTextView("/",false));
        mod.setOnClickListener(v -> addToTextView("%",false));
        mul.setOnClickListener(v -> addToTextView("*",false));
        add.setOnClickListener(v -> addToTextView("+",false));
        sub.setOnClickListener(v -> addToTextView("-",false));
        ce.setOnClickListener(v -> {
            resultTextView.setText("");
            exprTextView.setText("");
        });
        equal.setOnClickListener(v -> {
            try {
                Expression expression = new ExpressionBuilder(exprTextView.getText().toString()).build();
                double result = expression.evaluate();
                long intResult = (long) result;
                if (result == intResult) resultTextView.setText(Long.toString(intResult));
                else resultTextView.setText(Double.toString(result));
            } catch (Exception e) {
                Toast.makeText(context, "Invalid Operation!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToTextView(String str,Boolean canClear) {
        if (!resultTextView.getText().toString().isEmpty()) exprTextView.setText("");
        if (canClear){
            resultTextView.setText("");
            exprTextView.append(str);
        }else {
            exprTextView.append(resultTextView.getText());
            exprTextView.append(str);
            resultTextView.setText("");
        }
    }
}