package zfl.android_one;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import zfl.com.myapplication.R;


public class CompleteTextView extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private String[] res = {"one","two","three"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_complete_text_view);
        autoCompleteTextView = findViewById(R.id.auto);
        multiAutoCompleteTextView = findViewById(R.id.multauti);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,res);
        autoCompleteTextView.setAdapter(adapter);

        multiAutoCompleteTextView.setAdapter(adapter);
        //设置分隔符
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }
}
