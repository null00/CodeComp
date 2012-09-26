package pl.edu.agh.codecomp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;
 
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
 
public class SyncTest extends JFrame {
    public JTextArea textArea1 = new JTextArea();
 
    public JTextArea textArea2 = new JTextArea();
 
    public SyncTest() {
        this.setSize(800, 600);
 
        JScrollPane jsp1 = new JScrollPane(new SyncSizePanel(textArea1));
        JScrollPane jsp2 = new JScrollPane(new SyncSizePanel(textArea2));
 
        final JScrollBar vScroll1 = jsp1.getVerticalScrollBar();
        final JScrollBar vScroll2 = jsp2.getVerticalScrollBar();
 
        vScroll1.addAdjustmentListener(new AdjustmentListener() {
 
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {
                    return;
                }
 
                vScroll2.setValue(e.getValue());
            }
 
        });
 
        vScroll2.addAdjustmentListener(new AdjustmentListener() {
 
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {
                    return;
                }
 
                vScroll1.setValue(e.getValue());
            }
 
        });
 
        this.getContentPane().setLayout(new GridLayout(1, 2));
 
        this.getContentPane().add(jsp1);
        this.getContentPane().add(jsp2);
 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
    }
 
    public static void main(String[] args) {
        SyncTest syncTest = new SyncTest();
        syncTest.setVisible(true);
 
        StringBuffer buffer = new StringBuffer();
 
        for (int i = 0; i < 100; i++) {
        	if(i % 2 == 0) {
        		buffer.append("\n");
        	}
            buffer.append("test1-" + i + "\n");
        }
 
        syncTest.textArea1.setText(buffer.toString());
 
        buffer.delete(0, buffer.length());
 
        for (int i = 0; i < 300; i++) {
            buffer.append("test2-" + i + "\n");
        }
 
        syncTest.textArea2.setText(buffer.toString());
    }
 
}
 
class SyncSizePanel extends JPanel {
    private static Vector panels = new Vector();
 
    private JPanel spacer = new JPanel();
 
    public JComponent component;
 
    public SyncSizePanel(JComponent component) {
        spacer.setBackground(Color.WHITE);
 
        panels.add(this);
        this.component = component;
 
        this.setLayout(new BorderLayout());
        this.add(component, BorderLayout.CENTER);
        this.add(spacer, BorderLayout.SOUTH);
 
        component.addComponentListener(new ComponentAdapter() {
 
            public void componentResized(ComponentEvent e) {
                syncComponentSizes();
            }
 
        });
    }
 
    private void syncComponentSizes() {
        Dimension d = component.getPreferredSize();
        
        int w = d.width;
        int h = d.height;
 
        for (int i = 0; i < panels.size(); i++) {
            SyncSizePanel panel = (SyncSizePanel) panels.get(i);
            Component c = panel.component;
 
            if (panel != this) {
                Dimension d2 = c.getPreferredSize();
                
                w = w > d2.width ? w : d2.width;
                h = h > d2.height ? h : d2.height;
            }
        }
 
        for (int i = 0; i < panels.size(); i++) {
            SyncSizePanel panel = (SyncSizePanel) panels.get(i);
            panel.setSyncSize(w, h);
        }
    }
 
    private void setSyncSize(int width, int height) {
        Dimension d = component.getPreferredSize();
        spacer.setPreferredSize(new Dimension(width - d.width,
                height - d.height));
        revalidate();
    }
}
 