import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static final int PRINT_TO = 1;
    public static FileWriter file;
    public static void main(String[] args) throws IOException 
    {
        file = new FileWriter("output2.txt");
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

}
