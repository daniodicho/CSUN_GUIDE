package googlemaps.example.com.csun_guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by jm12232 on 1/16/2016.
 */
public class StartMenu  extends Activity

{
 private Button mStart;

public void onCreate(Bundle savedInstanceState)
{

    super.onCreate(savedInstanceState);
    setContentView(R.layout.start_screen);
    startApp();

}

public void startApp()
{
    mStart=(Button)findViewById(R.id.start);
    mStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startMain = new Intent(StartMenu.this, MainActivity.class);
            startActivity(startMain);
        }  });



}


}
