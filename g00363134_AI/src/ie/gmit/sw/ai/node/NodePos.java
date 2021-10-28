package ie.gmit.sw.ai.node;

public class NodePos implements Node{
	private int row;
	private int col;
	
	public void setPos(int row, int col) {
		this.setRow(row);
		this.setCol(col);
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}

}
