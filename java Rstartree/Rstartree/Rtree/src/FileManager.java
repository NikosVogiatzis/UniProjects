import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

/**
 * Κλάση διαχείρισης των αρχείων. Δημιουργία datafile, indexfile, διάβασμα από αυτά καθώς και προσθήκη
 * δεδομένων (τοποθεσιών) στο indexfile
 */
public class FileManager {
    public int blockID;

    public FileManager()  {
        blockID = 0;
    }

    /**
     * Συνάρτηση που φορτώνει δεδομένα από ένα OSM αρχείο και τα αποθηκεύει στο αρχείο datafile ως μια σειρά από bytes
     */
    public void create_datafile() throws IOException, SAXException, ParserConfigurationException {
        Tree tree = new Tree(new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE));

        String data_file = "datafile.txt";
        //Διαχειριση του map.osm
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("map.osm"));
        document.getDocumentElement().normalize(); //για καλύτερη διαχείριση του xml
        NodeList nodeList = document.getElementsByTagName("node");
        nodeList.getLength();
        int size = nodeList.getLength();
        long[] id = new long[size];
        double[] lat = new double[size], lon = new double[size];
        for (int x = 0; x < size; x++) {
            id[x] = Long.parseLong((nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue()));
            lat[x] = Double.parseDouble((nodeList.item(x).getAttributes().getNamedItem("lat").getNodeValue()));
            lon[x] = Double.parseDouble((nodeList.item(x).getAttributes().getNamedItem("lon").getNodeValue()));

        }


        try {
            FileOutputStream fos = new FileOutputStream(data_file);
            DataOutputStream dos = new DataOutputStream(fos);
            int blocks_Number = 1;
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar('0');

            dos.writeInt(size/1365+1);
            dos.writeInt(size);
            dos.writeInt(1365);
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar((char) blocks_Number);
            int count = 0;
            for (int x = 0; x < size; x++) {
                if (count == 1365) {
                    // The number of entries in each block is 1365 so the block size is 32 KB. When we change blocks  we print the id of the new block in the beginning
                    count = 0;
                    blocks_Number++;
                    dos.writeChar('b');
                    if (blocks_Number < 10) {
                        dos.writeChar('0');
                        dos.writeChar('0');
                        dos.writeChar((char) blocks_Number);
                    } else {
                        String bns = String.valueOf(blocks_Number);
                        char[] tmp = bns.toCharArray();
                        if (blocks_Number < 99) {
                            dos.writeChar('0');
                            dos.writeChar(tmp[0]);
                            dos.writeChar(tmp[1]);
                        } else {
                            dos.writeChar(tmp[0]);
                            dos.writeChar(tmp[1]);
                            dos.writeChar(tmp[2]);
                        }
                    }

                }
                tree.insert_in_Rtree(new Point(lat[x],lon[x],blocks_Number,count));


                dos.writeLong(id[x]);
                dos.writeDouble(lat[x]);
                dos.writeDouble(lon[x]);
                count++;
            }
            write_tree_in_indexfile(tree); //Αποθήκευση του καταλόγου στο indexfile μέσω της μεθόδου write_tree_in_indexfile
            dos.close();
        } catch (
                IOException e) {
            System.out.println("IOException : " + e);
        }

    }

    /**
     * Συνάρτηση που προσθέτει μια καινούργια τοποθεσία στις ήδη υπάρχουσες στο δέντρο
     * @param location η τοποθεσία που θέλουμε να προσθέσουμε
     * @param locations όλες οι τοποθεσίες που υπάρχουν
     */
    public void add_location(Location location, ArrayList<Location> locations) {
        //προσθέτουμε την τοποθεσία στη λίστα με τις υπόλοιπες τοποθεσίες
        //επαναλαμβάνουμε την ίδια διαδικασία με πριν
        locations.add(location);
        int size = locations.size();
        try {
            FileOutputStream fos = new FileOutputStream("datafile.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            //τα πρώτα 4 ψηφία αντιπροσωπεύουν το blockid
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar('0');
            //Κάθε εισαγωγή περιλαμβάνει 24 bytes
            //άρα για να δημιουργήσουμε μπλοκς με χωρητικότητα 32ΚΒ πρέπει να έχουμε 1365 εισαγωγές
            //επιπλέον τα 8 πρώτα bytes του κάθε μπλοκ αντιπροσωπεύουν το blockid
            dos.writeInt(size/1365+1);
            dos.writeInt(size);
            dos.writeInt(1365);
            int blocks_Number = 1;
            //εμφάνιση μπλοκ id
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar((char) blocks_Number);
            int count = 0;
            for (Location value : locations) {
                switch (count) {
                    case 1365 -> {
                        count = 0;
                        blocks_Number++;
                        dos.writeChar('b');
                        if (blocks_Number < 10) {
                            dos.writeChar('0');
                            dos.writeChar('0');
                            dos.writeChar((char) blocks_Number);
                        } else {
                            String bns = String.valueOf(blocks_Number);
                            char[] tmp = bns.toCharArray();
                            if (blocks_Number < 99) {
                                dos.writeChar('0');
                                dos.writeChar(tmp[0]);
                                dos.writeChar(tmp[1]);
                            } else {
                                dos.writeChar(tmp[0]);
                                dos.writeChar(tmp[1]);
                                dos.writeChar(tmp[2]);
                            }
                        }
                    }
                }
                //γράφουμε τα αποτελέσματα στο datafile
                dos.writeLong(value.getLocation_id());
                dos.writeDouble(value.getLat());
                dos.writeDouble(value.getLon());
                count++;
            }
            dos.close();
        } catch (
                IOException e) {
            System.out.println("IOException : " + e);


        }

    }

    /**
     * Συνάρτηση που αποθηκεύει το R*δέντρο στο indexfile.txt ως object
     */
    public void write_tree_in_indexfile(Tree myRtree) {

        try {
            FileOutputStream myWriter = new FileOutputStream("indexfile.txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(myWriter);
            objectOut.writeObject(myRtree.getRoot());
            objectOut.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing in file");
            e.printStackTrace();
        }
    }

    /**
     * Η μέθοδος αυτή ανοίγει το indexfile.txt και διαβάζει το R*δέντρο
     * @return Το R*tree δέντρο που φορτώθηκε από το indexfile.txt
     */
        public Tree read_tree_from_indexfile() {
            try {
                FileInputStream myWriter = new FileInputStream("indexfile.txt");
                ObjectInputStream objectOut = new ObjectInputStream(myWriter);
                Tree myTree = new Tree((TreeNode) objectOut.readObject());
                objectOut.close();
                return myTree;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return null;
            }
        }


    /**
     * Η μέθοδος αυτή σκανάρει το αρχείο osm και αποθηκεύει τα σημεία σε μία λίστα point_list
     * @return Η λίστα με τα σημεία
     */
    public ArrayList<Point> parse_points() throws IOException, SAXException, ParserConfigurationException {
        ArrayList<Point> point_list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File("map.osm"));
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("node");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                point_list.add(new Point(Double.parseDouble(eElement.getAttribute("lat")),Double.parseDouble(eElement.getAttribute("lon"))));

            }
        }
        return point_list;
    }
}