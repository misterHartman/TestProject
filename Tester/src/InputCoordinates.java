import javax.swing.*;

public class InputCoordinates extends JPanel {

	private JTextField xField = new JTextField(5);
	private JTextField yField = new JTextField(5);

	public InputCoordinates() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel xrow = new JPanel();
		xrow.add(new JLabel("x: "));
		xrow.add(xField);
		add(xrow);
		JPanel yrow = new JPanel();
		yrow.add(new JLabel("y: "));
		yrow.add(yField);
		add(yrow);
	}

	public int getXCoordinate() {
		return Integer.parseInt(xField.getText());
	}

	public int getYCoordinate() {
		return Integer.parseInt(yField.getText());
	}

	public Coordinates getCoordinates() {
		int x = getXCoordinate();
		int y = getYCoordinate();
		Coordinates c = new Coordinates(x, y);
		return c;
	}
}
