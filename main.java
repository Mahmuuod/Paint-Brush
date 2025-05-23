import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;
import javax.imageio.ImageIO;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;

// need to handle dotted line and dotted freeHand
/*
Rectangle
Oval
Line
 */

public class main extends Applet {

    static final int RED = 1;
    static final int GREEN = 2;
    static final int BLUE = 3;
    static final int NoColor = 0;
    static final int OVAL = 1;
    static final int LINE = 2;
    static final int RECT = 3;
    static final int FREEHAND = 0;
    boolean cleard = false;
    Graphics2D g2d;
    File f;
    BufferedImage canvas, loaddedImage;
    static float[] pattern = { 4, 6 };
    static BasicStroke bs = new BasicStroke();
    int space = 3;
    boolean dotted = false;
    int startx = -1;
    int starty = -1;
    int colorFlag = -1; // white:0 red:1 green:2 blue:3 backgroundColor:0 (in case you didnt choose any
                        // color "black is the default color")
    int shapeFlag = -1; // freeHand:0 oval:1 line:2 rect:3
    ArrayList<shape> shapesArray = new ArrayList<>();
    ArrayList<shape> tempArray = new ArrayList<>();
    int shapeNo = -1;
    boolean clearOrNo = false;
    boolean drawOrFill = false;

    abstract static class shape implements Externalizable {
        private static final long serialVersionUID = 1L;
        private int colorCode;
        private int dim1;
        private int dim2;
        private int dim3;
        private int dim4;
        private boolean fillOrDraw;
        private boolean dotted;

        public void setdim1(int dim1) {
            this.dim1 = dim1;
        }

        public void setdim2(int dim2) {
            this.dim2 = dim2;
        }

        public void setdim3(int dim3) {
            this.dim3 = dim3;
        }

        public void setdim4(int dim4) {
            this.dim4 = dim4;
        }

        public boolean getdotted() {
            return dotted;
        }

        public int getdim1() {
            return dim1;
        }

        public int getdim2() {
            return dim2;
        }

        public int getdim3() {
            return dim3;
        }

        public int getdim4() {
            return dim4;
        }

        public int getcolorCode() {
            return colorCode;
        }

        public boolean getfillOrDraw() {
            return fillOrDraw;
        }

        public shape(int dim1, int dim2, int dim3, int dim4, int colorCode, boolean fillOrDraw, boolean dotted) {
            this.dim1 = dim1;
            this.dim2 = dim2;
            this.dim3 = dim3;
            this.dim4 = dim4;
            this.colorCode = colorCode;
            this.fillOrDraw = fillOrDraw;
            this.dotted = dotted;
        }

        public shape() {
            this.dim1 = 0;
            this.dim2 = 0;
            this.dim3 = 0;
            this.dim4 = 0;
            this.colorCode = 0;
            this.fillOrDraw = false;
            this.dotted = false;
        }

        public void setDotState() {
            if (dotted == true)
                bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, pattern, 0);
            else
                bs = new BasicStroke();

        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(getdim1());
            out.writeInt(getdim2());
            out.writeInt(getdim3());
            out.writeInt(getdim4());
            out.writeInt(getcolorCode());
            out.writeBoolean(getfillOrDraw());
            out.writeBoolean(getdotted());
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            dim1 = in.readInt();
            dim2 = in.readInt();
            dim3 = in.readInt();
            dim4 = in.readInt();
            colorCode = in.readInt();
            fillOrDraw = in.readBoolean();
            dotted = in.readBoolean();
        }

        public void setColorState(Graphics g) {
            switch (getcolorCode()) {
                case NoColor:
                    g.setColor(Color.WHITE);
                    break;
                case RED:

                    g.setColor(Color.RED);
                    break;
                case GREEN:

                    g.setColor(Color.GREEN);

                    break;
                case BLUE:
                    g.setColor(Color.BLUE);

                    break;
                default:
                    g.setColor(Color.BLACK);

                    break;
            }

        }

        public abstract void drawShape(Graphics g);
    }

    static class Rectangle extends shape {
        private static final long serialVersionUID = 2L;

        public Rectangle(int x, int y, int width, int height, int colorCode, boolean fillOrDraw, boolean dotted) {
            super(x, y, width, height, colorCode, fillOrDraw, dotted);
        }

        public Rectangle() {
            super();
        }

        public void drawShape(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            setDotState();
            g2d.setStroke(bs);
            setColorState(g);

            if (!getfillOrDraw()) {

                g.drawRect(getdim1(), getdim2(), getdim3(), getdim4());

            } else
                g.fillRect(getdim1(), getdim2(), getdim3(), getdim4());

        }
    }

    static class oval extends shape {
        private static final long serialVersionUID = 3L;

        public oval(int x, int y, int width, int height, int colorCode, boolean fillOrDraw, boolean dotted) {
            super(x, y, width, height, colorCode, fillOrDraw, dotted);
        }

        public oval() {
            super();
        }

        public void drawShape(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            setDotState();
            g2d.setStroke(bs);
            setColorState(g);

            if (!getfillOrDraw()) {

                g.drawOval(getdim1(), getdim2(), getdim3(), getdim4());

            }

            else
                g.fillOval(getdim1(), getdim2(), getdim3(), getdim4());
        }
    }

    static class Line extends shape {
        private static final long serialVersionUID = 4L;

        public Line(int x1, int y1, int x2, int y2, int colorCode, boolean dotted) {
            super(x1, y1, x2, y2, colorCode, false, dotted);
        }

        public Line() {
            super();
        }

        public void drawShape(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            setDotState();
            g2d.setStroke(bs);
            setColorState(g);
            g.drawLine(getdim1(), getdim2(), getdim3(), getdim4());

        }
    }

    public void init() {
        int buttonW = 90;
        int buttonH = 40;
        canvasInit();
        initJavaFX();
        Button red = new Button("Red");
        Button green = new Button("Green");
        Button blue = new Button("Blue");
        Button rect = new Button("Rect");
        Button oval = new Button("Oval");
        Button line = new Button("Line");
        Button clear = new Button("Clear");
        Button free = new Button("Free Hand");
        Button earse = new Button("Erase");
        Button save = new Button("Save");
        Button read = new Button("Open");
        Button Undo = new Button("Undo");

        Checkbox dots = new Checkbox("DOTS");
        Checkbox fill = new Checkbox("FILL");
        Undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (shapeNo >= 0) {
                    shapesArray.remove(shapeNo);
                    shapeNo--;
                    repaint();
                } else {
                    if (tempArray.size() >= 0) {
                        shapesArray = tempArray;
                        tempArray = new ArrayList<>();
                        shapeNo = shapesArray.size() - 1;
                        loaddedImage=null;
                        canvasInit();
                        repaint();
                    }
                   /*  if (cleard) {

                        shapesArray = tempArray;
                        tempArray = new ArrayList<>();
                        shapeNo = shapesArray.size() - 1;
                        cleard = false;
                        repaint();
                    }*/
                }
  

            }
        });
        read.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                LoadImage2();

            }
        });
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SaveImage2();

            }
        });
        fill.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                drawOrFill = fill.getState();
            }
        });
        free.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shapeFlag = FREEHAND;
                clearOrNo = false;
                dotted = false;
                dots.setState(false);
            }
        });
        earse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // shapeFlag=0;
                clearOrNo = true;
                shapeFlag = RECT;

            }
        });
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorFlag = -1;
                shapeFlag = -1;
                cleard = true;
                tempArray = shapesArray;
                shapesArray = new ArrayList<>();
                shapeNo = -1;
                dotted = false;
                fill.setState(false);
                drawOrFill = false;
                clearOrNo = false;
                dots.setState(false);
                loaddedImage = null;
                canvasInit();

                repaint();
            }
        });
        red.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorFlag = RED;
            }
        });
        green.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorFlag = GREEN;
            }
        });
        blue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorFlag = BLUE;
            }
        });
        dots.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                dotted = dots.getState();
                if (dots.getState()) {
                    drawOrFill = false;
                    fill.setState(false);

                }

            }
        });
        rect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shapeFlag = 3;
                clearOrNo = false;
            }
        });
        oval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shapeFlag = 1;
                clearOrNo = false;
            }
        });
        line.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shapeFlag = 2;
                clearOrNo = false;
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startx = e.getX();
                starty = e.getY();
                cleard = false;
                switch (shapeFlag) {
                    case FREEHAND:
                        shapesArray.add(new Line(startx, starty, e.getX(), e.getY(), colorFlag, dotted));
                        shapeNo++;
                        break;
                    case OVAL:
                        shapesArray.add(new oval(startx, starty, 0, 0, colorFlag, drawOrFill, dotted));
                        shapeNo++;
                        break;
                    case LINE:
                        shapesArray.add(new Line(startx, starty, e.getX(), e.getY(), colorFlag, dotted));
                        shapeNo++;
                        break;
                    case RECT:
                        shapesArray.add(new Rectangle(startx, starty, 0, 0, colorFlag, drawOrFill, dotted));
                        shapeNo++;
                        break;

                    default:
                        break;
                }

            }

            public void mouseReleased(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                switch (shapeFlag) {
                    case FREEHAND:
                        shapesArray.add(new Line(startx, starty, e.getX(), e.getY(), colorFlag, dotted));
                        shapeNo++;
                        startx = e.getX();
                        starty = e.getY();
                        break;
                    case OVAL:
                        if (startx < e.getX() && starty < e.getY())
                            shapesArray.set(shapeNo, new oval(startx, starty, e.getX() - startx, e.getY() - starty,
                                    colorFlag, drawOrFill, dotted));
                        else if (startx > e.getX() && starty < e.getY())
                            shapesArray.set(shapeNo, new oval(e.getX(), starty, -(e.getX() - startx), e.getY() - starty,
                                    colorFlag, drawOrFill, dotted));
                        else if (startx < e.getX() && starty > e.getY())
                            shapesArray.set(shapeNo, new oval(startx, e.getY(), e.getX() - startx, -e.getY() + starty,
                                    colorFlag, drawOrFill, dotted));
                        else if (startx > e.getX() && starty > e.getY())
                            shapesArray.set(shapeNo, new oval(e.getX(), e.getY(), (-e.getX() + startx),
                                    (-e.getY() + starty), colorFlag, drawOrFill, dotted));
                        break;
                    case LINE:
                        shapesArray.set(shapeNo, new Line(startx, starty, e.getX(), e.getY(), colorFlag, dotted));
                        break;
                    case RECT:
                        if (!clearOrNo) {
                            if (startx < e.getX() && starty < e.getY())
                                shapesArray.set(shapeNo, new Rectangle(startx, starty, e.getX() - startx,
                                        e.getY() - starty, colorFlag, drawOrFill, dotted));
                            else if (startx > e.getX() && starty < e.getY())
                                shapesArray.set(shapeNo, new Rectangle(e.getX(), starty, -(e.getX() - startx),
                                        e.getY() - starty, colorFlag, drawOrFill, dotted));
                            else if (startx < e.getX() && starty > e.getY())
                                shapesArray.set(shapeNo, new Rectangle(startx, e.getY(), e.getX() - startx,
                                        -e.getY() + starty, colorFlag, drawOrFill, dotted));
                            else if (startx > e.getX() && starty > e.getY())
                                shapesArray.set(shapeNo, new Rectangle(e.getX(), e.getY(), (-e.getX() + startx),
                                        (-e.getY() + starty), colorFlag, drawOrFill, dotted));

                        } else {
                            shapesArray.add(new Rectangle(e.getX(), e.getY(), 10, 10, 0, true, false));
                            shapeNo++;
                            startx = e.getX();
                            starty = e.getY();
                        }
                        break;

                    default:
                        break;
                }

                repaint();

            }
        });
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setLayout(null);
        red.setBounds(0, 0, buttonW, buttonH);
        red.setBackground(Color.RED);
        green.setBackground(Color.green);
        blue.setBackground(Color.blue);
        green.setBounds(0, buttonH, buttonW, buttonH);
        blue.setBounds(0, 2 * buttonH, buttonW, buttonH);
        fill.setBounds(buttonW + 10, 0, buttonW, buttonH / 2);
        dots.setBounds(buttonW + 10, 30, buttonW, buttonH / 2);
        rect.setBounds(getWidth() - buttonW, 0, buttonW, buttonH);
        oval.setBounds(getWidth() - buttonW, buttonH, buttonW, buttonH);
        line.setBounds(getWidth() - buttonW, 2 * buttonH, buttonW, buttonH);
        free.setBounds(getWidth() - buttonW, 3 * buttonH, buttonW, buttonH);
        earse.setBounds(getWidth() - buttonW, 4 * buttonH, buttonW, buttonH);
        save.setBounds((getWidth() / 2) - (buttonW * 2), 0, buttonW, buttonH);
        read.setBounds((getWidth() / 2) - buttonW, 0, buttonW, buttonH);
        Undo.setBounds((getWidth() / 2) + 10, 0, buttonW, buttonH);
        clear.setBounds((getWidth() / 2) + buttonW + 10, 0, buttonW, buttonH);
        add(red);
        add(green);
        add(blue);
        add(dots);
        add(fill);
        add(rect);
        add(oval);
        add(line);

        add(clear);
        add(free);
        add(earse);
        add(save);
        add(read);
        add(Undo);
    }

    public void paint(Graphics g) {
        shape s;

        g.drawImage(canvas, 0, 0, this);

        if (loaddedImage != null) {
            g.drawImage(loaddedImage, 0, 0, this);

        }

        for (int i = 0; i <= shapesArray.size() - 1; i++) {
            s = shapesArray.get(i);
            s.drawShape(g);
        }

    }

    private Image offscreenBuffer;
    private Graphics offscreenGraphics;

    public void update(Graphics g) {
        if (offscreenBuffer == null ||
                offscreenBuffer.getWidth(this) != getWidth() ||
                offscreenBuffer.getHeight(this) != getHeight()) {
            offscreenBuffer = createImage(getWidth(), getHeight());
            offscreenGraphics = offscreenBuffer.getGraphics();
        }

        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        paint(offscreenGraphics);
        g.drawImage(offscreenBuffer, 0, 0, this);
    }

    private void SaveImage2() {

        Platform.runLater(new Runnable() {
            public void run() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open File");

                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    try {

                        paint(g2d);
                        ImageIO.write(canvas, "png", file);
                        // repaint();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private void LoadImage2() {

        Platform.runLater(new Runnable() {
            public void run() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open File");

                File file = fileChooser.showOpenDialog(null);

                if (file != null) {
                    try {
                        tempArray=shapesArray;
                        shapesArray=new ArrayList<>();
                        shapeNo = -1;
                        loaddedImage = ImageIO.read(file);
                        repaint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private JFXPanel fxPanel;

    private void initJavaFX() {
        // Create a JFXPanel to initialize JavaFX
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
    }

    public void canvasInit() {
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE); // Set background color
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}

/* <applet code="main.class" width="1000" height="800"></applet> */

/*
 * else
 * {
 * for(int i=getdim1();i<=getdim1()+getdim3();i+=space)
 * {
 * g.drawLine(i,getdim2(),i,getdim2());
 * 
 * }
 * for(int i=getdim2();i<=getdim2()+getdim4();i+=space)
 * {
 * g.drawLine(getdim1(),i,getdim1(),i);
 * 
 * }
 * for(int i=getdim1();i<=getdim1()+getdim3();i+=space)
 * {
 * g.drawLine(i,getdim2()+getdim4(),i,getdim2()+getdim4());
 * 
 * }
 * for(int i=getdim2();i<=getdim2()+getdim4();i+=space)
 * {
 * g.drawLine(getdim1()+getdim3(),i,getdim1()+getdim3(),i);
 * 
 * }
 * }
 */
/*
 * else
 * {
 * int center_x=getdim1()+(getdim3()/2);
 * int center_y=getdim2()+(getdim4()/2);
 * for(int i=0;i<=360;i+=space)
 * {
 * double angel=Math.toRadians(i);
 * int x=center_x+(int)( (getdim3()/2)*Math.cos(angel));
 * int y=center_y+(int)((getdim4()/2)*Math.sin(angel));
 * g.drawLine(x, y, x, y);
 * }
 * }
 */

/*
 * if(!getdotted())
 * {
 * 
 * }
 * 
 * else
 * {
 * double lineLength=Math.round(Math.sqrt(( (getdim3()-getdim1()) *
 * (getdim3()-getdim1()) )+( (getdim4()-getdim2()) * (getdim4()-getdim2()) )));
 * int no_of_dots = (int)lineLength/space;
 * double xInc=0;
 * double yInc=0;
 * if(no_of_dots>0)
 * {
 * xInc=(getdim3()-getdim1())/no_of_dots;
 * yInc=(getdim4()-getdim2())/no_of_dots;
 * }
 * for(int i=0;i<=no_of_dots;i++)
 * {
 * int x=(int)(Math.round( getdim1() + (i * xInc) ) );
 * int y=(int)( Math.round( getdim2() + (i * yInc) ));
 * g.drawLine(x, y, x, y);
 * 
 * }
 * 
 * 
 * 
 * 
 * 
 * }
 */

/*
 * private void LoadImage() {
 * 
 * Platform.runLater(new Runnable() {
 * public void run() {
 * FileChooser fileChooser = new FileChooser();
 * fileChooser.setTitle("Open File");
 * 
 * File file = fileChooser.showOpenDialog(null);
 * 
 * if (file != null) {
 * try {
 * FileInputStream fis = new FileInputStream(file);
 * ObjectInputStream ois = new ObjectInputStream(fis);
 * shapesArray = (ArrayList<shape>) ois.readObject();
 * shapeNo = shapesArray.size() - 1;
 * repaint();
 * ois.close();
 * fis.close();
 * } catch (IOException e) {
 * e.printStackTrace();
 * } catch (ClassNotFoundException cn) {
 * cn.printStackTrace();
 * }
 * 
 * }
 * 
 * }
 * });
 * 
 * }
 */