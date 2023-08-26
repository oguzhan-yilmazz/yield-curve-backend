package com.project.yieldcurve;

import java.text.NumberFormat;
import com.project.yieldcurve.BondCalculator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YieldCurveGraph {
    public static void plotYieldCurve(List<Double> maturities, List<Double> yields , List<LocalDate> Dates) {
        XYSeries series = new XYSeries("Yield Curve");

        XYSeries series2 = new XYSeries("Yield Curve2");
        

        for (int i = 0; i < maturities.size(); i++) {
        	for (int j =1 ; j < 8 ; j++) {        		
        		series2.add(maturities.get(i), yields.get(i));
        	}            
        }
        
        for (int i = 0; i < maturities.size(); i++) {
            series.add(maturities.get(i), yields.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Yield Curve",
                "Maturity (years)",
                "Yield (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        XYSeriesCollection dataset2 = new XYSeriesCollection(series);
        JFreeChart chart2 = ChartFactory.createXYLineChart(
                "Yield Curve",
                "Maturity (years)",
                "Yield (%)",
                dataset2,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        

        
        ChartPanel chartPanel = new ChartPanel(chart); // ChartPanel değişkenini oluştur
        chartPanel.setPreferredSize(new Dimension(720, 720));

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

     // Şekilleri görünür yap
     renderer.setBaseShapesVisible(true);
     // 0. serinin şeklini yuvarlak yap
     renderer.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
     // Çizgi kalınlığını 3 birim olarak ayarlar
     renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                NumberFormat.getNumberInstance(),
                NumberFormat.getPercentInstance()
        ));
        
        renderer.setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset dataset, int series, int item) {
                double x = dataset.getXValue(series, item);
                double y = dataset.getYValue(series, item);
                // aşağıdaki denemedir silinebilir
                LocalDate date = Dates.get(item);
                //LocalDate date = BondCalculator.getMaturityDate(null, null)
                
                // İstenilen boyut ve format; metni değiştirerek tooltip'in görsel boyutunu etkileyebilirsiniz
                return "<html><body><h3>Maturity: " + x + "</h3><h3>Date: "+date+"</h3><h3>Yield: " + y + "</h3></body></html>";
            }
        });
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Yield Curve");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel); // chartPanel değişkenini kullan
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        

        
        
        JLabel coordinateLabel = new JLabel();
        //coordinateLabel.setSize(450, 250);
        frame.add(coordinateLabel, BorderLayout.SOUTH);

        
        
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                Point2D p = chartPanel.translateScreenToJava2D(event.getTrigger().getPoint());
                double chartX = plot.getDomainAxis().java2DToValue(p.getX(), chartPanel.getScreenDataArea(), plot.getDomainAxisEdge());

                int leftNodeIndex = 0;
                int rightNodeIndex = 0;

                // Doğru iki node'u bul
                for (int i = 1; i < maturities.size(); i++) {
                    if (chartX >= maturities.get(i - 1) && chartX <= maturities.get(i)) {
                        leftNodeIndex = i - 1;
                        rightNodeIndex = i;
                        break;
                    }
                }

                // Lineer spline interpolasyon
                double x0 = maturities.get(leftNodeIndex);
                double y0 = yields.get(leftNodeIndex);
                double x1 = maturities.get(rightNodeIndex);
                double y1 = yields.get(rightNodeIndex);
                
                System.out.println("x0 :"+x0 + "y0: "+y0 + "x1: "+x1 + "y1:"+y1);

                // Lineer interpolasyon formülü
                double interpolatedYield = y0 + ((chartX - x0) / (x1 - x0)) * (y1 - y0);
                
                //önce business date' i buluruz daha sonra tıkladığım noktanın vade tarihini buluruz.
                
                
                JPanel panel = new JPanel();
                JLabel label = new JLabel("<html><body><h3>Maturity: " + chartX + "</h3><h3>interpolatedYield : " + interpolatedYield + "</h3></body></html>");
                panel.add(label);
                JOptionPane.showMessageDialog(chartPanel, panel);
           
                //JOptionPane.showMessageDialog(frame, "Maturity: " + chartX + "\nInterpolated Yield: " + interpolatedYield + "%");
            }
            
            // kordinatları göstermek için kullanıyoruz.

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // Eğer isterseniz burada farklı bir işlem yapabilirsiniz.
                Point2D p = chartPanel.translateScreenToJava2D(event.getTrigger().getPoint());
                Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea().getBounds2D();          
                double chartX = plot.getDomainAxis().java2DToValue(p.getX(), dataArea, plot.getDomainAxisEdge());
                double chartY = plot.getRangeAxis().java2DToValue(p.getY(), chartPanel.getScreenDataArea(), plot.getRangeAxisEdge());
                // Koordinatları etikete yansıt
                coordinateLabel.setText("X: " + chartX + ", Y: " + chartY);
            }
        });
        
          
    }
}


