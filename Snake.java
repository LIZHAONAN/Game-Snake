import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;

public class PA05demo extends JPanel implements ActionListener {

  Snake snake = new Snake("S");
  ArrayList<Point> points = snake.getPoints();
  JTextArea score = new JTextArea("Score = 0");

  public PA05demo(){
    addKeyListener(new KAdapter());
    setFocusable(true);
    setDoubleBuffered(true);
  }

  public static void main(String[] args){
    JFrame window = new JFrame("PA05demo");
    JPanel content = new JPanel();

    PA05demo game = new PA05demo();

    content.setLayout(new BorderLayout());
		content.add(game,BorderLayout.CENTER);

    content.add(score,BorderLayout.LINE_END);

    window.pack();
    window.setContentPane(content);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocation(120,70);
    window.setSize(261,185);
    window.setVisible(true);


    Timer frameTimer = new Timer(100,game);
    frameTimer.start();
  }

  public class Point{
    int x;
    int y;

    Point(int x, int y){
      this.x = x;
      this.y = y;
    }

    public String toString(){
      return "The point is at (" + x + "," + y + ")";
    }
  }

  public class Snake{
    ArrayList<Point> points;
    String direction;
    Point apple;

    public Snake(String dir){
      this.points = getNewArray();
      this.direction = dir;
      this.apple = getApplePos(25,15);
    }

    public boolean isConnected(Point p){
      for (Point po : points){
        if (po.x == p.x && po.y == p.y) return true;
      }
      return false;
    }

    public boolean isConnected(Point p, int x, int y){
      for (Point po : points){
        if (po.x == p.x && po.y == p.y) return true;
        if (po.x > x || po.x < 0 || po.y > y || po.y < 0) return true;
      }
      return false;
    }

    public ArrayList<Point> getNewArray(){
      ArrayList<Point> result = new ArrayList<>();
      for (int i = 0; i < 5; i++) result.add(new Point(0,i));
      return result;
    }

    public String toString(){
      return "The snake has length of " + points.size();
    }

    // if need to elongate, set e to true
    public void update(boolean e){
      int length = points.size();
      switch (direction) {
        case "S": if (direction == "W") break;
                  points.add(new Point(points.get(length - 1).x, points.get(length - 1).y + 1));
                  if(!e) points.remove(0);
                  break;
        case "A": if (direction == "D") break;
                  points.add(new Point(points.get(length - 1).x - 1, points.get(length - 1).y));
                  if(!e) points.remove(0);
                  break;
        case "D": if (direction == "A") break;
                  points.add(new Point(points.get(length - 1).x + 1, points.get(length - 1).y));
                  if(!e) points.remove(0);
                  break;
        case "W": if (direction == "S") break;
                  points.add(new Point(points.get(length - 1).x, points.get(length - 1).y - 1));
                  if(!e) points.remove(0);
                  break;
      }
    }

    public Point getNextPoint(){
      Point cur = points.get(points.size() - 1);
      switch (direction){
        case "W": return new Point(cur.x, cur.y - 1);
        case "S": return new Point(cur.x, cur.y + 1);
        case "A": return new Point(cur.x - 1, cur.y);
        case "D": return new Point(cur.x + 1, cur.y);
      }
      return null;
    }

    public Point getApplePos(int x, int y){
      Point result = points.get(0);
      int num = 0;
      while(pointsContains(result) && num < 1000){
        int pos_x = (int) (x * Math.random());
        int pos_y = (int) (y * Math.random());
        result = new Point(pos_x, pos_y);
        num += 1;
      }
      return result;
    }

    public boolean pointsContains(Point p){
      for (Point po : points){
        if (po.x == p.x && po.y == p.y) return true;
      }
      return false;
    }

    public ArrayList<Point> getPoints(){
      return points;
    }
  }

  public void paintComponent(Graphics g){
    g.setColor(new Color(235, 244, 66));
    g.fillRect(0,0,getWidth(),getHeight());

    g.setColor(Color.black);
    g.fillRect(0,0,260,1);
    g.fillRect(0,0,1,160);
    g.fillRect(260,0,1,160);
    g.fillRect(0,160,260,1);

    //snake.update(false);
    for(Point p: points){
      g.setColor(Color.black);
      g.fillRect (p.x * 10, p.y * 10, 4, 4);
      g.fillRect (p.x * 10 + 5, p.y * 10, 4, 4);
      g.fillRect (p.x * 10, p.y * 10 + 5, 4, 4);
      g.fillRect (p.x * 10 + 5, p.y * 10 + 5, 4, 4);
    }

    //draw food
    Point apple = snake.apple;
    g.fillRect(apple.x * 10 + 3, apple.y * 10, 3, 3);
    g.fillRect(apple.x * 10 + 3, apple.y * 10 + 6, 3, 3);
    g.fillRect(apple.x * 10, apple.y * 10 + 3, 3, 3);
    g.fillRect(apple.x * 10 + 6, apple.y * 10 + 3, 3, 3);
  }

  public void actionPerformed(ActionEvent e){
    if ( !snake.isConnected(snake.getNextPoint(), 25, 15)){
      snake.update(false);
      Point head = snake.points.get(snake.points.size() - 1);
      if (head.x == snake.apple.x && head.y == snake.apple.y){
        System.out.println("FOOD!");
        snake.update(true);
        snake.apple = snake.getApplePos(25,15);
      }
    }
    repaint();
  }

  public class KAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            System.out.println("KEY PRESSED! " + key);

            if ((key == KeyEvent.VK_LEFT) && !snake.direction.equals("D")) {
                snake.direction = "A";
            }

            if ((key == KeyEvent.VK_RIGHT) && !snake.direction.equals("A")) {
                snake.direction = "D";
            }

            if ((key == KeyEvent.VK_UP) && !snake.direction.equals("S")) {
                snake.direction = "W";
            }

            if ((key == KeyEvent.VK_DOWN) && !snake.direction.equals("W")) {
                snake.direction = "S";
            }
        }
    }

}
