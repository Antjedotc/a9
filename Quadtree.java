public class Quadtree {

    protected Node root;

    public Quadtree () {

    }

    //-----------START OF PRIVATE NODE CLASS
    private class Node {

        private Color color;
        
        protected Node(Color color){
            this.color = color;
        }

        protected Node(int r, int g, int b){
            this.color = new Color(int r, int g, int b);
        }


    }
    //-----------END OF PRIVATE NODE CLASS
}
