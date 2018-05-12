import java.util.ArrayList;
import java.util.List;

public class Test {
	
		private List<Place> testRegister = new ArrayList<>();
		
		public void addPlace(Place p) {
			testRegister.add(p);
		}
		
		public void getMarked() {
			testRegister.forEach(p -> p.setVisible(false));
			testRegister.forEach(p -> p.requestFocusInWindow());
		}
		
}
