import javax.swing.*;
import java.awt.*;

public class Sketch extends JFrame {
    private static final int MIN_HEIGHT = 700;
    private static final int MIN_WIDTH = 700;

    public Sketch() {
        initializeComponents();
    }

    private void initializeComponents() {
        Model model = new Model();

        SketchCanvas canvas = new SketchCanvas(model);
        SketchSlider slider = new SketchSlider(model);
        SketchToolBar toolBar = new SketchToolBar(model);

        model.addViews(canvas);
        model.addViews(slider);

        JButton startButton= new JButton();

        add(canvas, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
        add(slider, BorderLayout.SOUTH);
        add(toolBar, BorderLayout.NORTH);


        setTitle("Sketch");
        setSize(MIN_WIDTH, MIN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Sketch frame = new Sketch();
                frame.setVisible(true);
            }
        });
    }
}
