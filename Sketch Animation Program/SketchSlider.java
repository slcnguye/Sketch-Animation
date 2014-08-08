import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class SketchSlider extends JPanel{
    private Model model;
    private java.util.Timer timer;

    private boolean stopTimer = true;
    private JSlider slider = new JSlider();
    private JButton playButton = new JButton();
    private JButton prevButton = new JButton();
    private JButton nextButton = new JButton();

    private final ImageIcon PLAY_ICON = new ImageIcon(new ImageIcon("buttons/play.png").getImage().getScaledInstance(32, 32, 16));
    private final ImageIcon PAUSE_ICON = new ImageIcon(new ImageIcon("buttons/pause.png").getImage().getScaledInstance(32, 32, 16));

    public SketchSlider(Model newModel) {
        this.model = newModel;

        setLayout(new GridBagLayout());

        slider.setValue(0);
        slider.setMaximum(model.getTotalFrames()-1);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setPreferredSize(new Dimension(500,55));

        playButton.setIcon(PLAY_ICON);
        playButton.setPreferredSize(new Dimension(40, 40));
        ImageIcon PREV_ICON = new ImageIcon(new ImageIcon("buttons/prev.png").getImage().getScaledInstance(32, 32, 16));
        prevButton.setIcon(PREV_ICON);
        prevButton.setPreferredSize(new Dimension(40, 40));
        ImageIcon NEXT_ICON = new ImageIcon(new ImageIcon("buttons/next.png").getImage().getScaledInstance(32, 32, 16));
        nextButton.setIcon(NEXT_ICON);
        nextButton.setPreferredSize(new Dimension(40, 40));

        add(playButton);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(prevButton);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(slider);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(nextButton);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slider.getValue() != model.getCurrentTimeFrame()) {
                    stopTimer();
                    model.setCurrentTimeFrame(slider.getValue());
                }
            }
        });

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (playButton.getIcon() == PLAY_ICON) {
                    playButton.setIcon(PAUSE_ICON);
                    playFromCurrentTimeFrame();
                } else if (playButton.getIcon() == PAUSE_ICON) {
                    stopTimer();
                }
            }
        });

        prevButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopTimer();
                model.decrementTimeFrame();
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopTimer();
                model.incrementTimeFrame();
            }
        });
    }

    private void playFromCurrentTimeFrame() {
        timer = new Timer();
        stopTimer = false;
        model.setStateAnimating();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(model.getCurrentTimeFrame()<model.getTotalFrames()-1 && !stopTimer && model.isAnimating()) {
                    model.incrementTimeFrame();
                } else {
                    model.setToPrevState();
                    playButton.setIcon(PLAY_ICON);
                    timer.cancel();
                }
            }
        },0,150);
    }

    public void stopTimer() {
        stopTimer = true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        slider.setValue(model.getCurrentTimeFrame());
        slider.setMaximum(model.getTotalFrames());
    }
}
