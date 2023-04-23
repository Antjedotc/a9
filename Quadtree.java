public class Quadtree {

    protected Node root;

    public Quadtree () {

    }

    //-----------START OF PRIVATE NODE CLASS
    private class Node {

        private Color color;

        private Node NE = null;
        private Node SE = null;
        private Node SW = null;
        private Node NW = null;

        //constructor using a color
        protected Node(Color color){
            this.color = color;
        }

        //constructor using rgb values
        protected Node(int r, int g, int b){
            this.color = new Color(r, g, b);
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
        protected void setNW(Node NW){ this.NW = NW; }

        protected boolean isLeaf(){

            if (NE == null && SE == null && SW == null && NW == null){ return true; }
            else return false;

        }

    }
    //-----------END OF PRIVATE NODE CLASS
}
