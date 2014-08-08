import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SketchCanvas extends JComponent {

    private Model model;
    private Drawing currentDrawing;
    private Polygon selectedCircle;
    private ArrayList<Drawing> selectedDrawings = new ArrayList<Drawing>();

    private Cursor pencil;
    private Cursor erasingCursor;
    private Cursor wand;
    private Cursor hand;

    private int dragCounter = 0;
    private Point previousDragPoint = new Point();
    private boolean stopTimer = true;
    private Timer timer;

    public SketchCanvas(Model model) {
        this.model = model;
        addListeners();

        pencil = Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon("buttons/pencil.png").getImage(),
                new Point(0, 0), "pencil");
        erasingCursor = getToolkit().createCustomCursor(
                new ImageIcon("buttons/shape_square.png").getImage(),
                new Point(0, 0), "eraser");
        wand = getToolkit().createCustomCursor(
                new ImageIcon("buttons/wand.png").getImage(),
                new Point(0, 0), "wand");
        hand = getToolkit().createCustomCursor(
                new ImageIcon("buttons/arrow_inout.png").getImage(),
                new Point(0, 0), "hand");
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                Point currentPoint = e.getPoint();
                previousDragPoint = currentPoint;

                if (model.isDrawing()) {
                    currentDrawing = new Drawing(model.getTotalFrames(), model.getColor(), model.getStroke());
                    currentDrawing.addPoint(currentPoint);
                    model.addDrawing(currentDrawing);
                } else if (model.isErasing()) {
                    eraseDrawing(currentPoint);
                } else if (model.isSelecting()) {
                    currentDrawing = new Drawing(model.getTotalFrames(), model.getColor(), model.getStroke());
                    currentDrawing.addPoint(currentPoint);
                    model.addSelection(currentDrawing);
                } else if (model.isTransitioning()) {
                    previousDragPoint = e.getPoint();
                    if (selectedCircle.contains(e.getPoint())) {
                        stopTimer = false;
                        p = new Point(e.getPoint());
                        timer = new java.util.Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                if (stopTimer) {
                                    timer.cancel();
                                } else {
                                    model.transitionDrawing(p, previousDragPoint, selectedDrawings);
                                    int deltaX = (int) (previousDragPoint.getX() - p.getX());
                                    int deltaY = (int) (previousDragPoint.getY() - p.getY());
                                    selectedCircle.translate(deltaX, deltaY);
                                    p = new Point(previousDragPoint);
                                }
                            }
                        },0,150);
                    }
                } else if (model.isAnimating()) {
                    model.setToPrevState();
                }
            }

            Point p = new Point();

            @Override
            public void mouseReleased(MouseEvent e) {
                if (model.isSelecting()) {
                    selectedCircle = new Polygon();
                    for (Point p : model.getSelectionPoints().getPoints()) {
                        selectedCircle.addPoint((int) p.getX(), (int) p.getY());
                    }
                    selectDrawingsWithinCircle(selectedCircle);
                } else if (model.isTransitioning()) {
                    if (stopTimer) {
                        model.clearStateAndUpdate();
                        model.setStateSelecting();
                        selectedDrawings.clear();
                    }
                    stopTimer = true;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();

                //dragCounter is used reduce number of stored points resulting in smoother drawings
                if (model.isDrawing() && dragCounter %3 == 0) {
                    currentDrawing.addPoint(currentPoint);
                    repaint();
                } else if (model.isErasing()) {
                    eraseDrawing(currentPoint);
                } else if (model.isSelecting() && dragCounter %3 == 0) {
                    currentDrawing.addPoint(currentPoint);
                    repaint();
                }

                dragCounter++;
                previousDragPoint = currentPoint;
            }
        });
    }

    private void selectDrawingsWithinCircle(Polygon selectedCircle) {
        selectedDrawings = new ArrayList<Drawing>();
        for (Drawing drawing: model.getDrawings()) {
            boolean containsDrawing = true;
            for (Point p: drawing.getTransitionedPoints(model.getCurrentTimeFrame())) {
                if (!selectedCircle.contains(p)) {
                    containsDrawing = false;
                    break;
                }
            }
            if (containsDrawing) {
                selectedDrawings.add(drawing);
            }
            if (selectedDrawings.size() > 0) {
                model.setStateTransitioning();
            }
        }
    }

    private void eraseDrawing(Point end) {
        ArrayList<Drawing> drawingsToErase = new ArrayList<Drawing>();
        for (Drawing drawing: model.getDrawings()) {
            if (drawing.intersecting(previousDragPoint, end, model.getCurrentTimeFrame())) {
                drawingsToErase.add(drawing);
            }
        }
        model.eraseDrawings(drawingsToErase);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Drawing drawing: model.getDrawings()) {
            drawing.draw(g2, model.getCurrentTimeFrame());
        }

        setCursor();

        if (model.isSelecting() || model.isTransitioning()) {
            model.getSelectionPoints().drawSelected(g2);
            for (Drawing drawing : selectedDrawings) {
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                drawing.drawSelectedObject(g2, model.getCurrentTimeFrame());
            }
        } else {
            selectedDrawings.clear();
        }
    }

    private void setCursor() {
        if (model.isDrawing()) {
            setCursor(pencil);
        } else if (model.isErasing()) {
            setCursor(erasingCursor);
        } else if (model.isSelecting()) {
            setCursor(wand);
        } else if (model.isTransitioning()) {
            setCursor(hand);
        }
    }
}
