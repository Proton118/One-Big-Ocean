import org.w3c.dom.css.Rect;

import processing.core.*;

public class App extends PApplet{
    public static void main(String[] args)  {
        PApplet.main("App");
    }
    public void settings() {
        size(800, 600);
    }

    // Set the background color
    public void setup() {
        background(100, 100, 100);
    }

    public void draw() {
        rect(25, 25, 100, 100);

        stroke(255, 0, 0); // Set stroke color to red (RGB: 255, 0, 0)
        strokeWeight(5);   // Set stroke thickness to 5 pixels
        circle(400, 300, 50);

        line(300, 400, 300, 800);

        triangle(100, 100, 200, 200, 50, 100);
    }
}
