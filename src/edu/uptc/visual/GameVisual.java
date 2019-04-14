/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uptc.visual;

import edu.uptc.logic.Game;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author javierpardo
 */
public class GameVisual extends JFrame implements ActionListener, Game.Callback {

    private JLabel imgOne;
    private JLabel imgTwo;
    private JLabel imgThree;
    private JPanel pnlImg;
    private JButton btnOne;
    private JButton btnTwo;
    private JButton btnThree;
    private JPanel pnlButton;
    private JSpinner spnRode;
    private JButton btnBegin;
    private JLabel lblMoney;
    private JPanel pnlBet;
    private Game gameOne;
    private Game gameTwo;
    private Game gameThree;
    private Font font;
    private int finished = 0;

    public GameVisual() {
        setTitle("RULETA");
        setSize(500, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        beginComponents();
        addComponents();
    }

    private void beginComponents() {
        imgOne = new JLabel();
        imgTwo = new JLabel();
        imgThree = new JLabel();
        imgOne.setHorizontalAlignment(SwingConstants.CENTER);
        imgTwo.setHorizontalAlignment(SwingConstants.CENTER);
        imgThree.setHorizontalAlignment(SwingConstants.CENTER);
        btnOne = new JButton(new ImageIcon(getClass().getResource("/images/stop.png")));
        btnTwo = new JButton(new ImageIcon(getClass().getResource("/images/stop.png")));
        btnThree = new JButton(new ImageIcon(getClass().getResource("/images/stop.png")));
        btnOne.addActionListener(this);
        btnTwo.addActionListener(this);
        btnThree.addActionListener(this);
        gameOne = new Game(imgOne);
        gameOne.setCallback(this);
        gameTwo = new Game(imgTwo);
        gameTwo.setCallback(this);
        gameThree = new Game(imgThree);
        gameThree.setCallback(this);
        pnlButton = new JPanel();
        pnlImg = new JPanel();
        spnRode = new JSpinner(new SpinnerNumberModel(10, 10, 100, 10));
        btnBegin = new JButton("Iniciar");
        btnBegin.addActionListener(this);
        lblMoney = new JLabel();
        pnlBet = new JPanel();
        pnlBet.add(new JLabel("Monto a Apostar:"));
        pnlBet.add(spnRode);
        pnlBet.add(btnBegin);
        pnlBet.add(new JLabel("Tu Dinero: $"));
        pnlBet.add(lblMoney);
        pnlImg.add(imgOne);
        pnlImg.add(imgTwo);
        pnlImg.add(imgThree);
        pnlButton.add(btnOne);
        pnlButton.add(new JLabel("            "));
        pnlButton.add(btnTwo);
        pnlButton.add(new JLabel("            "));
        pnlButton.add(btnThree);
    }

    private void addComponents() {
        add(pnlBet, BorderLayout.NORTH);
        add(pnlImg, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.AFTER_LAST_LINE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBegin) {
            btnBegin.setEnabled(false);
            int money = Integer.parseInt(lblMoney.getText());
            int rode = (Integer) spnRode.getValue();
            if (money == 0 || rode > money) {
                if (JOptionPane.showConfirmDialog(this, "No Tienes dinero suficiente, ¿Desea Ingresar mas Dinero?") == 0) {
                    depositMoney();
                }else{
                    if(lblMoney.getText().equals("0")){
                        this.dispose();
                    }
                    else{
                        btnBegin.setEnabled(true);
                    }
                }

            } else {
                gameOne.begin();
                gameTwo.begin();
                gameThree.begin();
            }

        } else if (e.getSource() == btnOne) {
            gameOne.setStateStop(true);
        } else if (e.getSource() == btnTwo) {
            gameTwo.setStateStop(true);
        } else {
            gameThree.setStateStop(true);
        }

    }

    @Override
    public void onFinished() {
        finished += 1;
        if (finished >= 3) {
            validateImages();
        }
    }

    public static void main(String[] args) {
        GameVisual game = new GameVisual();
        game.lblMoney.setText(JOptionPane.showInputDialog("Ingrese la cantidad de dinero"));
        game.setVisible(true);
    }

    public void validateImages() {
        if (gameOne.isState() == false && gameTwo.isState() == false && gameThree.isState() == false) {
            int rode = (Integer) spnRode.getValue();
            String nameImgOne = imgOne.getIcon().toString();
            String nameImgTwo = imgTwo.getIcon().toString();
            String nameImgThree = imgThree.getIcon().toString();
            gameOne.calculateProfit(rode);
            if (gameOne.validateImages(nameImgOne, nameImgTwo, nameImgThree)) {
                JOptionPane.showMessageDialog(null, "Felicitaciones, GANO:" + gameOne.calculateProfit(rode));
                int money = Integer.parseInt(lblMoney.getText())+gameOne.calculateProfit(rode);
                lblMoney.setText(""+money);
            } else {
                JOptionPane.showMessageDialog(null, "Perdio");
                int money = Integer.parseInt(lblMoney.getText()) - rode;
                lblMoney.setText("" + money);
            }
            finished = 0;
            if (lblMoney.getText().equals("0")) {
                btnBegin.setEnabled(false);
                if (JOptionPane.showConfirmDialog(this, "¿Desea Ingresar mas Dinero?") == 0) {
                    if (depositMoney() > 0) {
                        btnBegin.setEnabled(true);
                    }
                }else{
                    if(lblMoney.getText().equals("0")){
                        this.dispose();
                    }
                    else{
                        btnBegin.setEnabled(true);
                    }
                }
            }
        }
        btnBegin.setEnabled(true);
    }

    public int depositMoney() {
        int money = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de dinero"));
        lblMoney.setText("" + ((Integer.parseInt(lblMoney.getText())) + money));
        return money;
    }
}
