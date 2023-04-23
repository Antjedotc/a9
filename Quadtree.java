public class Quadtree {

    protected Node root;

    public Quadtree () {

    }

    //-----------START OF PRIVATE NODE CLASS
    private class Node {

        private Color color;

        private Node NE;
        private Node SE;
        private Node SW;
        private Node NW;

        //constructor using a color
        protected Node(Color color){
            this.color = color;
        }

        //constructor using rgb values
        protected Node(int r, int g, int b){
            this.color = new Color(int r, int g, int b);
        }

        //getters
        protected Node getNE(){ return this.NE; }
        protected Node getSE(){ return this.SE; }
        protected Node getSW(){ return this.SW; }
        protected Node getNW(){ return this.NW; }

        //setters
        protected void setNE(Node NE){ this.NE = NE; }
        protected void setSE(Node SE){ this.SE = SE; }
        protected void setSW(Node SW){ this.SW = SW; }
        protected void setNE(Node NW){ this.NW = NW; }

    }
    //-----------END OF PRIVATE NODE CLASS
}
