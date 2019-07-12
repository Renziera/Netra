package com.interpixel.netra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Locale;

public class SaldoActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        TextView tv_saldo = findViewById(R.id.tv_saldo);
        tv_saldo.setText("Rp" + PreferenceManager.getDefaultSharedPreferences(this).getInt("saldo", 0) + ",00");

        detector = new GestureDetectorCompat(this, new NetraGestureListener());

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(new Locale("id", "ID"));
                int saldo = PreferenceManager.getDefaultSharedPreferences(this).getInt("saldo", 0);
                String bacaan = "Saldo anda " + saldo + " rupiah";
                tts.speak(bacaan, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    class NetraGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(Math.abs(e1.getX() - e2.getX()) > 250){
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            float diffY = e1.getY() - e2.getY();
            boolean isUp = diffY > 0;
            diffY = Math.abs(diffY);
            if(diffY < 250){
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if(!isUp){
                finish();
            }

            return false;
        }
    }
}
