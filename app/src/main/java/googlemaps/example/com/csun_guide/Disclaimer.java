package googlemaps.example.com.csun_guide;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Disclaimer extends Activity {
	TextView disclaimer;
	Button b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disclaimer);
		b = (Button)findViewById(R.id.button1);
		disclaimer = (TextView)findViewById(R.id.textView3);
        disclaimer.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this)+"\n\n\n\n\n");
	
        b.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View currentView) 
			{
				finish();
			}		
		});

	}
}
