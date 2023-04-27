public class Quadtree {

    private Node root;
    private Image img;
    private static final Color BLACK = new Color(0, 0, 0);

    public Quadtree (Image img) {

        this.img = img;
        this.root = new Node(this.meanColor(), 0, 0, img.getX(), img.getY() );

    }

    //-----------START OF PRIVATE NODE CLASS
    private class Node {

        private Color color;

        private int width = 0;
        private int height = 0;  
        private int xCoord = 0;
        private int yCoord = 0;

        private Node NE = null;
        private Node SE = null;
        private Node SW = null;
        private Node NW = null;

        //constructor using a color and ints
        protected Node(Color color, int xCoord, int yCoord, int width, int height){

            if (width > 0 && height > 0){            
            this.color = color;    
            this.width = width;
            this.height = height;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            } 
        }

        //getters
        protected Node getNE(){ return this.NE; }
        protected Node getSE(){ return this.SE; }
        protected Node getSW(){ return this.SW; }
        protected Node getNW(){ return this.NW; }
        protected int getWidth(){ return this.width; }
        protected int getHeight(){ return this.height; }
        protected Color getColor(){ return this.color; }

        //setters
        protected void setNE(Node NE){ this.NE = NE; }
        protected void setSE(Node SE){ this.SE = SE; }
        protected void setSW(Node SW){ this.SW = SW; }
        protected void setNW(Node NW){ this.NW = NW; }
        protected void setColor(Color c){ this.color = c; }

        protected boolean isLeaf(){

            if (NE == null && SE == null && SW == null && NW == null){ return true; }
            else return false;

        }

    }
    //-----------END OF PRIVATE NODE CLASS


    //divides the node into quarters and sets those as its children
    public void subdivide(Node n){

        if (n.getWidth() > 1 && n.getHeight() > 1){

            int firstHalfWidth = n.width/2;
            int secondHalfWidth = firstHalfWidth;

            int firstHalfHeight = n.height/2;
            int secondHalfHeight = firstHalfHeight;

            if (n.width % 2 != 0){
                secondHalfWidth++;
            } 

            if (n.height % 2 != 0){
                secondHalfHeight++;
            }

            Node NW = new Node(meanColor(n.xCoord, n.yCoord, firstHalfWidth, firstHalfHeight), n.xCoord, n.yCoord, firstHalfWidth, firstHalfHeight);

            Node NE = new Node(meanColor((n.xCoord + firstHalfWidth), n.yCoord, secondHalfWidth, firstHalfHeight), (n.xCoord + firstHalfWidth), n.yCoord, secondHalfWidth, firstHalfHeight);

            Node SW = new Node(meanColor(n.xCoord, (n.yCoord + firstHalfHeight), firstHalfWidth, secondHalfHeight), n.xCoord, (n.yCoord + firstHalfHeight), firstHalfWidth, secondHalfHeight);

            Node SE = new Node(meanColor((n.xCoord + firstHalfWidth), (n.yCoord + firstHalfHeight), secondHalfWidth, secondHalfHeight), (n.xCoord + firstHalfWidth), (n.yCoord + firstHalfHeight), secondHalfWidth, secondHalfHeight);


            n.setNW(NW);
            n.setNE(NE);
            n.setSW(SW);
            n.setSE(SE);

            subdivide(NW);
            subdivide(NE);
            subdivide(SW);
            subdivide(SE);


        } 

    }

    //Loops over the area in the image given with the boundary ints, finding the average of all the colors within
    public Color meanColor(int xCoord, int yCoord, int width, int height){

        Color[][] canvas = img.getCanvas();

        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = xCoord; i < (xCoord + width); i++){
            for (int j = yCoord; j < (yCoord + height); j++){

                r = r + canvas[i][j].r();
                g = g + canvas[i][j].g();
                b = b + canvas[i][j].b();

            }
        }

        if (height != 0 && width != 0){
            return new Color((r / (width*height)), (g / (width*height)), (b / (width*height)));
        } else {
            return BLACK;
        }
    }

    //finds the average color over an entire Quadtree
    public Color meanColor(){

        Color[][] canvas = img.getCanvas();

        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < img.getX(); i++){
            for (int j = 0; j < img.getY(); j++){

                r = r + canvas[i][j].r();
                g = g + canvas[i][j].g();
                b = b + canvas[i][j].b();

            }
        }

        return new Color((r / (img.getX()*img.getY())), (g / (img.getX()*img.getY())), (b / (img.getX()*img.getY())));

    }

    public Color[][] export(){

        Color[][] image = new Color[img.getX()][img.getY()];

        exportHelper(image, root);
        
        return image;

    }

    private void exportHelper(Color[][] image, Node root){

        if (root.isLeaf()){

            for (int i = root.xCoord; i < root.xCoord+root.width; i++){
                for (int j = root.yCoord; j < root.yCoord+root.height; j++){

                    image[i][j] = root.getColor();

                }
            }

        } else {

            exportHelper(image, root.getNW());
            exportHelper(image, root.getNE());
            exportHelper(image, root.getSW());
            exportHelper(image, root.getSE());

        }


    }

    public static void main(String[] args){

        Image img = new Image(args[0]);
        Quadtree cooltree = new Quadtree(img);
        cooltree.subdivide(cooltree.root);

        Image export = new Image(cooltree.export());
        export.writeImg("output.ppm");

    }

}
