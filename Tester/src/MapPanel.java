import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel{
	private ImageIcon map;
	private int w, h;
	
	MapPanel(String filename){
		
		map = new ImageIcon(filename);
		w = map.getIconWidth();
		h = map.getIconHeight();
		setLayout(null);
		setPreferredSize(new Dimension(w,h));
		setMaximumSize(new Dimension(w,h));
		setMinimumSize(new Dimension(w,h));
		setBounds(0, 0, w, h);
	}
	
	public int getW(){
		return w;
	}
	

	public int getH(){
		return h;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(map.getImage(), 0, 0, this);
		
	}
	
	public ImageIcon getMap(){
		return map;
	}
}
