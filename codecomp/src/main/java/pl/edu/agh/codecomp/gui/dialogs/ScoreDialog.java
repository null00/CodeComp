package pl.edu.agh.codecomp.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID     = 1L;
    private final String      TITLE                = "Similarity Score";
    private final Dimension   DIALOG_DIMENSION     = new Dimension(300, 120);
    private final Dimension   MAX_DIALOG_DIMENSION = new Dimension(240, 140);

    public ScoreDialog(String... textToDisplay) {
        super();
        initDialog(textToDisplay);
    }

    private void initDialog(String... lines) {
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(DIALOG_DIMENSION);
//        this.getContentPane().setMaximumSize(MAX_DIALOG_DIMENSION);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        textPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        for(String text : lines) {
            JLabel scoreField = new JLabel(text, JLabel.CENTER);
            scoreField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            scoreField.setForeground(Color.CYAN);
            scoreField.setAlignmentX(Component.CENTER_ALIGNMENT);
            scoreField.setAlignmentY(Component.CENTER_ALIGNMENT);
            textPanel.add(scoreField, BorderLayout.CENTER);
        }
        this.getContentPane().add(textPanel, BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setActionCommand("ok");
        this.getContentPane().add(okButton, BorderLayout.SOUTH);

        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "ok": {
                this.dispose();
                break;
            }
        }

    }
}
