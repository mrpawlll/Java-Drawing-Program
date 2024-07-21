package ooad.group23;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawingProgram extends JFrame{
    private DrawingPanel drawingPanel; // Use custom JPanel subclass
    private ToolPanel toolbarPanel;
    private DrawingProgram() {
        super("Painter");
        setLayout(new BorderLayout());
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        drawingPanel = new DrawingPanel(); // Use custom JPanel subclass
        drawingPanel.setBackground(Color.white);
        drawingPanel.setOpaque(true);

        toolbarPanel = new ToolPanel();
        toolbarPanel.setPreferredSize(new Dimension(700,50));
        toolbarPanel.setOpaque(true);

        getContentPane().setBackground(Color.BLACK);

        this.add(drawingPanel, BorderLayout.CENTER);
        this.add(toolbarPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    
    public static void main(String[] a) {
        new DrawingProgram();
    }
}

class RadioWithIcon extends JPanel{
    private JRadioButton radio;
    private JLabel icon;

    public RadioWithIcon(ImageIcon image){
        super();
        GridLayout layout = new GridLayout(1,3,0,0);
        this.setLayout(layout);
        setPreferredSize(new Dimension(70, 30));

        if(image!=null){
        Image scaledIcon = image.getImage().getScaledInstance(30, 30,java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon(scaledIcon);
        this.radio = new JRadioButton();
        this.icon = new JLabel(image);
        add(this.radio);
        add(this.icon);
        }else{
            System.out.print("No image");
        }
        
        
    }

    public JRadioButton getButton(){
        return this.radio;
    }

    public void addActionListener(ActionListener listener){
        this.radio.addActionListener(listener);
    }
}

class ToolPanel extends JPanel implements ChangeListener, ActionListener{
    private JColorChooser jcc;
    private JButton colorPalette;
    private static Color selectedColor;

    private JSlider slider;
    private static int brushSize;

    private ButtonGroup groupSelection;
    // private JRadioButton square, line, freehand,oval;
    private ImageIcon squareIcon, lineIcon, freehandIcon,ovalIcon,triangleIcon,pentagonIcon;
    private RadioWithIcon square, line, freehand,oval,triangle,pentagon;
    private static int shapeSelection;



public ToolPanel(){
        super();
        brushSize = 10;
        shapeSelection = 1;
        selectedColor = Color.black;

        this.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));

        jcc = new JColorChooser();
        colorPalette = new JButton("Choose color");

        slider = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);


        squareIcon = new ImageIcon("rss/square.png");
        lineIcon = new ImageIcon("rss/line.png");
        freehandIcon = new ImageIcon("rss/freehand.png");
        ovalIcon = new ImageIcon("rss/circle.png");
        triangleIcon = new ImageIcon("rss/triangle.png");
        pentagonIcon = new ImageIcon("rss/pentagon.png");

        square   = new RadioWithIcon(squareIcon);
        line     = new RadioWithIcon(lineIcon);
        freehand = new RadioWithIcon(freehandIcon);
        oval     = new RadioWithIcon(ovalIcon);
        triangle = new RadioWithIcon(triangleIcon);
        pentagon = new RadioWithIcon(pentagonIcon);


        groupSelection = new ButtonGroup();
        groupSelection.add(line.getButton());
        groupSelection.add(square.getButton());
        groupSelection.add(freehand.getButton());
        groupSelection.add(oval.getButton());
        groupSelection.add(triangle.getButton());
        groupSelection.add(pentagon.getButton());
        line.getButton().setSelected(true);

        colorPalette.addActionListener(this);

        slider.addChangeListener(this);

        //listeners for button group
        square.addActionListener(this);
        line.addActionListener(this);
        freehand.addActionListener(this);
        oval.addActionListener(this);
        triangle.addActionListener(this);
        pentagon.addActionListener(this);

        add(colorPalette);
        add(slider);

        add(line);
        add(square);
        add(freehand);
        add(oval);
        add(triangle);
        add(pentagon);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == colorPalette) {
            JOptionPane.showMessageDialog(null, jcc);
            selectedColor = jcc.getColor();
        }

        if (line.getButton().isSelected()) {
            shapeSelection = 1;
        }

        if (square.getButton().isSelected()) {
            shapeSelection = 2;
        }

        if (freehand.getButton().isSelected()){
            shapeSelection = 3;
        }

        if(oval.getButton().isSelected()){
            shapeSelection = 4;
        }

        if(triangle.getButton().isSelected()){
            shapeSelection = 5;
        }

        if(pentagon.getButton().isSelected()){
            shapeSelection = 6;
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            brushSize = (int) source.getValue();
        }
    }

    public static int getBrushSize(){
        return brushSize;
    }

    public static int getShapeSelection(){
        return shapeSelection;
    }

    public static Color getColor(){
        return selectedColor;
    }

}

// Custom JPanel subclass for drawing shapes
class DrawingPanel extends JPanel implements MouseListener,MouseMotionListener{

    private ArrayList<Shape> shapes;     
    private Point pointFirstClick;
    private boolean isFirstClick;
    private Shape shape;
    private static final int WIDTH = 1000, HEIGHT = 500, LINE = 1,SQUARE = 2, FREEHAND = 3,OVAL = 4, TRIANGLE = 5, PENTAGON = 6;


    public DrawingPanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(getMinimumSize());
        setMaximumSize(getMaximumSize());
        shapes = new ArrayList<>();
        isFirstClick = true;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public Dimension getMinimumSize(){
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isFirstClick) {
            pointFirstClick = e.getPoint();
            isFirstClick = false;
            createShape(); // Store the drawn shape
            repaint();
        } else {
            isFirstClick = true;
            repaint(); // Repaint the custom JPanel
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isFirstClick) {
            updateShape(e.getPoint());
            repaint(); // Repaint the custom JPanel
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    private void createShape(){
        switch (ToolPanel.getShapeSelection()) {
            case LINE:
                shape = new LineShape(pointFirstClick.x, pointFirstClick.y,pointFirstClick.x,pointFirstClick.y,ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;

            case SQUARE:
                shape = new RectangleShape(pointFirstClick.x, pointFirstClick.y, 0, 0, ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;
            case FREEHAND:
                shape = new FreeHandDrawShape(pointFirstClick.x, pointFirstClick.y, pointFirstClick.x, pointFirstClick.y, ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;
            case OVAL:
                shape = new OvalShape(pointFirstClick.x, pointFirstClick.y, 0, 0, ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;
            case TRIANGLE:
                shape = new TriangleShape(pointFirstClick.x, pointFirstClick.y, 0,pointFirstClick.x,ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;

            case PENTAGON:
                shape = new PentagonShape(pointFirstClick.x, pointFirstClick.y, 0,pointFirstClick.x,ToolPanel.getColor(), ToolPanel.getBrushSize());
                break;
            default:
                return;
        }
        shapes.add(shape);
        this.setShapes(shapes);
    }

    private void updateShape(Point point){
        shape.setCoords(pointFirstClick,point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Draw the stored shapes
        for (Shape shape : shapes) {
            g2.setColor(shape.getColor());
            g2.setStroke(new BasicStroke(shape.getStroke()));

            switch(shape.getType()){
                case LINE:
                g2.drawLine(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2());
                break;

                case SQUARE:
                g2.drawRect(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2());
                break;
                
                case FREEHAND:
                if(shape instanceof FreeHandDrawShape && ((FreeHandDrawShape)shape).getPointSize()>0 ){
                    for (int i = 1; i < ((FreeHandDrawShape)shape).getPointSize(); i++) {
                        Point currentPoint = ((FreeHandDrawShape)shape).getPointAt(i);
                        g2.drawLine(currentPoint.x, currentPoint.y, currentPoint.x, currentPoint.y);
                    }
                }
                break;

                case OVAL:
                g2.drawOval(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2());
                break;

                case TRIANGLE:
                Polygon triangle = new Polygon(((TriangleShape)shape).getPointX(), ((TriangleShape)shape).getPointY(), 3);
                g2.drawPolygon(triangle);
                break;

                case PENTAGON:
                Polygon pentagon = new Polygon(((PentagonShape)shape).getPointX(),((PentagonShape)shape).getPointY(),5);
                g.drawPolygon(pentagon);
                break;

                default:
                return;

            }


        }
    }

}

abstract class Shape {
    private final int type;
    protected int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    private final Color color;
    private final int brushSize;

    public Shape(int type, int x1, int y1, int x2, int y2, Color color, int brush) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.brushSize = brush;
    }

    public int getType() {
        return type;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Color getColor() {
        return color;
    }

    public int getStroke() {
        return brushSize;
    }

    protected abstract void setCoords(Point a,Point b);

}

class LineShape extends Shape {
    public LineShape(int x1, int y1, int x2, int y2, Color color, int brush) {
        super(1, x1, y1, x2, y2, color, brush);
    }

    public void setCoords(Point firstPoint,Point secondPoint){
        this.x1 = firstPoint.x;
        this.y1 = firstPoint.y;
        this.x2 = secondPoint.x;
        this.y2 = secondPoint.y;
    }
}

class RectangleShape extends Shape {
    public RectangleShape(int x, int y, int width, int height, Color color, int brush) {
        super(2, x, y, width, height, color, brush);
    }

    public void setCoords(Point firstPoint, Point secondPoint){
        this.x1 = Math.min(firstPoint.x,  secondPoint.x);
        this.y1 = Math.min(firstPoint.y,  secondPoint.y); 
        this.x2 = Math.abs(firstPoint.x - secondPoint.x);
        this.y2 = Math.abs(firstPoint.y - secondPoint.y);
    }
}

class FreeHandDrawShape extends Shape {
    private ArrayList<Point> points;
    public FreeHandDrawShape(int x, int y, int width, int height, Color color, int brush){
        super(3, x, y, width, height, color, brush);
        points = new ArrayList<>();
    }

    public void setCoords(Point firstPoint, Point secondPoint){
        points.add(secondPoint);
        this.x1 = secondPoint.x;
        this.y1 = secondPoint.y;
        this.x2 = secondPoint.x;
        this.y2 = secondPoint.y;
    }

    public ArrayList<Point> getPoints(){
        return this.points;
    }

    public Point getPointAt(int a){
        return this.points.get(a);
    }

    public int getPointSize(){
        return this.points.size();
    }

}

class OvalShape extends Shape {
    public OvalShape(int x, int y, int width, int height, Color color, int brush){
        super(4, x, y, width, height, color, brush);
    }

    public void setCoords(Point firstPoint, Point secondPoint) {
        this.x1 = Math.min(firstPoint.x,  secondPoint.x);
        this.y1 = Math.min(firstPoint.y,  secondPoint.y); 
        this.x2 = Math.abs(firstPoint.x - secondPoint.x);
        this.y2 = Math.abs(firstPoint.y - secondPoint.y);
    }
}

class TriangleShape extends Shape {
    private int[] pointX = new int[3];
    private int[] pointY = new int[3];

    public TriangleShape(int x, int y, int width, int height, Color color, int brush) {
        super(5, x, y, width, height, color, brush);
    }

    public void setCoords(Point firstPoint, Point secondPoint) {
        this.x1 = Math.min(firstPoint.x, secondPoint.x); //which pointer is currently expanding the shape
        this.y1 = firstPoint.y; //first point's y
        this.x2 = Math.abs(firstPoint.x - secondPoint.x); //length
        int triangleHeight = (int) (this.x2 * Math.sqrt(3) / 2); //calculate the height

        //have to check if the mouse pointer> first point's y to know whether to add to the first point's y or deduct
        if(secondPoint.y>firstPoint.y){
            this.y2 = this.y1 + triangleHeight;
        }else{
            this.y2 = this.y1 - triangleHeight;
        }
        this.pointX[0] = this.x1;
        this.pointX[1] = this.x1 + this.x2 / 2;
        this.pointX[2] = this.x1 + this.x2;
        this.pointY[0] = this.y1;
        this.pointY[1] = this.y2;
        this.pointY[2] = this.y1;
        
    }

    int[] getPointX(){
        return this.pointX;
    }

    int[] getPointY(){
        return this.pointY;
    }
}

class PentagonShape extends Shape{
    private int[] xPoints = new int[5];
    private int[] yPoints = new int[5];
    private double angle = 2 * Math.PI / 5;


    public PentagonShape(int x, int y, int width, int height, Color color, int brush) {
    super(6, x, y, width, height, color, brush);
    }

    public void setCoords(Point firstPoint,Point secondPoint){
        this.x1 = firstPoint.x;
        this.y1 = firstPoint.y;
        this.x2 = Math.abs(firstPoint.x - secondPoint.x);

        int radius = getRadius(firstPoint, secondPoint);
        for (int i = 0; i < 5; i++) {
            xPoints[i] = (int) (this.x1 + radius * Math.cos(i * angle - Math.PI / 2));
            yPoints[i] = (int) (this.y1 + radius * Math.sin(i * angle - Math.PI / 2));
        }

    }

    public int getRadius(Point firstPoint, Point secondPoint){
            int dx = Math.abs(firstPoint.x - secondPoint.x);
            int dy = Math.abs(secondPoint.y - firstPoint.y);
            return Math.min(dx, dy);
    }

    int[] getPointX(){
        return this.xPoints;
    }

    int[] getPointY(){
        return this.yPoints;
    }

}