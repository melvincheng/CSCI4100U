package ca.tru.comp2160.kanamatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GetNameActivity extends Activity {
	public static final int GET_NAME_REQUEST = 1230021;
	public static final int GET_NAME_RESULT = 1230031;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.name_input);
    }
    
    public void returnName(View view) {
    	EditText txtName = (EditText)findViewById(R.id.txtHighScoreName);
    	String name = txtName.getText().toString();
    	
		Intent intent = this.getIntent();
		intent.putExtra("name", name);
		this.setResult(GetNameActivity.GET_NAME_RESULT, intent);
		finish();
    }
}
