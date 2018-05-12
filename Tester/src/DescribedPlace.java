import javax.swing.*;

public class DescribedPlace extends Place {
	private String description;

	public DescribedPlace(int x, int y, String name, String description, int category, Position position) {
		super(x, y, name, category, position);
		this.description = description;
	
	}
	public String getDescription() {
		return description;
	}

	@Override
	public void placeInfo() {
		DescribedPlaceInfo dpi = new DescribedPlaceInfo(DescribedPlace.this);
		JOptionPane.showMessageDialog(DescribedPlace.this, dpi, "Platsinfo:",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	class DescribedPlaceInfo extends JPanel {
		
		private JTextField nameField = new JTextField(10);
		private JTextField descriptionField = new JTextField(30);
		
		public DescribedPlaceInfo(DescribedPlace dp) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JPanel name = new JPanel();
			name.add(new JLabel("Namn: "));
			name.add(nameField);
			nameField.setText(dp.getName());
			add(name);
			JPanel description = new JPanel();
			description.add(new JLabel("Beskrivning: "));
			description.add(descriptionField);
			descriptionField.setText(dp.getDescription());
			add(description);
			nameField.setEditable(false);
			descriptionField.setEditable(false);
		}
	}
}
