/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uptc.logic;

import java.util.Random;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author javierpardo
 */
public class Game {

    private boolean state = true;
    private boolean stateStop = false;
    private JLabel output;
    private int time = 0;
    private Thread threadBegin;
    private Callback callback;

    public Game(JLabel output) {
        this.output = output;
        //restart();
    }

    public Game() {

    }

    public void restart() {
        state = true;
        stateStop = false;
        time = 0;

        threadBegin = new Thread(new Runnable() {
            @Override
            public void run() {
                while (stateStop && state) {
                    int i = 0;
                    while (i < 500) {
                        i += 25;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                        }
                        time = i;
                        if (time >= 500) {
                            state = false;
                        }
                    }
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (state) {
                    Random r = new Random();
                    int num = r.nextInt(5) + 1;
                    output.setIcon(new ImageIcon(getClass().getResource("/images/img" + num + ".png")));
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException ex) {
                    }
                }
                if (callback != null && stateStop) {
                    callback.onFinished();
                    stateStop = false;
                }
            }
        }).start();
    }

    public boolean isStateStop() {
        return stateStop;
    }

    public void setStateStop(boolean stateStop) {
        this.stateStop = stateStop;
        if (threadBegin != null) {
            try {
                threadBegin.start();
            } catch (Exception e) {
            }
        }
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void stopThread() {
        Thread.interrupted();
    }

    public boolean validateImages(String i1, String i2, String i3) {
        return (i1.equals(i2) && i1.equals(i3));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onFinished();
    }

    public void begin() {
        restart();
    }

    public int calculateProfit(int rode) {
        String imgName = output.getIcon().toString();
        imgName = imgName.substring(imgName.lastIndexOf("/"), imgName.length());
        imgName = imgName.substring(4, imgName.lastIndexOf("."));
        int numImg = Integer.parseInt(imgName);
        switch (numImg) {
            case 1:
                return rode * 2;
            case 2:
                return rode * 3;
            case 3:
                return rode * 4;
            case 4:
                return rode * 5;
            case 5:
                return rode * 10;
            case 6:
                return rode * 20;
        }
        return rode;
    }
}
