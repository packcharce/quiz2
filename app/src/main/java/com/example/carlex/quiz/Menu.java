package com.example.carlex.quiz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    public static int NIVEL1 = 1, NIVEL2 = 2, NIVEL3 = 3;
    private int resultadoNivel1, resultadoNivel2, resultadoNivel3, censura = 0;
    private Button opcion1, opcion2, opcion3;
    private TextView tv1;
    private Switch sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        opcion1 = (Button) findViewById(R.id.opcion1);
        opcion2 = (Button) findViewById(R.id.opcion2);
        opcion3 = (Button) findViewById(R.id.opcion3);
        tv1 = (TextView) findViewById(R.id.textView);
        sw = (Switch) findViewById(R.id.switchNSFW);
        sw.setVisibility(View.GONE);

        //------------------------------------------------------------------------------
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw.isChecked()) {
                    censura = 1;
                } else {
                    censura = 0;
                }
            }
        });

        //------------------------------------------------------------------------------

        // -----------------------------------------------------------------------------
        final ImageButton ib = (ImageButton) findViewById(R.id.imageButton);
        opcion1.setVisibility(View.GONE);
        opcion2.setVisibility(View.GONE);
        opcion3.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.videoplayback);
        mp.start();

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                ib.setVisibility(View.GONE);
                opcion1.setVisibility(View.VISIBLE);
                opcion2.setVisibility(View.VISIBLE);
                opcion3.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
                sw.setVisibility(View.VISIBLE);
            }
        });

        CountDownTimer ct = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mp.stop();
                ib.setVisibility(View.GONE);
                opcion1.setVisibility(View.VISIBLE);
                opcion2.setVisibility(View.VISIBLE);
                opcion3.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
                sw.setVisibility(View.VISIBLE);
            }
        };
        ct.start();

        //------------------------------------------------------------------------------

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resultadoNivel2 != 1) {
            tv1.setText(getResources().getText(R.string.tituloMenu));
        }
    }

    // Esto para llamar al nivel 1
    public void onClickBoton(View v) {
        Intent intent = new Intent(this, Principal.class);
        intent.putExtra("NIVEL", 1);
        intent.putExtra("censura", censura);
        startActivityForResult(intent, NIVEL1);
    }

    public void onClickBoton2(View v) {
        if (resultadoNivel1 == 1) {
            Intent intent = new Intent(this, Principal.class);
            intent.putExtra("NIVEL", 2);
            intent.putExtra("censura", censura);
            startActivityForResult(intent, NIVEL2);
        } else {
            tv1.setTextSize(20);
            tv1.setText(getResources().getString(R.string.nivelBloqueado));
        }
    }

    public void onClickBoton3(View v) {
        if (resultadoNivel2 == 1) {
            Intent intent = new Intent(this, Principal.class);
            intent.putExtra("NIVEL", 3);
            intent.putExtra("censura", censura);
            startActivityForResult(intent, NIVEL3);
        } else {
            tv1.setTextSize(20);
            tv1.setText(getResources().getString(R.string.nivelBloqueado));
        }
    }

    /**
     * Esto para recoger la info del nivel
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NIVEL1) {
            if (resultCode == Activity.RESULT_OK) {
                resultadoNivel1 = 1;
                //TODO
                opcion1.setText(opcion1.getText() + "\t" + getResources().getString(R.string.tick));
            } else {
                resultadoNivel1 = 0;
            }
        }
        if (requestCode == NIVEL2) {
            if (resultCode == Activity.RESULT_OK) {
                resultadoNivel2 = 1;
                //TODO
                opcion2.setText(opcion2.getText() + "\t" + getResources().getString(R.string.tick));
            } else {
                resultadoNivel2 = 0;
            }
        }
        if (requestCode == NIVEL3) {
            if (resultCode == Activity.RESULT_OK) {
                resultadoNivel3 = 1;
                opcion3.setText(opcion3.getText() + "\t" + getResources().getString(R.string.tick));
                tv1.setText(getResources().getString(R.string.fin));
            } else {
                resultadoNivel3 = 0;
            }
        }
    }
}


