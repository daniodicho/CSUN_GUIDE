package googlemaps.example.com.csun_guide;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import googlemaps.example.com.csun_guide.R;
import googlemaps.example.com.csun_guide.R.id;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MainActivity extends FragmentActivity  implements SensorEventListener, android.location.LocationListener, TextToSpeech.OnInitListener{
    GoogleMap map;
    Polyline pol;
    /*   static final LatLng HAMBURG = new LatLng(53.558, 9.927);
       static Point pt = new Point(34.194038,-118.322734);
       static Point pt2 = new Point(34.194048,-118.322734);
       static Point pt8 = new Point(34.194028,-118.322734);
       static Point pt4 = new Point(34.194038,-118.322834);
       static Point pt6 = new Point(34.194038,-118.322634);
       static Point pt1 = new Point(34.193892,-118.322836);
       static Point pt3 = new Point(34.194048,-118.322634);*/

        //test
    Point target;

    boolean pathSet = false;
    String output="";
    //test
    double lat=0;
    double lon=0;
    double oldLat=0;
    double oldLon=0;
    LocationManager LM;
    LatLng recent;
    LatLng old;
    CircleOptions co = new CircleOptions();
    TextView coordinates;
    TextView disclaimer;
    RelativeLayout layout;
    int RESULT_SPEECH = 1;      //flag to determine when speech to text is done
    String speechText="";		//string that holds the result of speech to text
    TextToSpeech tts;			//text to speech object
    Table table;
    float total;
    float counter;
    float compassAngle=0;
    float directionAngle=0;
    Circle mapCircle;
    Point CurrentLocationPoint;
    LinkedList<Point> path;

    Vibrator v;
    // Used for compass
    SensorManager sm;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    ImageView blueArrow;
    ImageView redArrow;
    float time;
    boolean vibrated = false;

    Point test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinates = (TextView)findViewById(R.id.textView2);                 // top left text view used for debugging
        blueArrow = (ImageView)findViewById(R.id.pointer);								// image view for compass pointer
        redArrow = (ImageView)findViewById(id.direction);
        // set up the sensor manager and sensors for the compass
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);

        // set up location manager and ask it to request location every 1000 milliseconds
        LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LM.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0,this);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();          // initialize map
        map.setMyLocationEnabled(true);
        //     CurrentLocationPoint = new Point(0,0);

        // moves the camera of the map
        move();


        v=(Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        // start the disclaimer activity  (needed for every app that uses google maps)
        //    Intent i=new Intent(this,Disclaimer.class);
        //     startActivity(i);


        // instantiate text to speech object
        tts = new TextToSpeech(this,this);


        AssetManager assetManager = getAssets();
        // To load text file
        InputStream input;
        try {
            input = assetManager.open("data.txt");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String text = new String(buffer);
            //break the string to parts by lines
            String[] parts = text.split("\n");
            // read the string into a table object
            table = new Table(parts);
            // set all adjacents of the table
            table.setAllAdjacents();

            //starting at jacaranda initially for testing
            CurrentLocationPoint = table.find(60);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //path = AStar.aStarPathFinding(table.find(55), table.find(52));
        //map.addCircle(new CircleOptions().center(new LatLng(test.getLatitude(),test.getLongitude())).radius(2));
        //map.addCircle(new CircleOptions().center(new LatLng(path.get(1).latitude,path.get(1).longitude)));
//        pathSet = true;
        drawLines();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            // What happens when you touch the map
            @Override
            public void onMapClick(LatLng point)
            {
                //Activate text to speech
                voicToText();
            }
        });

        time = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //    setUpMapIfNeeded();
    }

    @Override
    public void onLocationChanged(Location loc) {
        // TODO Auto-generated method stub
        lat = loc.getLatitude();
        lon = loc.getLongitude();
        CurrentLocationPoint = new Point(lat,lon);

        if(Math.abs(lat-oldLat)>0.00005||Math.abs(lon-oldLon)>0.00005)
        {
            //   markDot();
            //   move();
            oldLat=lat;
            oldLon=lon;
        }
        // decrease this 000.1 if it turned out to be too large
        if((pathSet)&&(CurrentLocationPoint.distance(path.getFirst())<0.0001)){
            path.removeFirst();
            target=path.getFirst();
        }

/*        coordinates.setText("P1:"+table.getAngle(pt1, pt)+"\n"+
        		"P2:"+table.getAngle(pt2, pt)+"\n"+
        		"P3:"+table.getAngle(pt3, pt)+"\n"+
        		"P4:"+table.getAngle(pt4, pt)+"\n"+
        		"P6:"+table.getAngle(pt6, pt)+"\n"+
        		"P8:"+table.getAngle(pt8, pt)+"\n"+
        		"Azimuth:"+total/counter);*/

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public void markDot(){
        recent = new LatLng(lat,lon);
        //old = new LatLng(oldLat,oldLon);
        if(mapCircle!=null)
            mapCircle.remove();
        co.center(recent).radius(5);
//        mapCircle = map.addCircle(co);


    }


    public void drawLine(Point p1, Point p2){
        LatLng l1 = new LatLng(p1.latitude,p1.longitude);
        LatLng l2 = new LatLng(p2.latitude,p2.longitude);
        PolylineOptions po = new PolylineOptions().color(Color.BLACK);
        po.add(l1);
        po.add(l2);
        map.addPolyline(po);
    }

    public void drawPath(LinkedList<Point> p){
        PolylineOptions po = new PolylineOptions().color(Color.RED);
        for(int i=0;i<p.size();i++){
            LatLng l1 = new LatLng(p.get(i).latitude,p.get(i).longitude);
            po.add(l1);
            map.addCircle(new CircleOptions().center(l1).radius(3));
        }
        map.addPolyline(po);
    }



    public void move(){
        //map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.24186636,-118.5308464)));


        //	map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.193945,-118.321800)));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        // Give feedback every 8 seconds
        if(pathSet&&(System.currentTimeMillis()-time>8000)){
            speakOut(getOutput(total/counter,table.getAngle(CurrentLocationPoint, target)));
            time = System.currentTimeMillis();
        }

        if((Math.abs(compassAngle-directionAngle)<2)&&!vibrated){
            v.vibrate(200);
            speakOut("Move Forward");
            vibrated = true;
        }
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians));
            if (azimuthInDegress<0)
                azimuthInDegress+=360;
            counter++;
            total+=azimuthInDegress;
            compassAngle = azimuthInDegress;
            coordinates.setText(""+compassAngle);
            if(System.currentTimeMillis()%1000<3){
                compassAngle = total/counter;
                blueArrow.setRotation(compassAngle - 90);
                total = azimuthInDegress;
                counter = 1;
            }
            if(pathSet){
                directionAngle = (float)table.getAngle(CurrentLocationPoint, target);
                coordinates.setText("P1:" + directionAngle + "\n" +

                        "Azimuth:" + total / counter + "\nTarget: " + target.id + "\nDistance:" + CurrentLocationPoint.distance(target));
                redArrow.setRotation(directionAngle-90);
            }
        }
    }

    public void drawLines(){
        for(int i=0;i<table.points.length;i++){
            Point p = table.points[i];
            for(int j=0;j<p.adjacents.size();j++) {
                drawLine(p, p.adjacents.get(j));
            }
        }
    }





    //Activates speech to text
    public void voicToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
            t.show();
            //try text input here if voice not available
            Log.d("TEXTTOSPEECH", "voice to text in voice to text: " + speechText);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("TEXTTOSPEECH", "onActivity " + speechText);
        if(requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                speechText = text.get(0);
                //	Log.d("TEXTTOSPEECH", "voice to text: " + speechText);
                Log.d("TEXTTOSPEECH", "voice to text in onActivityResult " + speechText);
                Point dest = table.findByName(speechText, CurrentLocationPoint.latitude, CurrentLocationPoint.longitude);
                path = AStar.aStarPathFinding(table.findClosest(CurrentLocationPoint), dest);
                speakOut("Navigating from "+ table.findClosest(CurrentLocationPoint).getName() + " to "+dest.getName());
                if(CurrentLocationPoint==null){
                    speakOut("Can't find your current location");
                } else if (path.size() == 1 && path.getFirst().getName()!=dest.getName()){
                    speakOut("Can't find path to "+speechText);
                }
                else{
                    pathSet = true;
                    target=path.getFirst();
                    drawPath(path);
                }
            }
        }
    }

    private void speakOut(String text) {
        //while(speechText=="");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.e("SPEECHTOTEXT", "Spoke: "+speechText);

    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void onBackPressed(){
        super.onBackPressed();
        onDestroy();
        finish();
    }

    String getOutput(double compass, double angle){
        if(Math.abs(compass-angle)<2){
            return "move forward";
        }
        else{
            vibrated = false;
            String s = "turn ";

          //  actual value no longer needed if we are using haptic feedback
            //s+= (int)Math.abs(compass-angle);
            if(compass>angle){
                if(compass-angle<180)
                    s+=" left";
                else
                    s+=" right";
            }
            else{
                if(angle-compass<180)
                    s+=" right";
                else
                    s+=" left";

            }
            return s;
        }
    }
}
   