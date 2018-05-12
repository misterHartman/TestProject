import javax.swing.*;

public class NamedPlaceForm extends JPanel {

	private JTextField nameField = new JTextField(10);

	public NamedPlaceForm() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel name = new JPanel();
		name.add(new JLabel("Namn: "));
		name.add(nameField);
		add(name);
	}

	@Override
	public String getName() {
		return nameField.getText();
	}

}
