package pl.edu.agh.codecomp.comparator;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.gui.CodeCompGUI;
import pl.edu.agh.codecomp.gui.dialogs.ScoreDialog;

public class DistanceComparator extends IComparator {

    public DistanceComparator(RSyntaxTextArea left, RSyntaxTextArea right) {
        super();
        this.left = left;
        this.right = right;
        start();
    }

    @Override
    public void run() {
        new ScoreDialog("Distance: " + compare());
        CodeCompGUI.getMainWin().setEnabled(true);
    }
    
    private String compare() {
        double distance = StringUtils.getLevenshteinDistance(left.getText(), right.getText());
        int len1 = left.getText().length(), len2 = right.getText().length();
        double len;
        if(len1 > len2) {
            len = len1;
        } else {
            len = len2;
        }
        double p = (1 - distance / len) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(p);
    }    
}
