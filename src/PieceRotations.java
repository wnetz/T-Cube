import java.io.*;
import java.util.ArrayList;

public class PieceRotations implements Serializable
{
    private static ArrayList<ArrayList<Point>> piece_rotations;
    public static void set_piece_rotations(ArrayList<ArrayList<Point>> pr)
    {
        piece_rotations = new ArrayList<>();
        for (ArrayList<Point> piece: pr)
        {
            ArrayList<Point> temp = new ArrayList<>();
            for (Point point: piece)
            {
                temp.add(point);
            }
            piece_rotations.add(temp);
        }
    }
    public static void write_piece_rotations(String file)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(piece_rotations);
            oos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static ArrayList<ArrayList<Point>> read_piece_rotations(String file) {
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            piece_rotations = (ArrayList<ArrayList<Point>>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return piece_rotations;
    }
}
