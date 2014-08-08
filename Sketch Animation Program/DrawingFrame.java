import java.awt.*;

public class DrawingFrame {
    private boolean visible;
    private Point transition;

    public DrawingFrame() {
        visible = true;
        transition = new Point(0,0);
    }

    public boolean isVisible() {
        return visible;
    }

    public Point getTransition() {
        return transition;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setTransition(Point transition) {
        this.transition = transition;
    }
}
