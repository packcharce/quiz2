package com.example.carlex.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Principal extends AppCompatActivity {

    private final int tiempoFinal = 10000, intervalo = 100;
    private RadioGroup rg;
    private RadioButton rb1, rb2, rb3;
    private TextView tv;
    private ImageView imagen;
    private ProgressBar pb;
    private int progreso, numRespuestas = 0, posicionCorrecta, puntos = 0;
    private List vistos;
    private TypedArray imgs, resps;
    private CountDownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        Bundle b = getIntent().getExtras();
        short numNivel = b.getShort("NIVEL");

        switch (numNivel) {
            case 1:
                imgs = getResources().obtainTypedArray(R.array.imagenes1);
                resps = getResources().obtainTypedArray(R.array.respuestas1);
                break;
            case 2:
                imgs = getResources().obtainTypedArray(R.array.imagenes2);
                resps = getResources().obtainTypedArray(R.array.respuestas2);
                break;
            case 3:
                imgs = getResources().obtainTypedArray(R.array.imagenes3);
                resps = getResources().obtainTypedArray(R.array.respuestas3);
                break;
        }

        vistos = new LinkedList<>();

        imagen = (ImageView) findViewById(R.id.imagen);
        tv = (TextView) findViewById(R.id.muestraRes);

        pb = (ProgressBar) findViewById(R.id.progressBar);

        rb1 = (RadioButton) findViewById(R.id.opcion1);
        rb2 = (RadioButton) findViewById(R.id.opcion2);
        rb3 = (RadioButton) findViewById(R.id.opcion3);

        rg = (RadioGroup) findViewById(R.id.respuestas);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (i) {
                    case R.id.opcion1:
                        if (posicionCorrecta == 0)
                            puntos++;
                        rg.clearCheck();
                        ct.cancel();
                        rb1.setText(null);
                        rb2.setText(null);
                        rb3.setText(null);
                        imagenRandom();
                        break;
                    case R.id.opcion2:
                        if (posicionCorrecta == 1)
                            puntos++;
                        rg.clearCheck();
                        ct.cancel();
                        rb1.setText(null);
                        rb2.setText(null);
                        rb3.setText(null);
                        imagenRandom();
                        break;
                    case R.id.opcion3:
                        if (posicionCorrecta == 2)
                            puntos++;
                        rg.clearCheck();
                        ct.cancel();
                        rb1.setText(null);
                        rb2.setText(null);
                        rb3.setText(null);
                        imagenRandom();
                        break;
                }
            }
        });
        imagenRandom();
    }


    /**
     * Metodo que carga una imagen aleatoria
     * y resetea el contador de tiempo.
     */
    private void imagenRandom() {

        ct = null;
        final Random rand = new Random();
        int rndInt = rand.nextInt(imgs.length());
        int resID = imgs.getResourceId(rndInt, 0);

        cargaResp(rndInt);
        if (numRespuestas != imgs.length()) {
            while (vistos.contains(resID)) {
                rndInt = rand.nextInt(imgs.length());
                resID = imgs.getResourceId(rndInt, 0);
                cargaResp(rndInt);
            }
            vistos.add(resID);
            imagen.setImageResource(resID);

            progreso = 100;
            pb.setProgress(progreso);
            ct = new CountDownTimer(tiempoFinal, intervalo) {
                @Override
                public void onTick(long l) {
                    pb.setProgress(progreso);
                    progreso -= 1;
                }

                @Override
                public void onFinish() {
                    progreso -= 1;
                    pb.setProgress(progreso);
                    fin(0);
                }
            };
            numRespuestas++;
            ct.start();
        } else {
            fin(1);
        }

    }

    /**
     * Metodo que carga las respuestas
     * en posicion aleatoria.
     *
     * @param posImagen La posicion de la imagen y respuesta correcta
     */
    private void cargaResp(int posImagen) {

        Random rand = new Random();
        posicionCorrecta = rand.nextInt(3);


        int aux = 0;
        List<Integer> posIncorr = new LinkedList<>();

        while (aux < 2) {
            int aux2 = rand.nextInt(4);
            if (aux2 != posImagen && !posIncorr.contains(aux2)) {
                posIncorr.add(aux2);
                aux++;
            }
        }

        switch (posicionCorrecta) {
            case 0:
                rb1.setText(resps.getString(posImagen));
                rb2.setText(resps.getString(posIncorr.get(0)));
                rb3.setText(resps.getString(posIncorr.get(1)));
                break;
            case 1:
                rb1.setText(resps.getString(posIncorr.get(0)));
                rb2.setText(resps.getString(posImagen));
                rb3.setText(resps.getString(posIncorr.get(1)));
                break;

            case 2:
                rb1.setText(resps.getString(posIncorr.get(1)));
                rb2.setText(resps.getString(posIncorr.get(0)));
                rb3.setText(resps.getString(posImagen));

        }
    }

    /**
     * Metodo que muestra un mensaje dependiendo
     * del final del juego: falta tiempo o
     * se acabaron las imagenes.
     *
     * @param tipoFin Tipo de fin de juego
     **/
    private void fin(int tipoFin) {
        if (tipoFin == 0)
            tv.setText("Se ha acabado el tiempo");
        else
            tv.setText("Has acabado!!! \nGanas " + puntos + " puntos");
        rg.setVisibility(View.INVISIBLE);
        imagen.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intentRetornoDatos = new Intent();
                if (puntos == imgs.length()) {
                    setResult(Activity.RESULT_OK, intentRetornoDatos);
                } else
                    setResult(Activity.RESULT_CANCELED, intentRetornoDatos);
                finish();
            }
        }, 2000);


    }
}