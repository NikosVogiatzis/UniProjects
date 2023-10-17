package GUI;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Η κλάση αυτή δημιουργεί φίλτρο για τα JFileChooser που δημιουργούνται κατά την προσθήκη
 * νέου καταλύματος από τους παρόχους και την επιλογή του από αρχεία του υπολογιστή. Δέχεται μόνο
 * αρχεία που έχουν τις παρακάτω καταλήξεις, οι οποίες δηλώνουν ότι αφορούν μόνο φωτογραφικό υλικό
 */
class ImageFilter extends FileFilter {
    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            return extension.equals(TIFF) ||
                    extension.equals(TIF) ||
                    extension.equals(GIF) ||
                    extension.equals(JPEG) ||
                    extension.equals(JPG) ||
                    extension.equals(PNG);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Image Only";
    }

    String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
