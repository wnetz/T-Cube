import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main
{
    public static final int WIDTH = 5;
    public static final int DEPTH = 5;
    public static final int HEIGHT = 3;
    public static final int ORIENTATION_FAIL = -2;
    public static final String PIECE_ROTATION_FILE = "src/t_piece_rotations.txt";
    public static final String CUBE_ROTATION_FILE = "src/t_cube_rotations.txt";
    public static final int PRINT_TO = 1;
    public static FileWriter file;
    public static void main(String[] args) throws IOException 
    {
        file = new FileWriter("solutions.txt");
        piece_rotations(new Piece(new int[][]{{ 1, 0, 0},{ 2, 0, 0},{ 1, 1, 0}}));
        Cube_rotations();
        Piece.rotations = Rotations.read_cube_rotations(Main.CUBE_ROTATION_FILE);
        Piece.rotationKeys = Piece.rotations.keySet().toArray();
        SolutionState.ORIENTATIONS = Rotations.read_piece_rotations(Main.PIECE_ROTATION_FILE);
        SolutionState.ORIENTATIONS_SIZE = SolutionState.ORIENTATIONS.size();
        Solve solve = new Solve(PRINT_TO, file);
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
    public static void piece_rotations(Piece piece)
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
                /*System.out.println(home + switch (axis) {
                    case 0 -> "X-axis";
                    case 1 -> "Y-axis";
                    case 2 -> "Z-axis";
                    default -> throw new IllegalStateException("Unexpected value: " + axis);
                });*/
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
        for (ArrayList<Point> points: rotation_set)
        {
            System.out.println(points);
        }
        //save rotation set
        Rotations.set_piece_rotations(rotation_set);
        Rotations.write_piece_rotations(PIECE_ROTATION_FILE);
    }
    public static void Cube_rotations()
    {
        Hashtable<Point,ArrayList<Integer>> rotation_set = new Hashtable<>();
        for(int orientation = 0; orientation < 12; orientation++)
        {
            for (int reflect = 0; reflect < 2; reflect++)
            {
                Piece new_piece = new Piece(new Point(1, 2, 3), orientation);
                if (reflect == 1)
                {
                    new_piece = reflect_xy(new_piece);
                }
                if (rotation_set.get(new_piece.get_point()) != null && !rotation_set.get(new_piece.get_point()).contains(new_piece.get_orientation()))
                {
                    rotation_set.get(new_piece.get_point()).add(new_piece.get_orientation());
                }
                else  if (rotation_set.get(new_piece.get_point()) == null)
                {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(new_piece.get_orientation());
                    rotation_set.put(new_piece.get_point(), temp);
                }
                for (int first = 0; first < 3; first++)
                {
                    //create copy
                    Piece first_rotation = new Piece(new_piece.get_point(), new_piece.get_orientation());

                    new_piece = switch (first){
                        case 0 -> rotate_x(new_piece);
                        case 1 -> rotate_y(new_piece);
                        case 2 -> rotate_z(new_piece);
                        default -> throw new IllegalStateException("Unexpected value: " + first);
                    };
                    //add first rotation
                    if (rotation_set.get(new_piece.get_point()) != null && !rotation_set.get(new_piece.get_point()).contains(new_piece.get_orientation()))
                    {
                        rotation_set.get(new_piece.get_point()).add(new_piece.get_orientation());
                    }
                    else  if (rotation_set.get(new_piece.get_point()) == null)
                    {
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(new_piece.get_orientation());
                        rotation_set.put(new_piece.get_point(), temp);
                    }
                    for (int second = 0; second < 3; second++)
                    {
                        //create copy
                        Piece second_rotation = new Piece(new_piece.get_point(), new_piece.get_orientation());

                        new_piece = switch (second) {
                            case 0 -> rotate_x(new_piece);
                            case 1 -> rotate_y(new_piece);
                            case 2 -> rotate_z(new_piece);
                            default -> throw new IllegalStateException("Unexpected value: " + second);
                        };
                        //add second rotation
                        if (rotation_set.get(new_piece.get_point()) != null && !rotation_set.get(new_piece.get_point()).contains(new_piece.get_orientation()))
                        {
                            rotation_set.get(new_piece.get_point()).add(new_piece.get_orientation());
                        }
                        else  if (rotation_set.get(new_piece.get_point()) == null)
                        {
                            ArrayList<Integer> temp = new ArrayList<>();
                            temp.add(new_piece.get_orientation());
                            rotation_set.put(new_piece.get_point(), temp);
                        }
                        for (int third = 0; third < 3; third++) {
                            //create copy
                            Piece third_rotation = new Piece(new_piece.get_point(), new_piece.get_orientation());
                            new_piece = switch (third) {
                                case 0 -> rotate_x(new_piece);
                                case 1 -> rotate_y(new_piece);
                                case 2 -> rotate_z(new_piece);
                                default -> throw new IllegalStateException("Unexpected value: " + third);
                            };
                            //add third rotation
                            if (rotation_set.get(new_piece.get_point()) != null && !rotation_set.get(new_piece.get_point()).contains(new_piece.get_orientation()))
                            {
                                rotation_set.get(new_piece.get_point()).add(new_piece.get_orientation());
                            }
                            else  if (rotation_set.get(new_piece.get_point()) == null)
                            {
                                ArrayList<Integer> temp = new ArrayList<>();
                                temp.add(new_piece.get_orientation());
                                rotation_set.put(new_piece.get_point(), temp);
                            }
                            for (int forth = 0; forth < 3; forth++) {
                                //create copy
                                Piece forth_rotation = new Piece(new_piece.get_point(), new_piece.get_orientation());
                                new_piece = switch (forth) {
                                    case 0 -> rotate_x(new_piece);
                                    case 1 -> rotate_y(new_piece);
                                    case 2 -> rotate_z(new_piece);
                                    default -> throw new IllegalStateException("Unexpected value: " + forth);
                                };
                                //add forth rotation
                                if (rotation_set.get(new_piece.get_point()) != null && !rotation_set.get(new_piece.get_point()).contains(new_piece.get_orientation()))
                                {
                                    rotation_set.get(new_piece.get_point()).add(new_piece.get_orientation());
                                }
                                else if (rotation_set.get(new_piece.get_point()) == null)
                                {
                                    ArrayList<Integer> temp = new ArrayList<>();
                                    temp.add(new_piece.get_orientation());
                                    rotation_set.put(new_piece.get_point(), temp);
                                }
                                //rest rotation
                                new_piece = new Piece(forth_rotation.get_point(), forth_rotation.get_orientation());
                            }
                            //rest rotation
                            new_piece = new Piece(third_rotation.get_point(), third_rotation.get_orientation());
                        }
                        //rest rotation
                        new_piece = new Piece(second_rotation.get_point(), second_rotation.get_orientation());
                    }
                    //rest rotation
                    new_piece = new Piece(first_rotation.get_point(), first_rotation.get_orientation());
                }
            }
        }
        for (Point point: rotation_set.keySet())
        {
            System.out.println(point+ ":" + rotation_set.get(point));
        }

        //save rotation set
        Rotations.set_cube_rotations(rotation_set);
        Rotations.write_cube_rotations(CUBE_ROTATION_FILE);
    }
    public static Piece rotate_x(Piece piece)
    {
        int orientation = switch (piece.get_orientation()){
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 0;
            case 4 -> 8;
            case 5 -> 9;
            case 6 -> 10;
            case 7 -> 11;
            case 8 -> 4;
            case 9 -> 7;
            case 10 -> 6;
            case 11 -> 5;
            default -> -1;
        };
        //x,-z,y
        Point new_point = new Point(piece.get_point().x(),-piece.get_point().z(),piece.get_point().y());
        return new Piece(new_point,orientation);
    }
    public static Piece rotate_y(Piece piece)
    {
        int orientation = switch (piece.get_orientation()){
            case 0 -> 11;
            case 1 -> 10;
            case 2 -> 9;
            case 3 -> 8;
            case 4 -> 5;
            case 5 -> 6;
            case 6 -> 7;
            case 7 -> 4;
            case 8 -> 1;
            case 9 -> 2;
            case 10 -> 3;
            case 11 -> 0;
            default -> -1;
        };
        //z,y,-x
        Point new_point = new Point(piece.get_point().z(),piece.get_point().y(),-piece.get_point().x());
        return new Piece(new_point,orientation);
    }
    public static Piece rotate_z(Piece piece)
    {
        int orientation = switch (piece.get_orientation()){
            case 0 -> 4;
            case 1 -> 5;
            case 2 -> 6;
            case 3 -> 7;
            case 4 -> 2;
            case 5 -> 1;
            case 6 -> 0;
            case 7 -> 3;
            case 8 -> 9;
            case 9 -> 10;
            case 10 -> 11;
            case 11 -> 8;
            default -> -1;
        };
        //-y,x,z
        Point new_point = new Point(-piece.get_point().y(),piece.get_point().x(),piece.get_point().z());
        return new Piece(new_point,orientation);
    }
    public static Piece reflect_xy(Piece piece)
    {
        int orientation = switch (piece.get_orientation()){
            case 0 -> 0;
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            case 4 -> 4;
            case 5 -> 7;
            case 6 -> 6;
            case 7 -> 5;
            case 8 -> 8;
            case 9 -> 9;
            case 10 -> 10;
            case 11 -> 11;
            default -> -1;
        };
        //x,y,-z
        Point new_point = new Point(piece.get_point().x(),piece.get_point().y(),-piece.get_point().z());
        return new Piece(new_point,orientation);
    }

}
