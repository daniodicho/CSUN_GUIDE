package googlemaps.example.com.csun_guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jm12232 on 1/16/2016.
 */
public class StartMenu  extends Activity

{
 private Button mStart;
 private boolean mConnection=false;

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
            mConnection=isNetworkAvailable();
          if(mConnection)
          {
              Toast.makeText(getApplicationContext(),"Internet",Toast.LENGTH_LONG).show();
              startActivity(startMain);
          }
            else
          {
           Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_LONG).show();
          }
        }
    });
}
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        onDestroy();
       finish();
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }










}
