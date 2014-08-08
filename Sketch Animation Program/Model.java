import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Model {
    private ArrayList<Drawing> drawings = new ArrayList<Drawing>();
    private ArrayList<JComponent> views = new ArrayList<JComponent>();
    private Drawing selectionPoints;
    private Color selectedColor;
    private Stroke selectedStroke;

    public Model() {
        selectedColor = Color.black;
        selectedStroke = new BasicStroke(1f);
        selectionPoints = new Drawing(totalFrames, selectedColor, selectedStroke);
    }

    // --- drawing options

    public void setColorBlack() {
        selectedColor = Color.BLACK;
        setStateDrawing();
    }


    public void setColorRed() {
        selectedColor = Color.RED;
        setStateDrawing();
    }


    public void setColorGreen() {
        selectedColor = Color.GREEN;
        setStateDrawing();
    }


    public void setColorBlue() {
        selectedColor = Color.BLUE;
        setStateDrawing();
    }

    public Color getColor() {
        return selectedColor;
    }

    public void setStrokeNormal() {
        selectedStroke = new BasicStroke(1f);
        setStateDrawing();
    }

    public void setStrokeMedium() {
        selectedStroke = new BasicStroke(3.0f);
        setStateDrawing();
    }

    public void setStrokeMax() {
        selectedStroke = new BasicStroke(5.0f);
        setStateDrawing();
    }

    public Stroke getStroke() {
        return selectedStroke;
    }
    //

    // --- states of the program
    private DrawingState state = DrawingState.DRAWING;
    private DrawingState prevState;

    private enum DrawingState {
        DRAWING, SELECTING, ERASING, TRANSITIONING, ANIMATING;

    }

    public void setToPrevState() {
        if (isAnimating() && prevState != DrawingState.TRANSITIONING) {
            state = prevState;
        } else if (isAnimating() && prevState == DrawingState.TRANSITIONING) {
            setStateSelecting();
        }
    }

    public void clearStateAndUpdate() {
        selectionPoints = new Drawing(totalFrames, Color.BLACK, new BasicStroke(1));
        updateViews();
    }

    public boolean isDrawing() {
        return (state == DrawingState.DRAWING);
    }

    public boolean isErasing() {
        return (state == DrawingState.ERASING);
    }

    public boolean isSelecting() {
        return (state == DrawingState.SELECTING);
    }

    public boolean isTransitioning() {
        return (state == DrawingState.TRANSITIONING);
    }

    public boolean isAnimating() {
        return (state == DrawingState.ANIMATING);
    }

    public void setStateDrawing() {
        state = DrawingState.DRAWING;
        clearStateAndUpdate();
    }

    public void setStateErasing() {
        state = DrawingState.ERASING;
        clearStateAndUpdate();
    }

    public void setStateSelecting() {
        state = DrawingState.SELECTING;
        updateViews();
    }

    public void setStateTransitioning() {
        state = DrawingState.TRANSITIONING;
        updateViews();
    }

    public void setStateAnimating() {
        prevState = state;
        state = DrawingState.ANIMATING;
        clearStateAndUpdate();
    }

    // --- timeframes
    private int currentTimeFrame = 0;
    private int totalFrames = 101;

    public int getCurrentTimeFrame() {
        return currentTimeFrame;
    }

    public void setCurrentTimeFrame(int currentTimeFrame) {
        this.currentTimeFrame = currentTimeFrame;
        if (isTransitioning()) {
            setStateAnimating();
        }
        clearStateAndUpdate();
    }

    public void incrementTimeFrame() {
        if (currentTimeFrame < totalFrames-1) {
            currentTimeFrame++;
            clearStateAndUpdate();
        }
        if (isTransitioning()) {
            setStateAnimating();
        }
    }

    public void decrementTimeFrame() {
        if (currentTimeFrame > 0) {
            currentTimeFrame--;
            clearStateAndUpdate();
        }
        if (isTransitioning()) {
            setStateAnimating();
        }
    }

    public void incrementForSelect() {
        currentTimeFrame++;
        updateViews();
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void addFrame() {
        for (Drawing d: drawings) {
            d.addTransitionFrame(currentTimeFrame);
        }
        totalFrames++;
        incrementTimeFrame();
    }

    // --- drawings

    public ArrayList<Drawing> getDrawings() {
        ArrayList<Drawing> visibleDrawings = new ArrayList<Drawing>();
        for (Drawing d: drawings) {
            if (d.isVisible(currentTimeFrame)) {
                visibleDrawings.add(d);
            }
        }
        return visibleDrawings;
    }

    public void addDrawing(Drawing drawing) {
        drawings.add(drawing);
        drawing.setDrawnTimeFrame(currentTimeFrame);
        updateViews();
    }

    public void eraseDrawings(ArrayList<Drawing> erasableDrawings) {
        for (Drawing d: erasableDrawings) {
            d.erase(currentTimeFrame);
        }
        updateViews();
    }

    public void addSelection(Drawing selection) {
        selectionPoints = selection;
        updateViews();
    }

    public Drawing getSelectionPoints() {
        return selectionPoints;
    }

    public void transitionDrawing(Point point1, Point point2, ArrayList<Drawing> selectedDrawings) {
        if (currentTimeFrame+1 < totalFrames) {
            for (Drawing d : selectedDrawings) {
                d.redraw(currentTimeFrame);
                d.transitionPoints(currentTimeFrame, point1, point2);
            }
            selectionPoints.transitionFrame(point1, point2);
            incrementForSelect();
        }
    }


    // --- views

    public void addViews(JComponent view) {
        views.add(view);
    }

    private void updateViews() {
        for (JComponent component: views) {
            component.repaint();
        }
    }


}
