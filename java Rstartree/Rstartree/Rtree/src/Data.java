import java.io.*;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

public class Data {

    /**
     * Συνάρτηση που διαβάζει με τον κατάλληλο τρόπο το datafile.txt και δημιουργεί μία λίστα με όλες τις εισαγωγές
     * την οποία επιστρέφει στο τέλος
     * @return μια Arraylist με εισαγωγές
     */
    public ArrayList<Location> read_datafile() {
        ArrayList<Location> locationsArrayList = new ArrayList<>();
        try {

            DataInputStream stream = new DataInputStream(new FileInputStream("datafile.txt"));
            //το πρώτο μπλοκ με τα μεταδεδομένα είναι 20 Bytes
            byte[] metadata = new byte[20];

            stream.read(metadata, 0, 20);
            byte[] number_of_blocks = new byte[4];
            byte[] number_of_entries = new byte[4];
            byte[] records_per_block = new byte[4];
            int bytes = 0; //μετρητής των bytes
            //αγνοούμε τα 8 πρώτα bytes σε κάθε μπλοκ
            for(int counter=8;counter<20;counter++){
                //ανά 4 bytes αλλάζουν οι μεταβλητές
                if(bytes==4)
                    bytes=0;
                if(counter<12)
                    number_of_blocks[bytes]=metadata[counter];
                else if(counter<16)
                    number_of_entries[bytes]=metadata[counter];
                else
                    records_per_block[bytes]=metadata[counter];
                bytes++;
            }
            //Τα δεδομένα έχουν διαβαστεί ως μια σειρά από bytes
            ByteBuffer bfMeta = ByteBuffer.wrap(number_of_blocks);
            IntBuffer lgMeta = bfMeta.asIntBuffer();
            int Blocks_Num = lgMeta.get();
            bfMeta = ByteBuffer.wrap(records_per_block);
            lgMeta = bfMeta.asIntBuffer();
            int Records_Block = lgMeta.get();
            //η ανάγνωση γίνεται ανα μπλοκ
            for (int o = 0; o < Blocks_Num; o++) {
                byte[] buffer = new byte[32768]; //32ΚΒ μπλοκ
                stream.read(buffer, 0, 32768);
                byte[][] ids = new byte[Records_Block][8];
                int k = 0;
                int l = 0;
                byte[][] lats = new byte[Records_Block][8];
                byte[][] lots = new byte[Records_Block][8];
                int i = 0;
                int count = 0;
                //παραλείπονται τα πρώτα 8 bytes από κάθε μπλοκ γιατί εκεί δεσμεύεται το id κάθε μπλοκ
                for (int m = 8; m < 32768; m++) {
                    if (i == 24) {
                        i = 0;
                        l = 0;
                        k = 0;
                        count++;
                    }
                    if (i < 8)
                        ids[count][i] = buffer[m];
                    else if (i < 16) {
                        lats[count][k] = buffer[m];
                        k++;
                    } else {
                        lots[count][l] = buffer[m];
                        l++;
                    }
                    i++;
                }
                for (int c = 0; c < Records_Block; c++) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(ids[c]);
                    LongBuffer longBuffer = byteBuffer.asLongBuffer();
                    long tempId = longBuffer.get();
                    byteBuffer = ByteBuffer.wrap(lats[c]);
                    DoubleBuffer bs = byteBuffer.asDoubleBuffer();
                    double tempLot = bs.get();
                    byteBuffer = ByteBuffer.wrap(lots[c]);
                    DoubleBuffer bl = byteBuffer.asDoubleBuffer();
                    double tempLon = bl.get();
                    if (tempId == 0 && tempLot == 0 && tempLon == 0) {
                        break;
                    }
                    locationsArrayList.add(new Location(tempId, tempLot, tempLon));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            return locationsArrayList;
        }

    public Location get_block_loc(int block_id, int slot_id) {
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile("datafile.txt", "rw");
            accessFile.skipBytes(20);
            accessFile.skipBytes(32768 * --block_id);
            accessFile.skipBytes(8);
            accessFile.skipBytes(24 * slot_id);
            long id= accessFile.readLong();
            double lat=accessFile.readDouble();
            double lon=accessFile.readDouble();
            return new Location(id, lat, lon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
