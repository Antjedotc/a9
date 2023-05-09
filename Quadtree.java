// Antje Cramer - cs151

public class Quadtree {

    private Node root;
    private Image img; 

    //calculation variables
    private int numLeaves = 0; 

    //constants chosen by me
    private static final Color BORDERS = new Color(150, 150, 150);      //color used to border nodes when outlines is turned on
    private static final Color EDGEFILL = new Color(0, 0, 0);           //background color used in edge detection
    private static final Color EDGELINES = new Color(255, 255, 255);        //outline color used in edge detection
    private static final int EDGEMAXDETAIL = 6;     //max number of pixels required in a node to perform edge detection
    private static final int GRAYCUTOFF = 60;      //Brightness required for edge detection to return EDGEFILL or EDGELINES

    //vars picked at load in
    private double detailThreshold = 1000.0;    //higher number = more heavily compressed (fewer nodes, faster)
    private String fileOut = "output.ppm";      //filename to export resulting image to
    private boolean outlines = false;       //draws outlines around each node if turned on
    private boolean edgeDetect = false;      //performs edge detection if turned on
    private boolean myFilter = false;

    public Quadtree (Image img) {

        this.img = img;
        this.root = new Node(this.meanColor(), 0, 0, img.getX(), img.getY() );

    }

    public void setThreshold(double detailThreshold){ this.detailThreshold = detailThreshold; }
    public void setFileOut(String fileOut) { this.fileOut = fileOut; }
    public void doOutlines(boolean value){ this.outlines = value; }
    public void doEdgeDetect(boolean value){ this.edgeDetect = value; }
    public void doMyFilter(boolean value){ this.myFilter = value; }

    public double compressionLV(){
        double compressionLV = this.numLeaves / ( ((double)img.getX())*((double)img.getY()) );
        return compressionLV;
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


    //divides the node into quarters and sets those as its children.
    /**method subdivide: divides the image into a compressed quadtree
     * @param n: the node to start from
     * @param complv: compression level, number of leaves divided by pixel count
     */
    public void subdivide(){
        subdivide(root);
    }

    public void subdivide(Node n){

        if (meanSquaredError(n) < detailThreshold){
            numLeaves++;
            return;
        }

        if (n.getWidth() > 0 && n.getHeight() > 0 ){

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

            if (n.getWidth() == 1 && n.getHeight() > 1){

                Node NW = new Node(meanColor(n.xCoord, n.yCoord, 1, firstHalfHeight), n.xCoord, n.yCoord, 1, firstHalfHeight);
                Node SE = new Node(meanColor(n.xCoord, (n.yCoord + firstHalfHeight), 1, secondHalfHeight), n.xCoord, (n.yCoord + firstHalfHeight), 1, secondHalfHeight);
                
                n.setNW(NW);
                n.setSE(SE);

                subdivide(NW);
                subdivide(SE);

            } else if (n.getHeight() == 1 && n.getWidth() > 1) {

                Node NW = new Node(meanColor(n.xCoord, n.yCoord, firstHalfWidth, 1), n.xCoord, n.yCoord, firstHalfWidth, 1);
                Node SE = new Node(meanColor((n.xCoord + firstHalfWidth), n.yCoord, secondHalfWidth, 1), (n.xCoord + firstHalfWidth), n.yCoord, secondHalfWidth, 1);
                
                n.setNW(NW);
                n.setSE(SE);

                subdivide(NW);
                subdivide(SE);

            } else if (!(n.getHeight() == 1 && n.getWidth() == 1)){

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

            } else {

                numLeaves++;

            }

        }

    }

    //Loops over the area in the image given with the boundary ints, finding the average of all the colors within
    private Color meanColor(int xCoord, int yCoord, int width, int height){

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
            return BORDERS;
        }
    }

    //finds the average color over an entire Quadtree
    private Color meanColor(){

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

    //finds the mean squared error
    private double meanSquaredError(Node n) {

        double error = 0.0;
        //avg color:
        Color C = n.getColor();
        //og colors:
        Color[][] canvas = img.getCanvas();


        for (int i = n.xCoord; i < n.xCoord+n.width; i++){
            for (int j = n.yCoord; j < n.yCoord+n.height; j++){

                error += ((canvas[i][j].r() - C.r()) * (canvas[i][j].r() - C.r())) + ((canvas[i][j].g() - C.g()) * (canvas[i][j].g() - C.g())) + ((canvas[i][j].b() - C.b()) * (canvas[i][j].b() - C.b()));
                //runs through each pixel of the image

            }
        }

        double avgError = Math.abs(error );

        return avgError;
    }

    //calculates an int that represents the average contrast in a 3x3 area of pixels
    private int edgeDetect(int x, int y){

        //we are grabbing pixels from the ORIGINAL IMAGE!!! this.img.getCanvas[][]
        Color[][] canv =img.getCanvas();
        Color core = canv[x][y];

        int r = -canv[x - 1][y - 1].r() - canv[x][y - 1].r() - canv[x + 1][y - 1].r() 
                - canv[x - 1][y].r() + (8*canv[x][y].r()) - canv[x + 1][y].r()
                - canv[x - 1][y + 1].r() - canv[x][y + 1].r() - canv[x + 1][y + 1].r();

        int g = -canv[x - 1][y - 1].g() - canv[x][y - 1].g() - canv[x + 1][y - 1].g() 
                - canv[x - 1][y].g() + (8*canv[x][y].g()) - canv[x + 1][y].g()
                - canv[x - 1][y + 1].g() - canv[x][y + 1].g() - canv[x + 1][y + 1].g();

        int b = -canv[x - 1][y - 1].b() - canv[x][y - 1].b() - canv[x + 1][y - 1].b() 
                - canv[x - 1][y].b() + (8*canv[x][y].b()) - canv[x + 1][y].b()
                - canv[x - 1][y + 1].b() - canv[x][y + 1].b() - canv[x + 1][y + 1].b();

        int avg = (int)((double)(r+g+b)/3.0);

        return avg;
    }

    //secret
    private Color myFilter(int x, int y){

        //we are grabbing pixels from the ORIGINAL IMAGE!!! this.img.getCanvas[][]
        //if there's a lot of difference, it should be REALLY blue
        Color[][] canv =img.getCanvas();
        Color core = canv[x][y];

        double r1 = 0.1/0.9;
        double r2 = -1;

        double rem = -2;


        double r = (r1)*(canv[x - 1][y - 1].r()) + (r1)*(canv[x][y - 1].r()) + (r1)*(canv[x + 1][y - 1].r()) 
                + (r1)*(canv[x - 1][y].r()) + (0.1/0.9)*(canv[x][y].r()) + (r1)*(canv[x + 1][y].r())
                + (r1)*(canv[x - 1][y + 1].r()) + (r1)*(canv[x][y + 1].r()) + (r1)*(canv[x + 1][y + 1].r());

        double g = (r2)*(canv[x - 1][y - 1].g()) + (r2)*(canv[x][y - 1].g()) + (r2)*(canv[x + 1][y - 1].g()) 
                + (r2)*(canv[x - 1][y].g()) + (8)*(canv[x][y].g()) + (r2)*(canv[x + 1][y].g())
                + (r2)*(canv[x - 1][y + 1].g()) + (r2)*(canv[x][y + 1].g()) + (r2)*(canv[x + 1][y + 1].g());

        double b = (rem)*(canv[x - 1][y - 1].b()) + (rem)*(canv[x][y - 1].b()) + (rem)*(canv[x + 1][y - 1].b()) 
                + (rem)*(canv[x - 1][y].b()) + (16)*(canv[x][y].b()) + (rem)*(canv[x + 1][y].b())
                + (rem)*(canv[x - 1][y + 1].b()) + (rem)*(canv[x][y + 1].b()) + (rem)*(canv[x + 1][y + 1].b());

        /*if (Math.random() > .5){
            r = r*r2;
            g = g*rem;
            b = b*r1;
        }*/
        r = r/3;

        if ( b < 40){

            b+= 10;
        }
            
        return new Color((int)r, (int)g, (int)b);
    }

    /* method export: converts a quadtree into an array of Colors.
     * load this function into the Image constructor to make an image,
     * and then use writeImg() to save it as a ppm.
     * 
     */
    public Color[][] export(){

        Color[][] image = new Color[img.getX()][img.getY()];

        exportHelper(image, root);
        
        return image;

    }

    private void exportHelper(Color[][] image, Node root){

        if (root != null){

            if (root.isLeaf()){

                for (int i = root.xCoord; i < root.xCoord+root.width; i++){
                    for (int j = root.yCoord; j < root.yCoord+root.height; j++){

                        if (outlines && ( i == root.xCoord+root.width-1 || j == root.yCoord+root.height-1) && (root.width > 1 && root.height > 1)){
                            image[i][j] = BORDERS;
                        } else{

                            //-------------------- EDGE DETECTION
                            if (edgeDetect){
                                if (root.getWidth()*root.getHeight() < EDGEMAXDETAIL && i != 0 && j != 0 && i != img.getX() - 1 && j != img.getY() - 1){

                                    int avg = edgeDetect(i, j);

                                    if (avg > GRAYCUTOFF){
                                        image[i][j] = EDGELINES;
                                    } else {
                                        image[i][j] = EDGEFILL;    
                                    }


                                } else {
                                    image[i][j] = EDGEFILL;

                                }

                            //-------------------- MY FILTER 
                            } else if (myFilter) {

                                if(true && i != 0 && j != 0 && i != img.getX() - 1 && j != img.getY() - 1 ){
                                    image[i][j] = myFilter(i, j);
                                } else {
                                    image[i][j] = new Color (0, 0, 0);
                                }

                            } else {
                                image[i][j] = root.getColor();
                            }
                        }
                    }
                }

            } else {

                exportHelper(image, root.getNW());
                exportHelper(image, root.getNE());
                exportHelper(image, root.getSW());
                exportHelper(image, root.getSE());

            }

        }
    }

    public static void main(String[] args){

        Image img = new Image(args[0]);
        Quadtree cooltree = new Quadtree(img);
        cooltree.subdivide();

        System.out.printf("Compression level: %.3f \n", cooltree.compressionLV());

        Image export = new Image(cooltree.export());
        export.writeImg("output.ppm");

    }

}

/**
 * TO DO
 * 
 * Edge detection (n maxsize)
 * - if bigger than n: set to black
 * - if smaller than n: run edge detection
 * 
 * Args loadin: command parser (use code from the names assignment)
 * 
 * Design filter
 * 
 * 
 */
