package com.example.melvincheng.a1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AskQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        TextView question_textView = (TextView)findViewById(R.id.question_lbl);
        TextView question_num_textView = (TextView)findViewById(R.id.question_num_lbl);

        Bundle extras = getIntent().getExtras();
        String question_number = extras.getString("question_number");
        String question = extras.getString("question");

        question_textView.setText(question);
        question_num_textView.setText(question_number);
    }

    public void answered(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.yes_btn:
                intent.putExtra("answer", "yes");
                break;
            case R.id.no_btn:
                intent.putExtra("answer", "no");
                break;
        }
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
