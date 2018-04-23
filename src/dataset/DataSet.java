package dataset;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javax.swing.JFrame;
import javax.swing.JPanel;

// apache poi libraries
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL; // for .xlsx files online

public class DataSet extends JPanel {
    //private static Workbook output;
    private static final long serialVersionUID = 1L;
    private static ArrayList<Point> points;

    private final static int X_COORD = 0;
    private final static int Y_COORD = 1;
    
    public DataSet(){
        //output = new XSSFWorkbook();
        points = new ArrayList<Point>();
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.gray);
        for (Point point : points) {
            g2.fillOval(point.x, point.y, 15, 15);
        }
    }
    
    private static void createFile() throws Exception{
        XSSFWorkbook output = new XSSFWorkbook();
        Sheet opt = output.createSheet("Generated Coordinates");
        
        Row head = opt.createRow(0);
        head.createCell(0).setCellValue("X");
        head.createCell(1).setCellValue("Y");
        
        for (int j = 0; j < points.size(); j++){
            Row optRow = opt.createRow(j + 1);
            Cell optCell = optRow.createCell(X_COORD);
            optCell.setCellValue(points.get(j).x);
            
            optCell = optRow.createCell(Y_COORD);
            optCell.setCellValue(points.get(j).y);
        }
        
        FileOutputStream fileOut = new FileOutputStream("output.xlsx");
        output.write(fileOut);
        output.close();
        fileOut.close();
    }
    
    public static void main(String[] args) throws Exception{
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run(){
                JFrame frame = new JFrame();
                frame.add(new DataSet());
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        if (JOptionPane.showConfirmDialog(frame, 
                            "Submit this frame for coordinate generation? ", "Confirmation", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                            try{createFile();}
                            catch(Exception e){System.out.println("Caught exception on close");}
                            System.exit(0);
                        }
                    }
                });
                frame.setSize(1000, 1000);
                frame.setVisible(true);
            }
        });
    }

}