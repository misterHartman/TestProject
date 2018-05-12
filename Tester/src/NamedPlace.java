import javax.swing.JOptionPane;

public class NamedPlace extends Place {

	public NamedPlace(int x, int y, String name, int category, Position position) {
		super(x, y, name, category, position);
	}

	@Override
	public void placeInfo() {
		JOptionPane.showMessageDialog(NamedPlace.this, super.getName() + " " + super.getPosition(), "Platsinfo:",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
