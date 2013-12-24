package com.example.crystalball;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crystalball.R.id;
import com.example.crystalball.ShakeDetector.OnShakeListener;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private CrystalBall mCrystalBall = new CrystalBall();
    private TextView mAnswerLabel;
    private ImageView mCrystalBallImage;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnswerLabel = (TextView) findViewById(id.textView1);
        mCrystalBallImage = (ImageView) findViewById(R.id.imageView1);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new OnShakeListener() {
            @Override
            public void onShake() {
                handleNewAnswer();
            }
        });

        Log.d(TAG, "We're logging from #onCreate.");
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    private void animateCrystalBall() {
        mCrystalBallImage.setImageResource(R.drawable.ball_animation);
        AnimationDrawable ballAnimation = (AnimationDrawable) mCrystalBallImage.getDrawable();

        if (ballAnimation.isRunning()) {
            ballAnimation.stop();
        }

        ballAnimation.start();
    }

    private void animateAnswer() {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);

        fadeInAnimation.setDuration(1500);
        fadeInAnimation.setFillAfter(true);
        mAnswerLabel.setAnimation(fadeInAnimation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void handleNewAnswer() {
        String answer = mCrystalBall.getAnAnswer();

        mAnswerLabel.setText(answer);
        animateCrystalBall();
        animateAnswer();
    }
}
