package googlemaps.example.com.csun_guide;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jm12232 on 1/16/2016.
 */

//First window that shows up when user starts the app
public class StartMenu  extends Activity

{

private int RESULT_SPEECH=1;
private String speechText="";

 private boolean mConnection=false;
Vibrator vibrator;


public void onCreate(Bundle savedInstanceState)
{

    super.onCreate(savedInstanceState);
    setContentView(R.layout.start_screen);
    startApp();

}
//Starts the app and vibrates when you click on the botton
    //Detects wether or not the app is connected to the internet

public void startApp()
{
    vibrator=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
    Button mStart;
    mStart=(Button)findViewById(R.id.start);
    mStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            vibrator.vibrate(25);
            mConnection = isNetworkAvailable();
            if (mConnection) {
                Toast.makeText(getApplicationContext(), "Internet", Toast.LENGTH_LONG).show();
                voiceToText();


            } else {
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
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



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
               super.onActivityResult(requestCode, resultCode, data);
String word;
             if (requestCode == RESULT_SPEECH&& resultCode == RESULT_OK)
                 {
                             ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                speechText = matches.get(0);

                }
            word=speechText;

       if(word.equals("start"))
       {
           Intent startMain = new Intent(StartMenu.this, MainActivity.class);
           startActivity(startMain);
       }

          }



    public void voiceToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a)
        {
            Toast t = Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
            //try text input here if voice not available
            Log.d("TEXTTOSPEECH", "voice to text in voice to text: " + speechText);

        }
    }





















    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }










}
