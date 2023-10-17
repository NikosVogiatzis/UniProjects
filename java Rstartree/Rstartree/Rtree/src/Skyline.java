import java.util.ArrayList;

public class Skyline {

    /**
     * Συνάρτηση που υλοποιεί τα ερωτήματα κορυφογραμμής
     * Επιστρέφει μια λίστα με τα στοιχεία που ανήκουν στην κορυφογραμμή
     * Ουσιαστικά ελέγχει κάθε φορά τα σημεία στον χώρο για το αν κυριαρχούνται από κάποια άλλα ή οχι
     * αν δεν κυριαρχούνται μπαίνουν στη λίστα των αποτελεσμάτων αλλιώς αποκλείονται
     **/
    public ArrayList<SkylineNode> calculateSkyline(Tree tree) {
        ArrayList<SkylineNode> skylineResults = new ArrayList<>();
        MinHeapSkyline heap = new MinHeapSkyline(99999);
        heap.insert_to_minHeap_for_skyline(new SkylineNode(tree.getRoot()));

        while (!heap.isEmpty()) {
            SkylineNode popNode = heap.remove_for_skyline();
            boolean isDominated = false;

            for (SkylineNode n : skylineResults) {
                if (n.getX() >= popNode.getX() && n.getY() >= popNode.getY()) {
                    isDominated = true;
                    break;
                }
            }

            if (isDominated) continue;

            if (!popNode.node.isLeaf()) {
                for (TreeNode childNode : popNode.node.getChildren()) {
                    SkylineNode childSkylineNode = new SkylineNode(childNode);
                    isDominated = false;

                    for (SkylineNode ans : skylineResults) {
                        if (childSkylineNode.getX() >= ans.getX() && childSkylineNode.getY() >= ans.getY()) {
                            isDominated = true;
                            break;
                        }
                    }

                    if (!isDominated) {
                        heap.insert_to_minHeap_for_skyline(childSkylineNode);
                    }
                }
            } else {
                for (Point point : popNode.node.getPoints()) {
                    SkylineNode pointSkylineNode = new SkylineNode(point);
                    isDominated = false;

                    for (SkylineNode ans : skylineResults) {
                        if (pointSkylineNode.getX() >= ans.getX() && pointSkylineNode.getY() >= ans.getY()) {
                            isDominated = true;
                            break;
                        }
                    }

                    if (!isDominated) {
                        skylineResults.add(pointSkylineNode);
                    }
                }
            }
        }
        return skylineResults;
    }
}
