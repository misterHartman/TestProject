import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;

abstract public class Place extends JComponent {
	private Position position;
	private String name;
	private int category;
	private TriangleListener triangleListener = new TriangleListener();
	private boolean inFocus = false;

	public Place(int x, int y, String name, int category, Position position) {
		this.name = name;
		this.position = position;
		this.category = category;
		setBounds((x - 11), (y - 23), 22, 22);
		setPreferredSize(new Dimension(x, y));
		addMouseListener(triangleListener);
		addFocusListener(new MarkedListener());
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int[] xPoints = { 0, 11, 22 };
		int[] yPoints = { 0, 23, 0 };

		if (category == -1) {
			g.setColor(Color.BLACK);
		} else if (category == 0) {
			g.setColor(Color.RED);
		} else if (category == 1) {
			g.setColor(Color.BLUE);
		} else if (category == 2) {
			g.setColor(Color.GREEN);
		}
		g.fillPolygon(xPoints, yPoints, 3);

		if (inFocus) {
			int[] xwPoints = { 0, 11, 22 };
			int[] ywPoints = { 0, 23, 0 };
			g.setColor(Color.YELLOW);
			g.drawPolygon(xwPoints, ywPoints, 3);

		}

	}

	public String getName() {
		return name;
	}

	public Position getPosition() {
		return position;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public boolean getInFocus() {
		return inFocus;
	}

	public void setFocus(boolean inFocus) {
		this.inFocus = inFocus;
	}

	public abstract void placeInfo();

	class TriangleListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent mev) {

			if (SwingUtilities.isRightMouseButton(mev)) {
				placeInfo();
				return;

			}
			if (isFocusOwner()) {
				inFocus = false;
				setFocusable(false);
				requestFocusInWindow();
				

			} else if (!isFocusOwner()) {

				setFocusable(true);
				requestFocusInWindow();
			}

		}
	}

	class MarkedListener implements FocusListener {

		public void focusGained(FocusEvent fev) {
			inFocus = true;
			validate();
			repaint();
			
		}

		public void focusLost(FocusEvent fev) {
			validate();
			repaint();
			
		}

	}

}
