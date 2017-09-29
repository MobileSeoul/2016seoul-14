package com.seoulapp.ssg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seoulapp.ssg.R;

/**
 * Created by Dongyoon on 2016. 10. 29..
 */

public class CustomLoginButton extends LinearLayout {

    LinearLayout bg;
    ImageView symbol;
    TextView text;

    public CustomLoginButton(Context context) {
        super(context);
        initView();
    }

    public CustomLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.widget_login_button, this, false);
        addView(v);

        bg = (LinearLayout) findViewById(R.id.bg);
        symbol = (ImageView) findViewById(R.id.symbol);

        text = (TextView) findViewById(R.id.text);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton, defStyle, 0);
        setTypeArray(typedArray);

    }

    private void setTypeArray(TypedArray typedArray) {


        int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.login_kakao_bg);
        bg.setBackgroundResource(bg_resID);

        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, R.drawable.ic_kakao_symbol);
        symbol.setImageResource(symbol_resID);

        int textColor = typedArray.getColor(R.styleable.LoginButton_textColor, 0);
        text.setTextColor(textColor);

        String text_string = typedArray.getString(R.styleable.LoginButton_text);
        text.setText(text_string);


        typedArray.recycle();

    }

    void setBg(int bg_resID) {

        bg.setBackgroundResource(bg_resID);
    }

    void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }

    void setTextColor(int color) {

        text.setTextColor(color);
    }

    void setText(String text_string) {
        text.setText(text_string);
    }

    void setText(int text_resID) {
        text.setText(text_resID);
    }

}
