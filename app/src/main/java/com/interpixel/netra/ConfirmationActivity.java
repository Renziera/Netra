package com.interpixel.netra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {

    private GestureDetectorCompat detector;
    private boolean confirmed = false;
    private TextToSpeech tts;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        imageView = findViewById(R.id.imageView);

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(new Locale("id", "ID"));
                String bacaan = "Apakah anda yakin ingin melakukan transfer sebesar " +
                        getIntent().getStringExtra("nominal") + " rupiah ke rekening, ";
                String norek = getIntent().getStringExtra("norek");
                for (int i = 0; i < norek.length(); i++) {
                    bacaan = bacaan.concat(norek.charAt(i) + " ");
                }
                tts.speak(bacaan, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        detector = new GestureDetectorCompat(this, new NetraGestureListener());
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

    class NetraGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > 250) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            float diffY = e1.getY() - e2.getY();
            boolean isUp = diffY > 0;
            diffY = Math.abs(diffY);
            if (diffY < 250) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            if (isUp) {
                if (confirmed) {
                    finish();
                } else {
                    confirmed = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ConfirmationActivity.this);
                    int saldo = sp.getInt("saldo", 0);
                    int nominal = Integer.parseInt(getIntent().getStringExtra("nominal"));
                    if(saldo < nominal){
                        tts.speak("Saldo anda tidak cukup, tranksaksi tidak dapat dilakukan", TextToSpeech.QUEUE_FLUSH, null);
                        new Handler().postDelayed(() -> finish(), 3500);
                        return false;
                    }
                    imageView.setImageResource(R.drawable.success);
                    saldo = saldo - nominal;
                    sp.edit().putInt("saldo", saldo).apply();
                    tts.speak("Transaksi berhasil dilakukan", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                if (!confirmed) {
                    tts.speak("Transaksi dibatalkan", TextToSpeech.QUEUE_FLUSH, null);
                    new Handler().postDelayed(() -> finish(), 1500);
                    return false;
                }
                finish();
            }

            return false;
        }
    }
}
