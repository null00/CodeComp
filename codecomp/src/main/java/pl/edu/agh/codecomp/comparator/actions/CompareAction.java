package pl.edu.agh.codecomp.comparator.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.codecomp.comparator.DistanceComparator;
import pl.edu.agh.codecomp.comparator.SourceComparator;
import pl.edu.agh.codecomp.comparator.TextComparator;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class CompareAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "text": {
                new TextComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
                new DistanceComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
                break;
            }
            case "source": {
                new TextComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
                new SourceComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
                break;
            }
        }
    }
}
