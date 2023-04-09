import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main
{
    public static final int PRINT_TO = 1;
    public static FileWriter file;
    public static void main(String[] args) throws IOException 
    {
        file = new FileWriter("src/t_piece_rotations.txt");
        piece_rotations(new Piece(new int[][]{{ 1, 0, 0},{ 2, 0, 0},{ 1, 1, 0}}),file);
        //Solve solve = new Solve(PRINT_TO, file);
        file.close();
    }
    public static void print(String string) throws IOException
    {
        switch (PRINT_TO)
        {
            case 0 -> System.out.print(string);
            case 1 -> file.write(string);
            default -> {}
        }
    }
    public static void piece_rotations(Piece piece, FileWriter file) throws IOException
    {
        ArrayList<ArrayList<Point>> rotation_set = new ArrayList<>();
        ArrayList<Point> base = piece.get_array();
        for(Point home: base)
        {
            //move points based on home
            ArrayList<Point> current_rotation = new ArrayList<>();
            for(Point point: base)
            {
                current_rotation.add(Point.subtract(point,home));
            }
            //rotate for each axis
            for(int axis = 0; axis < 3; axis++)
            {
                System.out.println(home + switch (axis) {
                    case 0 -> "X-axis";
                    case 1 -> "Y-axis";
                    case 2 -> "Z-axis";
                    default -> throw new IllegalStateException("Unexpected value: " + axis);
                });
                //deep copy
                ArrayList <Point> temp = new ArrayList<>();
                temp.addAll(current_rotation);
                rotation_set.add(temp);
                for(int rotations = 1; rotations < 4; rotations++)
                {
                    temp = new ArrayList<>();
                    for (int i = 0; i < current_rotation.size(); i++)
                    {
                        current_rotation.set(i, current_rotation.get(i).rotate_about_origin(axis));
                    }
                    //deep copy
                    temp.addAll(current_rotation);
                    rotation_set.add(temp);
                }
                //complete cycle
                for (int i = 0; i < current_rotation.size(); i++)
                {
                    current_rotation.set(i, current_rotation.get(i).rotate_about_origin(axis));
                }
                //orient for next axis
                switch (axis)
                {
                    case 0://set up for y by doing a z on x0
                        for (int i = 0; i < current_rotation.size(); i++)
                        {
                            current_rotation.set(i, current_rotation.get(i).rotate_about_origin(2));
                        }break;
                    case 1://set up for z by doing a x on y0
                        for (int i = 0; i < current_rotation.size(); i++)
                        {
                            current_rotation.set(i, current_rotation.get(i).rotate_about_origin(0));
                        }break;
                }
            }
        }
        System.out.println(rotation_set);
        //save rotation set
        PieceRotations.set_piece_rotations(rotation_set);
        PieceRotations.write_piece_rotations("T.txt");
    }

}
