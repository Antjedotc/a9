import java.io.*;
import java.util.*;
import java.lang.*;

public class Image {

    private int x;
    private int y;
    private Color[][] canvas;

    public Image(String filename){
        this.open(filename);
    }

    public Image(Color[][] canvas){
        this.x = canvas.length;
        this.y = canvas[0].length;
        this.canvas = canvas;

    }

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public Color[][] getCanvas(){ return this.canvas; }
    public void setCanvas(Color[][] canvas){ this.canvas = canvas; }

    public void open(String filename){

        File coolFile = new File(filename);

        try {

            System.out.println("Opening " + filename + "...");

            Scanner reader = new Scanner(coolFile);

            int j = 0;  //counts through the header data. 0 is version, 1 is x, 2 is y, 3 is color max
 
            while(reader.hasNext()){

                String next = reader.next();

                if (next.contains("#")){
                    reader.nextLine();
                } else if (j == 0){
                                        //System.out.println(j + ": " + next);
                    j++;
                } else if (j == 1){
                                        //System.out.println(j + ": " + next);
                    this.x = Integer.parseInt(next);
                    j++;
                } else if (j == 2){
                                        //System.out.println(j + ": " + next);
                    this.y = Integer.parseInt(next);
                    j++;
                } else if (j == 3){
                                        //System.out.println(j + ": " + next);
                    j++;
                    break;
                } 
            }

            //System.out.println(this.x + " x " + this.y);

            this.canvas = new Color[this.x][this.y];

            for (int i = 0; i < this.y; i++) {
                for (int q = 0;  q < this.x; q++) {
                    this.canvas[q][i] = new Color(reader.nextInt(), reader.nextInt(), reader.nextInt());
                }
            }

        } catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
            System.exit(0);
        }

    }
 
    public void writeImg(String filename){

        File coolFile = new File(filename);

        try {

            System.out.println("Saving " + filename + "...");

            PrintWriter out = new PrintWriter(coolFile);

            out.print("P3 ");
            out.println(canvas.length + " " + canvas[0].length + " 255");

            for (int i = 0; i < canvas[0].length; i++){
                for (int j = 0; j < canvas.length; j++){

                    out.print(canvas[j][i].r() + " " +
                              canvas[j][i].g() + " " + 
                              canvas[j][i].b() + " ");


                }
            }

            out.println();
            out.close();


        } catch (IOException e) {
            System.out.print("you broke it");
        }

    }

    public static Image grayscale(Image input){

        Color[][] newCanvas = new Color[input.x][input.y];

        for (int i = 0; i < input.canvas[0].length; i++){
            for (int j = 0; j < input.canvas.length; j++){

                int c = (int)((input.canvas[j][i].r() * .3) + (input.canvas[j][i].g() * .59) + (input.canvas[j][i].b() * .11));

                newCanvas[j][i] = new Color(c, c, c);

            }
        }        

        return new Image(newCanvas);

    }

    public static Image tint(Image input, Color tintColor){

        int r = tintColor.r();
        int g = tintColor.g();
        int b = tintColor.b();

        Color[][] newCanvas = new Color[input.x][input.y];

        for (int i = 0; i < input.canvas[0].length; i++){
            for (int j = 0; j < input.canvas.length; j++){

                int R = input.canvas[j][i].r(); 
                int G = input.canvas[j][i].g();
                int B = input.canvas[j][i].b();

                R = (int)(((double)R/255) * r);
                G = (int)(((double)G/255) * g);
                B = (int)(((double)B/255) * b);

                newCanvas[j][i] = new Color(R, G, B);

            }
        }        

        return new Image(newCanvas);
    }

    public static Image negative(Image input) {
        Color[][] newCanvas = new Color[input.x][input.y];
        
        for (int i = 0; i < input.canvas[0].length; i++) {
            for (int j = 0; j < input.canvas.length;j++) {
                int oldR = input.getCanvas()[j][i].r();
                int oldG = input.getCanvas()[j][i].g();
                int oldB = input.getCanvas()[j][i].b();
                
                int R = input.canvas[j][i].r();
                int G = input.canvas[j][i].g();
                int B = input.canvas[j][i].b();
                
                R = 255 - oldR;
                G = 255 - oldG;
                B = 255 - oldB;
                
                newCanvas[j][i] = new Color(R, G, B);
            }
        }
        return new Image(newCanvas);
    }


    public static void main(String[] args){

        Image img = new Image(args[0]);

        Image imgChanged = negative(img);

        //, new Color(157, 130, 180)

        imgChanged.writeImg("output.ppm");

    }

}
