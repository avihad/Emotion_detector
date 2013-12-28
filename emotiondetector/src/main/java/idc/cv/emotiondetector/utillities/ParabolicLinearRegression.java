package idc.cv.emotiondetector.utillities;

import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;
import org.opencv.core.Point;

import java.util.Arrays;
import java.util.Collection;

/*
* This class finds the least squares parameters of an equation of type:
* y = aX^2 + bX + c
* whereas a, b, c are the regression parameters
* */
public class ParabolicLinearRegression
{
    private ParabolicLinearRegression(){}

    /*
    * Example:
    *
        double[] y = new double[]{0.0, 12, 9, 12, 15, 24};
        double[][] x = new double[6][];
        x[0] = new double[]{0, 0, 0, 0, 0};
        x[1] = new double[]{2.0, 0, 0, 0, 0};
        x[2] = new double[]{0, 3.0, 0, 0, 0};
        x[3] = new double[]{0, 0, 4.0, 0, 0};
        x[4] = new double[]{0, 0, 0, 5.0, 0};
        x[5] = new double[]{0, 0, 0, 0, 6.0};
        regression.newSampleData(y, x);
        System.out.println(Arrays.toString(regression.estimateRegressionParameters()));
    *
    */
    public static double[] linearRegressionOf(Collection<Point> points)
    {
        return linearRegressionOf((Point[]) points.toArray());
    }

    public static double[] linearRegressionOf(Point... points)
    {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        double[] y = new double[points.length + 1];
        double[][] x = new double[points.length + 1][];
        x[0] = new double[]{0, 0, 0};

        int pointNumber = 1;
        for (Point point : points)
        {
            y[pointNumber] = point.y;
            x[pointNumber] = new double[]{Math.pow(point.x, 2), point.x, 1};
            pointNumber++;
        }

        regression.newSampleData(y, x);
        System.out.println(Arrays.toString(regression.estimateRegressionParameters()));

        return regression.estimateRegressionParameters();
    }
}
