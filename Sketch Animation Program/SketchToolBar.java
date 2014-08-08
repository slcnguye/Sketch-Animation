import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SketchToolBar extends JToolBar{
    private Model model;
    private ButtonGroup stateGroup = new ButtonGroup();
    private ButtonGroup colorGroup = new ButtonGroup();
    private ButtonGroup boldGroup = new ButtonGroup();
    private JToggleButton drawState;
    private JToggleButton eraseState;
    private JToggleButton selectState;
    private JToggleButton blackColor;
    private JToggleButton redColor;
    private JToggleButton blueColor;
    private JToggleButton greenColor;
    private JToggleButton normalBold;
    private JToggleButton mediumBold;
    private JToggleButton maxBold;
    private JButton addFrame;

    public SketchToolBar(Model model1) {
        this.model = model1;

        setName("Toolbar");
        setOrientation(JToolBar.HORIZONTAL);

        drawState = new JToggleButton(imageIcon("buttons/draw.png"));
        eraseState = new JToggleButton(imageIcon("buttons/eraser.png"));
        selectState = new JToggleButton(imageIcon("buttons/lasso.png"));
        blackColor = new JToggleButton(imageIcon("buttons/black.png"));
        redColor = new JToggleButton(imageIcon("buttons/red.png"));
        blueColor = new JToggleButton(imageIcon("buttons/blue.png"));
        greenColor = new JToggleButton(imageIcon("buttons/green.png"));
        normalBold = new JToggleButton();
        mediumBold = new JToggleButton();
        maxBold = new JToggleButton();

        normalBold.setText("Thin");
        mediumBold.setText("Normal");
        maxBold.setText("Bold");

        addFrame = new JButton(imageIcon("buttons/frame.png"));
        addFrame.setText("Add Frame");

        add(drawState);
        add(eraseState);
        add(selectState);
        addSeparator();
        add(blackColor);
        add(redColor);
        add(greenColor);
        add(blueColor);
        addSeparator();
        add(normalBold);
        add(mediumBold);
        add(maxBold);
        addSeparator();
        add(addFrame);

        stateGroup.add(drawState);
        stateGroup.add(eraseState);
        stateGroup.add(selectState);
        colorGroup.add(blackColor);
        colorGroup.add(redColor);
        colorGroup.add(blueColor);
        colorGroup.add(greenColor);
        boldGroup.add(normalBold);
        boldGroup.add(mediumBold);
        boldGroup.add(maxBold);

        drawState.setSelected(true);
        blackColor.setSelected(true);
        normalBold.setSelected(true);

        drawState.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStateDrawing();
            }
        });

        eraseState.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStateErasing();
            }
        });

        selectState.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStateSelecting();
            }
        });

        addFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.addFrame();
            }
        });

        blackColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setColorBlack();
            }
        });

        redColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setColorRed();
            }
        });

        blueColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setColorBlue();
            }
        });

        greenColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setColorGreen();
            }
        });

        normalBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStrokeNormal();
            }
        });

        mediumBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStrokeMedium();
            }
        });

        maxBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setStrokeMax();
            }
        });
    }

    private ImageIcon imageIcon(String path) {
        return new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(24,24,24));
    }
}
