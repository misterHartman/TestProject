import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.*;
import java.util.List;

public class MainWindow extends JFrame {

	private final String[] BUTTONS = { "Search", "Hide", "Remove", "Coordinates" };
	private JTextField searchField = new JTextField("Search", 10);
	private MapPanel mp = null;
	private JFileChooser jfc = new JFileChooser(".");
	private JButton newButton;
	private MouseListener mouseListener = new MouseListener();
	private JScrollPane scroll = null;
	private JPanel east = new JPanel();
	private JPanel north = new JPanel();
	private String[] categories = { "Bus", "Underground", "Train" };
	private JList<String> jList = new JList<String>(categories);
	private JRadioButton namedButton = new JRadioButton("Named", true);
	private JRadioButton describedButton = new JRadioButton("Described");
	private ButtonGroup bg = new ButtonGroup();
	private int category = -1;

	private Map<Integer, Set<Place>> categoryMap = new HashMap<>();
	private Map<Position, Place> positionMap = new HashMap<>();
	private Map<String, Set<Place>> nameMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private ArrayList<Place> markedList = new ArrayList<Place>();
	private Map<Place, Place> markedMap = new HashMap<>();

	public MainWindow() {
		super("Inlupp 2");
		super.setMinimumSize(new Dimension(750, 250));

		JMenuBar mb = new JMenuBar();
		JMenu iMen = new JMenu("Archive");
		JMenuItem newMap = iMen.add("New Map");
		newMap.addActionListener(new NewMapListener());
		JMenuItem loadPlaces = iMen.add("Load Places");
		JMenuItem save = iMen.add("Save");
		JMenuItem exit = iMen.add("Exit");
		setJMenuBar(mb);
		mb.add(iMen);

		add(north, BorderLayout.NORTH);
		newButton = new JButton("New");
		north.add(newButton);
		newButton.addActionListener(new NewListener());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(namedButton);
		buttonPanel.add(describedButton);
		north.add(buttonPanel, BorderLayout.EAST);
		bg.add(namedButton);
		bg.add(describedButton);
		north.add(searchField);
		searchField.addFocusListener(new SearchListener());

		FileFilter ff = new FileNameExtensionFilter("Pictures", "jpg", "gif", "png");
		jfc.setFileFilter(ff);
		ButtonListener bl = new ButtonListener();
		for (String butt : BUTTONS) {
			JButton b = new JButton(butt);
			north.add(b);
			b.addActionListener(bl);
		}

		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		add(east, BorderLayout.EAST);
		east.add(new JLabel("Categories"));
		east.add(new JScrollPane(jList));
		jList.addMouseListener(new JListListener());

		JButton hideCategoryButton = new JButton("Hide category");
		east.add(hideCategoryButton);
		hideCategoryButton.addActionListener(new HideCategoryListener());

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(630, 220);
		setLocation(250, 40);
		validate();
		repaint();
		setVisible(true);

	}

	public String getSearch() {
		return searchField.getText();
	}

	class NewListener implements ActionListener {

		public void actionPerformed(ActionEvent ave) {
			if (mp != null) {
				mp.addMouseListener(mouseListener);
				newButton.setEnabled(false);
				mp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		}
	}

	class MouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			int x = mev.getX();
			int y = mev.getY();
			Position position = new Position(x, y);
			category = jList.getSelectedIndex();
			Set<Place> places = categoryMap.get(category);
			if (places == null) {
				places = new HashSet<Place>();
				categoryMap.put(category, places);

			}

			if (x < mp.getW() && y < mp.getH()) {

				boolean emptyNameField = true;

				if (describedButton.isSelected()) {
					DescribedPlaceForm dpf = new DescribedPlaceForm();

					while (emptyNameField) {

						int answer = JOptionPane.showConfirmDialog(MainWindow.this, dpf, "Ny plats",
								JOptionPane.OK_CANCEL_OPTION);
						if (answer != JOptionPane.OK_OPTION)
							break;
						if (dpf.getName().equals("")) {
							JOptionPane.showMessageDialog(MainWindow.this, "Platsen måste ha ett namn", "Fel",
									JOptionPane.ERROR_MESSAGE);

						} else {

							DescribedPlace dp = new DescribedPlace(x, y, dpf.getName(), dpf.getDescription(), category,
									position);

							places.add(dp);
							mp.add(dp);
							Set<Place> namedPlaces = categoryMap.get(category);
							if (namedPlaces == null) {
								namedPlaces = new HashSet<Place>();

							}

							nameMap.put(dp.getName(), namedPlaces);
							positionMap.put(position, dp);
							markedMap.put(dp, dp);
							dp.addFocusListener(new MarkListener());
							emptyNameField = false;
						}
					}
				}

				if (namedButton.isSelected()) {
					NamedPlaceForm npf = new NamedPlaceForm();

					while (emptyNameField) {
						int answer = JOptionPane.showConfirmDialog(MainWindow.this, npf, "Ny plats",
								JOptionPane.OK_CANCEL_OPTION);
						if (answer != JOptionPane.OK_OPTION)
							return;
						if (npf.getName().equals("")) {
							JOptionPane.showMessageDialog(MainWindow.this, "Platsen m�ste ha ett namn", "Fel",
									JOptionPane.ERROR_MESSAGE);

						} else {

							NamedPlace np = new NamedPlace(x, y, npf.getName(), category, position);
							places.add(np);
							mp.add(np);
							Set<Place> namedPlaces = categoryMap.get(category);
							if (namedPlaces == null) {
								namedPlaces = new HashSet<Place>();
							}
							nameMap.put(np.getName(), namedPlaces);
							positionMap.put(position, np);
							markedMap.put(np, np);
							np.addFocusListener(new MarkListener());
							emptyNameField = false;
						}

					}
				}

				validate();
				repaint();
				mp.removeMouseListener(mouseListener);
				newButton.setEnabled(true);
				mp.setCursor(Cursor.getDefaultCursor());
				jList.clearSelection();

			} else {
				JOptionPane.showMessageDialog(MainWindow.this, "Du kan bara skapa platser på kartan!", "Fel",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class NewMapListener implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			int answer = jfc.showOpenDialog(MainWindow.this);

			if (answer != JFileChooser.APPROVE_OPTION) {
				return;
			}

			if (mp != null) {
				remove(scroll);

			}
			File file = jfc.getSelectedFile();
			mp = new MapPanel(file.getAbsolutePath());
			scroll = new JScrollPane(mp);
			add(scroll, BorderLayout.CENTER);
			setSize((175 + mp.getW()), (125 + mp.getH()));
			validate();
			repaint();
		}

	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			if (mp != null) {
				try {

					JButton b = (JButton) ave.getSource();
					String str = b.getText();

					if (str.equals("Search")) {

						String name = searchField.getText();
						Set<Place> namedPlaces = nameMap.get(name);

						if (!namedPlaces.isEmpty()) {
							namedPlaces.forEach(p -> p.setVisible(true));
							namedPlaces.forEach(p -> p.requestFocusInWindow());

						} else {
							JOptionPane.showMessageDialog(MainWindow.this, "Finns inga platser med det namnet", "Fel",
									JOptionPane.ERROR_MESSAGE);
							searchField.setText("Search");

						}
					} else if (str.equals("Hide")) {

						if (!markedList.isEmpty()) {
							for (Place p : markedList) {
								if (p.getInFocus() == true) {
									p.setVisible(false);
									p.requestFocusInWindow();

								}
							}
						}

					} else if (str.equals("Remove")) {

						if (!markedList.isEmpty()) {
							for (Place p : markedList) {
								if (p.getInFocus() == true) {
									Set<Place> namedPlaces = nameMap.get(p.getName());
									for (Place place : namedPlaces) {
										if (place.equals(p)) {
											nameMap.get(place.getName()).remove(p);
											return;
										}
									}
									categoryMap.remove(p.getCategory());
									positionMap.remove(p.getPosition());
									markedMap.remove(p);
									mp.remove(p);

								}

							}
							validate();
							repaint();
							markedList.clear();
						}

					} else if (str.equals("Coordinates")) {
						InputCoordinates ic = new InputCoordinates();
						int answer = JOptionPane.showConfirmDialog(MainWindow.this, ic, "Input coordinates",
								JOptionPane.OK_CANCEL_OPTION);
						if (answer != JOptionPane.OK_OPTION)
							return;

					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(MainWindow.this, "Fel inmatning!", "Fel", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}

	class JListListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {

			if (SwingUtilities.isRightMouseButton(mev)) {
				jList.clearSelection();
				return;
			}
			category = jList.getSelectedIndex();
			Set<Place> places = categoryMap.get(category);

			if (places != null) {
				places.forEach(p -> p.setVisible(true));

			}
		}
	}

	class HideCategoryListener implements ActionListener {

		public void actionPerformed(ActionEvent ave) {

			category = jList.getSelectedIndex();
			if (category >= 0) {
				Set<Place> places = categoryMap.get(category);

				if (places != null) {
					places.forEach(p -> p.setVisible(false));

				}
			}
		}
	}

	class SearchListener implements FocusListener {

		public void focusGained(FocusEvent fev) {

			searchField.setText("");

		}

		public void focusLost(FocusEvent fev) {
			String s = searchField.getText();
			if (s.equals("")) {
				searchField.setText("Search");
			}
		}
	}

	class MarkListener implements FocusListener {

		public void focusGained(FocusEvent fev) {

			Place place = (Place) fev.getSource();
			place = markedMap.get(place);
			markedList.add(place);

		}

		public void focusLost(FocusEvent fev) {

		}

	}
}
