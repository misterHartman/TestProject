import javax.swing.*;

public class DescribedPlaceForm extends JPanel {

	private JTextField nameField = new JTextField(10);
	private JTextField descriptionField = new JTextField(20);

	public DescribedPlaceForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel name = new JPanel();
		name.add(new JLabel("Namn: "));
		name.add(nameField);
		add(name);
		JPanel description = new JPanel();
		description.add(new JLabel("Beskrivning: "));
		description.add(descriptionField);
		add(description);
	}

	@Override
	public String getName() {
		return nameField.getText();
	}

	public String getDescription() {
		return descriptionField.getText();
	}

}
