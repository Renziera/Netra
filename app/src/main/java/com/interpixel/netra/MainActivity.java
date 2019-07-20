package com.interpixel.netra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Locale;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements FlingListener {

    private final int[] topMenu = {
            R.drawable.account_info,
            R.drawable.transfer,
            R.drawable.payment,
            R.drawable.qr_payment,
            R.drawable.m_commerce,
            R.drawable.account_setting,
    };

    private final int[] accountInfoMenu = {
            R.drawable.balance_info,
            R.drawable.mini_statement,
    };

    private final int[] transferMenu = {
            R.drawable.same_bank,
            R.drawable.other_bank,
            R.drawable.va_transfer,
    };

    private final int[] paymentMenu = {
            R.drawable.internet_bill,
            R.drawable.telephone_bill,
            R.drawable.electricity_bill,
            R.drawable.flight_ticket,
            R.drawable.insurance,
    };

    private int[] currentMenu = topMenu;
    private Stack<Integer> selectedMenu = new Stack<>();
    private Adapter adapter;
    private NetraViewPager viewpager;
    private boolean mustListenPageChange = true;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView splashLogo = findViewById(R.id.splash_logo);

        viewpager = findViewById(R.id.viewpager);

        new Handler().postDelayed(() -> {
            splashLogo.setVisibility(View.GONE);
            viewpager.setVisibility(View.VISIBLE);
        }, 1000);

        adapter = new Adapter(getSupportFragmentManager());

        viewpager.setAdapter(adapter);
        viewpager.setFlingListener(this);
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (!mustListenPageChange) return;
                selectedMenu.pop();
                selectedMenu.push(position);
                if (currentMenu == topMenu) {
                    switch (position) {
                        case 0:
                            tts.speak("Informasi akun", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 1:
                            tts.speak("Transfer", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 2:
                            tts.speak("Pembayaran", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 3:
                            tts.speak("Pembayaran QR", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 4:
                            tts.speak("Belanja online", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 5:
                            tts.speak("Pengaturan akun", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                    }
                }
                if (currentMenu == accountInfoMenu) {
                    if (position == 0) {
                        tts.speak("Informasi saldo", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        tts.speak("Keterangan singkat", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                if (currentMenu == transferMenu) {
                    switch (position) {
                        case 0:
                            tts.speak("Transfer bank fision", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 1:
                            tts.speak("Transfer bank lain", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 2:
                            tts.speak("Transfer akun virtual", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                    }
                }
                if (currentMenu == paymentMenu) {
                    switch (position) {
                        case 0:
                            tts.speak("Tagihan internet", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 1:
                            tts.speak("Tagihan telepon", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 2:
                            tts.speak("Tagihan listrik", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 3:
                            tts.speak("Tiket pesawat", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 4:
                            tts.speak("Asuransi", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                    }
                }
            }
        });
        selectedMenu.push(0);

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(new Locale("id", "ID"));
                tts.speak("Selamat datang di fision, " +
                        "anda berada pada menu informasi akun." +
                        "Geser ke samping untuk menu lain," +
                        "geser ke bawah untuk memilih menu.", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sp.contains("saldo")){
            sp.edit().putInt("saldo", 190000000).apply();
        }
    }

    @Override
    public void menuPrev() {
        if (selectedMenu.size() < 2) return;
        currentMenu = topMenu;
        adapter.notifyDataSetChanged();
        int temp = selectedMenu.pop();
        viewpager.setCurrentItem(selectedMenu.peek(), false);
        if(temp == selectedMenu.peek()){
            if(temp == 0)
                tts.speak("Informasi akun", TextToSpeech.QUEUE_FLUSH, null);
            if(temp == 1)
                tts.speak("Transfer", TextToSpeech.QUEUE_FLUSH, null);
            if(temp == 2)
                tts.speak("Pembayaran", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void menuNext() {
        if (selectedMenu.size() == 1) {
            if (selectedMenu.peek() == 0) {
                currentMenu = accountInfoMenu;
                tts.speak("Informasi saldo", TextToSpeech.QUEUE_FLUSH, null);
            } else if (selectedMenu.peek() == 1) {
                currentMenu = transferMenu;
                tts.speak("Transfer bank fision", TextToSpeech.QUEUE_FLUSH, null);
            } else if (selectedMenu.peek() == 2) {
                currentMenu = paymentMenu;
                tts.speak("Tagihan internet", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                if (selectedMenu.peek() == 3) {
                    tts.speak("Orang buta tidak bisa scan QR", TextToSpeech.QUEUE_FLUSH, null);
                } else if (selectedMenu.peek() == 4) {
                    tts.speak("Bank ini belum punya mitra", TextToSpeech.QUEUE_FLUSH, null);
                } else if (selectedMenu.peek() == 5) {
                    tts.speak("Silahkan ke bank untuk mengubah pengaturan akun", TextToSpeech.QUEUE_FLUSH, null);
                }
                return;
            }
        } else {
            if (currentMenu == accountInfoMenu) {
                if (selectedMenu.peek() == 0) {
                    startActivity(new Intent(this, SaldoActivity.class));
                } else if (selectedMenu.peek() == 1) {
                    tts.speak("Anda kurang beruntung, silahkan coba lagi", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else if (currentMenu == transferMenu) {
                if (selectedMenu.peek() == 0) {
                    startActivity(new Intent(this, InputNumberActivity.class));
                } else if (selectedMenu.peek() == 1) {
                    tts.speak("Layanan belum tersedia", TextToSpeech.QUEUE_FLUSH, null);
                } else if (selectedMenu.peek() == 2) {
                    tts.speak("Layanan belum tersedia", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                tts.speak("Layanan belum tersedia", TextToSpeech.QUEUE_FLUSH, null);
            }
            return;
        }
        mustListenPageChange = false;
        adapter.notifyDataSetChanged();
        selectedMenu.push(0);
        viewpager.setCurrentItem(selectedMenu.peek(), false);
        mustListenPageChange = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    class Adapter extends FragmentStatePagerAdapter {

        Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return new MenuFragment(currentMenu[position]);
        }

        @Override
        public int getCount() {
            return currentMenu.length;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
