package VisualProcessing;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Matrix {
	
	private int rows, colums;
	private float[][] data;
	
	public Matrix(int rows, int colums) {
		this.rows = rows;
		this.colums = colums;
		data = new float[rows][colums];
	}

	public Matrix(float[][] data) {
		this.data = data;
		rows = data.length;
		colums = data[0].length;
	}
	
	public void setValue(int row, int column, float value) {
		data[row][column] = value;
	}
	
	public float getValue(int row, int column) {
		return data[row][column];
	}
	
	public static Matrix multiply(Matrix m1, Matrix m2) {
		Matrix result = new Matrix(m1.rows, m2.colums);
		
		for(int i = 0; i < result.rows;i++) {
			for(int j=0;j < result.colums; j++) {
				float sum = 0;
				for(int k=0;k<m1.colums;k++) {
					sum += m1.getValue(i, k) * m2.getValue(k, j);
				}
				result.setValue(i, j, sum);
			}
		}
		
		return result;
	}
	
	public Matrix copy() {
		float[][] dataCopy = data;
		
		return new Matrix(dataCopy);
	}
	public static float[][] copyAFloat(float[][] a){
		int height = a.length;
		int width = a[1].length;
		float[][] out = new float[height][width];
		for(int i = 0; i < height;i++){
			for(int j = 0; j < width; j++){
				out[i][j] = a[i][j];
			}
		}
		return out;
	}
	
	public static Matrix rotateMatrixN(Matrix m, int n) {
		float[][] dataCopy = copyAFloat(m.data);
		Matrix img = new Matrix(dataCopy);
		for(int i = 0; i < n; i++) {img = rotateMatrix(img);}
		return img;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colums;
		result = prime * result + Arrays.deepHashCode(data);
		result = prime * result + rows;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (colums != other.colums)
			return false;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		if (rows != other.rows)
			return false;
		return true;
	}

	// An Inplace function to rotate a N x N matrix
	// by 90 degrees in anti-clockwise direction
	public static Matrix rotateMatrix(Matrix m){
	    int N = 3;
	    float[][] mat = m.data;
	    for (int x = 0; x < N / 2; x++){
	        // Consider elements in group of 4 in 
	        // current square
	        for (int y = x; y < N-x-1; y++)
	        {
	            // store current cell in temp variable
	            int temp = (int) mat[x][y];
	 
	            // move values from right to top
	            mat[x][y] = mat[y][N-1-x];
	 
	            // move values from bottom to right
	            mat[y][N-1-x] = mat[N-1-x][N-1-y];
	 
	            // move values from left to bottom
	            mat[N-1-x][N-1-y] = mat[N-1-y][x];
	 
	            // assign temp to left
	            mat[N-1-y][x] = temp;
	        }
	    }
	    return new Matrix(mat);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		for(float[] arr:data) {
			s += Arrays.toString(arr) + "\n";
		}
		return s;
	}
	
	
	
	
	


}