//Antje Cramer
//Run this

public class Main {

    public static void main(String[] args){

        double[] thresholds = {5000000.0, 1000000.0, 100000.0, 30000.0, 4000.0, 400.0, 100.0, 10.0};
        String fileIn = "Broken";
        String fileOut = "out";      //filename to export resulting image to

        boolean doCompression = false;
        boolean edgeDetect = false;      //performs edge detection if turned on
        boolean myFilter = false;
        boolean outlines = false;       //draws outlines around each node if turned on

        for (int i = 0; i < args.length; i++){

            if (args[i].compareTo("-i") == 0){
                fileIn = args[++i];
            } else if (args[i].compareTo("-o") == 0) {
                fileOut = args[++i];
            } else if (args[i].compareTo("-c") == 0) {
                doCompression = true;
            } else if (args[i].compareTo("-e") == 0) {  
                edgeDetect = true;
            } else if (args[i].compareTo("-x") == 0) {
                myFilter = true;
            } else if (args[i].compareTo("-t") == 0) {
                outlines = true;
            } 

        }

        if (fileIn.compareTo("Broken") == 0){

            System.out.println("Provide a filename in arguments following this tag: -i");
            System.exit(0);

        }

        Quadtree coolTree = new Quadtree(new Image(fileIn));


        if (doCompression){
        
            for (int i = 0; i < 8; i++){
                coolTree.setThreshold(thresholds[i]);
                coolTree.subdivide();
                Image export = new Image(coolTree.export());
                export.writeImg(fileOut + "_" + (i + 1) + ".ppm");
            }

        } else {
            coolTree.doOutlines(outlines);
            coolTree.doEdgeDetect(edgeDetect);
            coolTree.doMyFilter(myFilter);

            coolTree.subdivide();
            Image export = new Image(coolTree.export());
            export.writeImg(fileOut + ".ppm");

        }


    }
}
