public class Position {
	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public int getXPoint() {
		return x;
	}

	public int getYPoint() {
		return y;
	}
	
	@Override
	public String toString() {
		return "{" + getXPoint() + ", " + getYPoint() + "}";
	}
	
}
