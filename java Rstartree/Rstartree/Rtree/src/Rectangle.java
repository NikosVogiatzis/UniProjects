import java.util.ArrayList;
import java.io.Serializable;

/** Κλάση που αναπαριστά ένα ορθογώνιο
 */
public class Rectangle implements Serializable {
    private double min_x;
    private double max_x;
    private double min_y;
    private double max_y;

    /**
     * Κατασκευαστής της κλάσης με 4 ορίσματα
     */
    public Rectangle(double x1, double x2, double y1, double y2){
        this.min_x =x1;
        this.max_x =x2;
        this.min_y =y1;
        this.max_y =y2;
    }

    /**
     * Μέθοδος που επιστρέφει και τις 4 τιμές του ορθογωνίου
     * σε μία λίστα 4ων κελιών
     */
    public ArrayList<Double> getAllValues(){
        ArrayList<Double> values = new ArrayList<>(4);
        values.add(min_x);
        values.add(max_x);
        values.add(min_y);
        values.add(max_y);
        return values;
    }

    public double getMin_x(){
        return min_x;
    }
    public double getMax_x(){
        return max_x;
    }
    public double getMin_y(){
        return min_y;
    }
    public double getMax_y(){
        return max_y;
    }

    /**
     * Μέθοδος υπολογισμού εμβαδού του MBR
     */
    public double getArea(){
        return (max_x - min_x) * (max_y - min_y);
    }

    /**
     * Μέθοδος υπολογισμού της περιμέτρου του MBR
     */
    public double getPerimeter(){
        return 2 * (max_x - min_x) + 2 * (max_y - min_y);
    }

    /**
     * Μέθοδος που υπολογίζει την επικάλυψη που υπάρχει μεταξύ δύο ορθογωνίων
     */
    public double calculate_overlap(Rectangle second_rectangle){
        double x1 = second_rectangle.getMin_x();
        double x2 = second_rectangle.getMax_x();
        double y1 = second_rectangle.getMin_y();
        double y2 = second_rectangle.getMax_y();
        double overlap_x = Math.max(0,Math.min(max_x,x2)- Math.max(min_x,x1));
        double overlap_y = Math.max(0,Math.min(max_y,y2)-Math.max(min_y,y1));
        if(overlap_x == 0){
            return overlap_y;
        }
        if(overlap_y == 0){
            return overlap_x;
        }
        return overlap_x * overlap_y;
    }


    /**
     * Η μέθοδος αυτή υπολογίζει την επέκταση του ορθογωνίου έπειτα από την προσθήκη του σημείου (x,y)
     * @param x η τιμή του x του προστιθέμενου σημείου
     * @param y η τιμή του y του προστιθέμενου σημείου
     */
    public double calculate_new_area(double x, double y){
        if(min_x <= x && x <= max_x && y > max_y){
            return (max_x - min_x) * (y- min_y);
        }
        else if(min_y <= y && y <= max_y && x > max_x){
            return (x - min_x) * (max_y - min_y);
        }
        else if(min_x <= x && x <= max_x && y < min_y){
            return (max_x - min_x) * (max_y - y);

        }
        else if(min_y <= y && y <= max_y && x < min_x){
            return (max_x - x) * (max_y - min_y);
        }
        else if(x < min_x && y > max_y){
            return (max_x - x) * (y - min_y);
        }
        else if(x > max_x && y > max_y){
            return (x - min_x) * (y - min_y);
        }
        else if(x > max_x && y < min_y){
            return (x - min_x) * (max_y - y);
        }
        else if(x < min_x && y < min_y){
            return (max_x - x) * (max_y - y);
        }
        else if(x >= min_x && x <= max_x && y >= min_y && y <= max_y){
            return (max_x - min_x) * (max_y - min_y);
        }
        else {
            System.out.println("Error in calculating area");
            return -1;
        }
    }

    /**
     * Μέθοδος που προσθέτει ένα καινούργιο σημείο σε ένα ορθογώνιο και αλλάζει τις τιμές min_x, max_x, min_y, max_y
     * @param x η τιμή του άξονα x του σημείου που θέλουμε να προσθέσουμε
     * @param y η τιμή του άξονα y του σημείου που θέλουμε να προσθέσουμε
     */
    public void setNewDimensions(double x, double y){
        if(min_x == Double.MAX_VALUE && max_x == Double.MIN_VALUE && min_y == Double.MAX_VALUE && max_y == Double.MIN_VALUE){
            min_x = x; min_y = y; max_x = x; max_y = y;
            return;

        }
        if(min_x <= x && x <= max_x && y > max_y)
            max_y = y;
        else if(min_y <= y && y <= max_y && x < min_x)
            min_x = x;
        else if(min_x <= x && x <= max_x && y < min_y)
            min_y = y;
        else if(min_y <= y && y <= max_y && x > max_x)
            max_x = x;
        else if(x < min_x && y > max_y){
            min_x = x;
            max_y = y;
        }else if(x > max_x && y < min_y){
            max_x = x;
            min_y = y;
        }else if(x > max_x && y > max_y){
            max_x = x;
            max_y = y;
        } else if(x < min_x && y < min_y){
            min_x = x;
            min_y = y;
        }
        else if(x <= max_x && x >= min_x && y >= min_y && y <= max_y){
        }
        else System.out.println("Error in setting dimensions");
    }

    /**
     * Μέθοδος που αναπροσαρμόζει τις τιμές ενός ορθογωνίου όταν προστεθεί σε αυτό ένας επιπλέον κόμβος
     * @param x1 η μικρότερη τιμή στον άξονα των x
     * @param x2 η μεγαλύτερη τιμή στον άξονα των x
     * @param y1 η μικρότερη τιμή στον άξονα των y
     * @param y2 η μεγαλύτερη τιμή στον άξονα των y
     */
    public void setNewDimensions(double x1, double x2, double y1, double y2){
        if(min_x == Double.MAX_VALUE && max_x == Double.MIN_VALUE && min_y == Double.MAX_VALUE && max_y == Double.MIN_VALUE){
            min_x = x1;
            max_x = x2;
            min_y = y1;
            max_y = y2;
            return;
        }
        if(x1 < min_x){
            min_x = x1;
        }
        if(x2 > max_x){
            max_x = x2;
        }
        if(y1 < min_y){
            min_y = y1;
        }
        if(y2 > max_y){
            max_y = y2;
        }
    }

    /**
     * Μέθοδος που υπολογίζει την απόσταση μεταξύ ενός σημείου και ενός ορθογωνίου
     * @param point σημείο που θέλουμε να μετρήσουμε πόσο μακριά είναι από ένα ορθογώνιο
     */
    public double distance_between_point_and_MBR(Point point){
        double x = point.getLat();
        double y = point.getLon();
        if(min_x <= x && x <= max_x && y > max_y){
            return y - max_y;
        }
        else if(min_y <= y && y <= max_y && x > max_x){
            return x - max_x;
        }
        else if(min_x <= x && x <= max_x && y < min_y){
            return min_y - y;
        }
        else if(min_y <= y && y <= max_y && x < min_x){
            return min_x - x;
        }
        else if(x < min_x && y > max_y){
            return Math.abs(min_x - x) + Math.abs(max_y - y);
        }
        else if(x > max_x && y > max_y){
            return Math.abs(max_x - x) + Math.abs(max_y - y);
        }
        else if(x > max_x && y < min_y){
            return Math.abs(max_x - x) + Math.abs(min_y - y);
        }
        else if(x < min_x && y < min_y){
            return Math.abs(min_x - x) + Math.abs(min_y - y);
        }
        return 0;
    }


}
