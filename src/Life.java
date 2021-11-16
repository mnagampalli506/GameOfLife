import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import processing.core.PApplet;

/**
 * 
 * Represents a Game Of Life grid.
 * 
 * Coded by: Modified on:
 * 
 */

public class Life {

	// Add a 2D array field to represent the Game Of Life grid.

	private boolean[][] grid;

	/**
	 * Initialized the Game of Life grid to an empty 20x20 grid.
	 */
	public Life() {
		grid = new boolean[20][20];

	}

	/**
	 * Initializes the Game of Life grid to a 20x20 grid and fills it with data from
	 * the file given.
	 * 
	 * @param filename The path to the text file.
	 */
	public Life(String filename) {
		grid = new boolean[20][20];
		this.readData(filename, grid);
		System.out.println(this);
	}

	/**
	 * Runs a single step within the Game of Life by applying the Game of Life rules
	 * on the grid.
	 */
	public void step() {
		int neighbors;
		boolean copygrid[][] = new boolean[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				copygrid[i][j] = grid[i][j];
			}
		}
		// goes through the entire grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {

				// you need to check the points that are 1 unit away from the thing
				neighbors = 0;
				for (int x = i - 1; x <= i + 1; x++) {
					if (x < grid.length && x >= 0) {
						for (int y = j - 1; y <= j + 1; y++) {
							if (y < grid[i].length && y >= 0 && grid[x][y]) {
								if (x != i || y != j) {
									neighbors++;
								}
							}
						}
					}
				}

				if (neighbors == 3) {
					copygrid[i][j] = true;
				} else if (neighbors <= 1 || neighbors >= 4) {
					copygrid[i][j] = false;
				}

			}
		}

		grid = copygrid;

	}

	/**
	 * Runs n steps within the Game of Life.
	 * 
	 * @param n The number of steps to run.
	 */
	public void step(int n) {
		while (n > 0) {
			step();
			n--;
		}
	}

	/**
	 * Formats this Life grid as a String to be printed (one call to this method
	 * returns the whole multi-line grid)
	 * 
	 * @return The grid formatted as a String.
	 */
	public String toString() {
		String output = "";
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j]) {
					output += "*";
				} else {
					output += "-";
				}
			}
			output += "\n";
		}
		return output;
	}

	/**
	 * (Graphical UI) Draws the grid on a PApplet. The specific way the grid is
	 * depicted is up to the coder.
	 * 
	 * @param marker The PApplet used for drawing.
	 * @param x      The x pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param y      The y pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param width  The pixel width of the grid drawing.
	 * @param height The pixel height of the grid drawing.
	 */
	public void draw(PApplet marker, float x, float y, float width, float height) {

		marker.noFill();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j]) {
					marker.fill(255, 255, 0);
				} else {
					marker.fill(100);
				}
				float rectWidth = width / grid[0].length;
				float rectHeight = height / grid.length;
				float rectX = x + (j * rectWidth);
				float rectY = y + (i * rectHeight);
				marker.rect(rectX, rectY, rectWidth, rectHeight);

			}
		}
	}

	/**
	 * (Graphical UI) Determines which element of the grid matches with a particular
	 * pixel coordinate. This supports interaction with the grid using mouse clicks
	 * in the window.
	 * 
	 * @param p      A Point object containing a graphical pixel coordinate.
	 * @param x      The x pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param y      The y pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param width  The pixel width of the grid drawing.
	 * @param height The pixel height of the grid drawing.
	 * @return A Point object representing a coordinate within the game of life
	 *         grid.
	 */
	public Point clickToIndex(Point p, float x, float y, float width, float height) {
		float cellWidth = width / grid[0].length;
		float cellHeight = height / grid.length;
		if (p.x >= 0 && p.x <= (grid.length * cellWidth) && p.y >= 0 && p.y <= (grid[0].length * cellHeight)) {
			int gridCountX = (int) ((p.x - x) / cellWidth);
			int gridCountY = (int) ((p.y - y) / cellHeight);
			if (p.x != 0 && (p.x % cellWidth) == 0) {
				gridCountX--;
			}
			if (p.y != 0 && (p.y % cellHeight) == 0) {
				gridCountY--;
			}
			return new Point(gridCountX, gridCountY);
		} else {
			return null;
		}
	}

	/**
	 * (Graphical UI) Toggles a cell in the game of life grid between alive and
	 * dead. This allows the user to modify the grid.
	 * 
	 * @param i The x coordinate of the cell in the grid.
	 * @param j The y coordinate of the cell in the grid.
	 */
	public void toggleCell(int i, int j) {
		if (grid[j][i]) {
			grid[j][i] = false;
		} else {
			grid[j][i] = true;
		}

	}

	// Reads in array data from a simple text file containing asterisks (*)
	public void readData(String filename, boolean[][] gameData) {
		File dataFile = new File(filename);

		if (dataFile.exists()) {
			int count = 0;

			FileReader reader = null;
			Scanner in = null;
			try {
				reader = new FileReader(dataFile);
				in = new Scanner(reader);

				while (in.hasNext()) {
					String line = in.nextLine();
					for (int i = 0; i < line.length(); i++)
						if (count < gameData.length && i < gameData[count].length && line.charAt(i) == '*')
							gameData[count][i] = true;

					count++;
				}
			} catch (IOException ex) {
				throw new IllegalArgumentException("Data file " + filename + " cannot be read.");
			} finally {
				if (in != null)
					in.close();
			}

		} else {
			throw new IllegalArgumentException("Data file " + filename + " does not exist.");
		}
	}

}