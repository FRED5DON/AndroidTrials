package com.freddon.android.app.fnannotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.freddon.android.app.nsannotation_api.FNViewBinder;
import com.freddon.android.app.nsannotations.ViewById;

public class MainActivity extends AppCompatActivity {


    @ViewById(R.id.textView)
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FNViewBinder.bind(this);
        textView.setText("Hello Annotation");
    }
}
