
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBisectingQuadrilaterals {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int caseNum = 1;
		End:
		while (true)
		{
			List<Point2D.Double> quad = new ArrayList<Point2D.Double>();
			for (int i = 0; i < 4; ++i)
			{
				int x = in.nextInt();
				int y = in.nextInt();
				if (x == -1 && y == -1)
				{
					break End;
				}
				quad.add(new Point2D.Double(x, y));
			}
			int slope = in.nextInt();
			double areaDiv2 = shoelace(quad) / 2;
			
			double prevGuess = Long.MAX_VALUE, lower = Long.MAX_VALUE, upper = Long.MIN_VALUE, guess = Long.MIN_VALUE;
			for (int i = 0; i < quad.size(); ++i)
			{
				double b = quad.get(i).y - slope * quad.get(i).x;
				if (b < lower)
				{
					lower = b;
				}
				if (b > upper)
				{
					upper = b;
				}
			}
			
			while (Math.abs(prevGuess - guess) > .00000001)
			{
				prevGuess = guess;
				guess = (upper + lower) / 2;
				Point2D.Double[] interSPoint = new Point2D.Double[4];
				int minXIndex = 4;
				double minX = Double.MAX_VALUE;
				for (int i = 0; i < quad.size(); ++i)
				{
					Point2D.Double one = quad.get(i), two = quad.get((i + 1) % quad.size());
					double x = intersects(slope, guess, one, two);
					double y = slope * x + guess;
					if (x >= Math.min(one.x, two.x) && x <= Math.max(one.x, two.x) && y >= Math.min(one.y, two.y) && y <= Math.max(one.y, two.y))
					{
						interSPoint[i] = new Point2D.Double(x, y);
						if (x < minX)
						{
							minX = x;
							minXIndex = i;
						}
					}
				}
				List<Point2D.Double> subPolygon = new ArrayList<Point2D.Double>();
				int counter = 0;
				for (int i = 0, index = minXIndex; i < 4; ++i, index = (index + 1) % 4)
				{
					if (interSPoint[index] != null)
					{
						subPolygon.add(interSPoint[index]);
						++counter;
						if (counter == 2)
						{
							break; 
						}
					}
					subPolygon.add(quad.get((index + 1) % 4));
				}
				double area = shoelace(subPolygon);
//				System.out.println(area);
				if (area > areaDiv2)
				{
					lower = guess;
				}
				else
				{
					upper = guess;
				}
			}
			
			System.out.printf("Case " + caseNum++ + ": %.5f\n", Math.round(guess * 100000) / 100000.0);
		}
	}
	
	public static double intersects(int m, double b, Point2D.Double one, Point2D.Double two)
	{
		if (one.x == two.x)
		{
			return one.x;
		}
		else
		{
			double m2 = (double)(two.y - one.y) / (two.x - one.x);
			if (m2 == m)
			{
				return -1;	//if they have the same slope, there are no intersects. -1 will always be outside quadrilateral range.
			}
			double b2 = -m2 * one.x + one.y;
			return (b2 - b) / (m - m2);
		}
	}
	
	public static double shoelace(List<Point2D.Double> polygon)
	{
		double area = 0;
		for (int i = 0; i < polygon.size(); ++i)
		{
			Point2D.Double pt1 = polygon.get(i);
			Point2D.Double pt2 = polygon.get((i + 1) % polygon.size());
			area += pt1.x * pt2.y - pt2.x * pt1.y;
		}
		return Math.abs(area) / 2.0;
	}
}
