package com.interpixel.netra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.Locale;
import java.util.Stack;

public class InputNumberActivity extends AppCompatActivity {

    TextView textView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    Stack<Integer> stack = new Stack<>();
    String input;
    String result;

    TextToSpeech tts;
    GestureDetectorCompat detector;
    String norek;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_number);

        norek = getIntent().getStringExtra("norek");

        input = "";
        result = "";

        textView = findViewById(R.id.textView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);

        button1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(1);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        button2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(2);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        button3.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(3);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        button4.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(4);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        button5.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(5);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        button6.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stack.push(6);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                parseBraille();
            }
            return false;
        });

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(new Locale("id", "ID"));
                if(norek == null){
                    tts.speak("Rotasikan perangkat untuk memasukkan nomor rekening tujuan", TextToSpeech.QUEUE_FLUSH, null);
                }else{
                    tts.speak("Rotasikan perangkat untuk memasukkan nominal transfer", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        detector = new GestureDetectorCompat(this, new NetraGestureListener());
    }

    private void parseBraille() {
        input = input.concat(stack.pop().toString());
        if (!stack.isEmpty()) return;

        if (input.contains("3") || input.contains("6")) {
            tts.speak("Tidak valid", TextToSpeech.QUEUE_FLUSH, null);
        } else if (input.length() == 1 && input.contains("1")) {
            result = result.concat("1");
            tts.speak("1", TextToSpeech.QUEUE_FLUSH, null);
        } else if (input.length() == 2) {
            if (input.contains("1") && input.contains("2")) {
                result = result.concat("2");
                tts.speak("2", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("1") && input.contains("4")) {
                result = result.concat("3");
                tts.speak("3", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("1") && input.contains("5")) {
                result = result.concat("5");
                tts.speak("5", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("2") && input.contains("4")) {
                result = result.concat("9");
                tts.speak("9", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                tts.speak("Tidak valid", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (input.length() == 3) {
            if (input.contains("1") && input.contains("4") && input.contains("5")) {
                result = result.concat("4");
                tts.speak("4", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("1") && input.contains("2") && input.contains("4")) {
                result = result.concat("6");
                tts.speak("6", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("1") && input.contains("2") && input.contains("5")) {
                result = result.concat("8");
                tts.speak("8", TextToSpeech.QUEUE_FLUSH, null);
            } else if (input.contains("2") && input.contains("4") && input.contains("5")) {
                result = result.concat("0");
                tts.speak("0", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                tts.speak("Tidak valid", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (input.length() == 4) {
            result = result.concat("7");
            tts.speak("7", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak("Tidak valid", TextToSpeech.QUEUE_FLUSH, null);
        }

        input = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textView.setVisibility(View.GONE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            button5.setVisibility(View.VISIBLE);
            button6.setVisibility(View.VISIBLE);
            button1.setWidth(button1.getMeasuredHeight());
            button2.setWidth(button2.getMeasuredHeight());
            button3.setWidth(button3.getMeasuredHeight());
            button4.setWidth(button4.getMeasuredHeight());
            button5.setWidth(button5.getMeasuredHeight());
            button6.setWidth(button6.getMeasuredHeight());
            result = "";
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            textView.setVisibility(View.VISIBLE);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);
            button5.setVisibility(View.GONE);
            button6.setVisibility(View.GONE);
            textView.setText(result);
            String bacaan;
            if(norek == null){
                bacaan = "Nomor rekening tujuan, ";
                for (int i = 0; i < result.length(); i++) {
                    bacaan = bacaan.concat(result.charAt(i) + " ");
                }
            }else{
                bacaan = "Nominal transfer sebesar " + result + " rupiah";
            }
            tts.speak(bacaan, TextToSpeech.QUEUE_FLUSH, null);
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

            if(isUp){
                if(result.isEmpty()){
                    tts.speak("Tidak boleh kosong", TextToSpeech.QUEUE_FLUSH, null);
                    return false;
                }
                if(norek == null){
                    Intent intent = new Intent(InputNumberActivity.this, InputNumberActivity.class);
                    intent.putExtra("norek", result);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(InputNumberActivity.this, ConfirmationActivity.class);
                    intent.putExtra("norek", norek);
                    intent.putExtra("nominal", result);
                    startActivity(intent);
                    finish();
                }
            }else{
                onBackPressed();
            }

            return false;
        }
    }
}
