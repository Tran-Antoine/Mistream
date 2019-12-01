package net.akami.mistream.core;

import net.akami.mistream.play.OutputSequence;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class MistreamDisplay extends JFrame {

    private JLabel gameInfo;
    private String text = "none";

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();
        new Timer(500, this::updateGameInfo).start();
    }

    private void updateGameInfo(ActionEvent event) {
        gameInfo.setText("Current play : " + text);
    }

    public void setText(OutputSequence seq) {
        this.text = seq == null ? "none" : seq.name();
    }

    private void addComponents() {

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dataPanel.add(new JLabel("Listening on port " + MistreamMain.DEFAULT_PORT), BorderLayout.CENTER);
        this.gameInfo = new JLabel();
        dataPanel.add(gameInfo, BorderLayout.CENTER);
        panel.add(dataPanel, BorderLayout.CENTER);
        add(panel);

        URL url = MistreamMain.class.getClassLoader().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        panel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        setIconImage(image);

        pack();
        setVisible(true);
    }
}
