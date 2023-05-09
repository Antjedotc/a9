public class Color implements Comparable<Color> {

    private int r;
    private int g;
    private int b;

    public Color(int r, int g, int b){

        this.r = r;
        this.g = g;
        this.b = b;
        
    }

    public int r(){ return this.r; }
    public int g(){ return this.g; }
    public int b(){ return this.b; }

    public String toString(){ return r + ", " + g + ", " + b; }

    @Override
    public int compareTo(Color otherColor){

        return (otherColor.r - this.r) + (otherColor.g - this.g) + (otherColor.b - this.b);

    }


    public static void main(String[] args){




    }

}
