package net.akami.mistream.core;

import rlbot.manager.BotManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

public class MistreamMain {

    private static final int DEFAULT_PORT = 17357;

    public static void main(String[] args) {

        BotManager botManager = new BotManager();
        BotLauncher pythonInterface = new BotLauncher(DEFAULT_PORT, botManager);
        new Thread(pythonInterface::start).start();
        displayWindow(botManager);
    }

    private static void displayWindow(BotManager botManager) {
        JFrame frame = new JFrame("Mistream");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dataPanel.add(new JLabel("Listening on port " + DEFAULT_PORT), BorderLayout.CENTER);
        dataPanel.add(new JLabel("I'm the thing controlling the Java bot, keep me open :)"), BorderLayout.CENTER);
        JLabel botsRunning = new JLabel("Bots running: ");
        dataPanel.add(botsRunning, BorderLayout.CENTER);
        panel.add(dataPanel, BorderLayout.CENTER);
        frame.add(panel);

        URL url = MistreamMain.class.getClassLoader().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        panel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        frame.setIconImage(image);

        frame.pack();
        frame.setVisible(true);

        ActionListener myListener = e -> {
            Set<Integer> runningBotIndices = botManager.getRunningBotIndices();

            String botsStr;
            if (runningBotIndices.isEmpty()) {
                botsStr = "None";
            } else {
                botsStr = runningBotIndices.stream()
                        .sorted()
                        .map(i -> "#" + i)
                        .collect(Collectors.joining(", "));
            }
            botsRunning.setText("Bots indices running: " + botsStr);
        };

        new Timer(1000, myListener).start();
    }
}
