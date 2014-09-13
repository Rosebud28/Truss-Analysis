
public class Matrix {
	double[][] matrix;
	int rows,cols;
	public Matrix(int rows, int cols){
		this.rows = rows;
		this.cols = cols;
		matrix = new double[rows][cols];
	}
	
	public void setElement(double value,int r,int c){
		matrix[r][c] = (int)(value*10000)/10000.0;
	}
	
	public double getElement(int r,int c){
		return matrix[r][c];
	}
	
	public int getRows(){
		return rows;
	}
	
	public int getCols(){
		return cols;
	}
	
	public void swapRows(int a, int b){
		double[] temp = new double[cols];
		for(int i=0;i<cols;i++){
			temp[i] = matrix[a][i];
			matrix[a][i] = matrix[b][i];
		}
		matrix[b] = temp;
	}
	
	public Matrix add(Matrix B){
		if(B.rows==rows&&B.cols==cols){
			Matrix newMatrix = new Matrix(rows,cols);
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					newMatrix.setElement(this.getElement(i, j)+B.getElement(i, j), i, j);
				}
			}
			return newMatrix;
		}
		else return null;
	}
	
	public Matrix subtract(Matrix B){
		if(B.rows==rows&&B.cols==cols){
			Matrix newMatrix = new Matrix(rows,cols);
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					newMatrix.setElement(this.getElement(i, j)-B.getElement(i, j), i, j);
				}
			}
			return newMatrix;
		}
		else return null;
	}
	
	public Matrix multiply(Matrix B){
		if(B.rows == cols){
			Matrix newMatrix = new Matrix(rows,B.cols);
			for(int i=0;i<rows;i++){
				for(int j=0;j<B.cols;j++){
					double sum =0;
					for(int k=0;k<cols;k++){
						sum+=(this.getElement(i, k)*B.getElement(k, j));
					}
					newMatrix.setElement(sum, i, j);
				}
			}
			return newMatrix;
		}
		else return null;
	}
	
	public double[] solveAugmented(){
		if(cols == rows+1){
			Matrix newMatrix = this.clone();
			for (int j=0;j<cols-1;j++){
				double max = newMatrix.getElement(j, j);
				int index = j;
				for(int i=j+1;i<rows;i++){
					if(max<newMatrix.getElement(i, j)){
						max = newMatrix.getElement(i, j);
						index = i;
					}
				}
				newMatrix.swapRows(index, j);
				for(int i=j+1;i<rows;i++){
					double factor = newMatrix.getElement(i, j)/max;
					for(int k=j;k<cols;k++){
						newMatrix.setElement(newMatrix.getElement(i, k) - newMatrix.getElement(j, k)*factor, i, k);
					}
				}
			}
			double[] solution = new double[rows];
			for(int i=rows-1;i>=0;i--){
				for(int j=cols-2;j>i;j--){
					newMatrix.setElement(newMatrix.getElement(i, cols-1)-solution[j]*newMatrix.getElement(i, j), i,cols-1);
				}
				solution[i] = newMatrix.getElement(i, cols-1)/newMatrix.getElement(i,i);
			}
			return solution;
		}
		return null;
	}
	
	public Matrix clone(){
		Matrix newMatrix = new Matrix(rows,cols);
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				newMatrix.setElement(this.getElement(i, j),i,j);
			}
		}
		return newMatrix;
	}
	
}
