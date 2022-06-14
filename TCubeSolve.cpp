#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>
#include <string.h>
#include <sys/resource.h>
#include <unistd.h>
using namespace std;

const int orientations [12][3][3] =
{
    //          x1                        x2                        x3                         x4
    {{1,0,0},{2,0,0},{1,1,0}},{{1,0,0},{2,0,0},{1,0,1}},{{1,0,0},{2,0,0},{1,-1,0}},{{1,0,0},{2,0,0},{1,0,-1}},
    //          y1                        y2                        y3                         y4
    {{0,1,0},{0,2,0},{1,1,0}},{{0,1,0},{0,2,0},{0,1,1}},{{0,1,0},{0,2,0},{-1,1,0}},{{0,1,0},{0,2,0},{0,1,-1}},
    //          z1                        z2                        z3                         z4
    {{0,0,1},{0,0,2},{1,0,1}},{{0,0,1},{0,0,2},{0,1,1}},{{0,0,1},{0,0,2},{-1,0,1}},{{0,0,1},{0,0,2},{0,-1,1}}
};
/*const int x1 [3][3] = {{1,0,0},{2,0,0},{1,1,0}};
const int x2 [3][3] = {{1,0,0},{2,0,0},{1,0,1}};
const int x3 [3][3] = {{1,0,0},{2,0,0},{1,-1,0}};
const int x4 [3][3] = {{1,0,0},{2,0,0},{1,0,-1}};
const int y1 [3][3] = {{0,1,0},{0,2,0},{1,1,0}};
const int y2 [3][3] = {{0,1,0},{0,2,0},{0,1,1}};
const int y3 [3][3] = {{0,1,0},{0,2,0},{-1,1,0}};
const int y4 [3][3] = {{0,1,0},{0,2,0},{0,1,-1}};
const int z1 [3][3] = {{0,0,1},{0,0,2},{1,0,1}};
const int z2 [3][3] = {{0,0,1},{0,0,2},{0,1,1}};
const int z3 [3][3] = {{0,0,1},{0,0,2},{-1,0,1}};
const int z4 [3][3] = {{0,0,1},{0,0,2},{0,-1,1}};*/
ofstream outFile;

long getMemusage()
{
    struct rusage myusage;
    getrusage(RUSAGE_SELF, &myusage);
    return myusage.ru_maxrss;
}
bool isOpen(int (* cube)[6][6][6],const int orientation[3][3], vector<int> point)
{
    bool in = true;
    bool open = true;
    for(int i = 0; i < 3; i++)
    {
        for(int p = 0; p < 3; p++)
        {
            if(orientation[i][p] + point[p] < 0 || orientation[i][p] + point[p] > 5)
            {
                in = false;
            }            
        }
        if(in)
        {
            if((*cube)[orientation[i][0] + point[0]][orientation[i][1] + point[1]][orientation[i][2] + point[2]] != -1)
            {
                outFile << "colision at {" << orientation[i][0] + point[0] << ", " << orientation[i][1] + point[1] << ", " << orientation[i][2] + point[2] << "}"<<endl;
                open = false;
            }
        }
    }
    return open && in;
}
int addT(int (* cube)[6][6][6], vector<int> point)
{
    outFile <<"addT "<< point[0] << " " << point[1] << " " << point[2] << endl;
    int orientation = -2;
    for(int i = 0; i < 12; i++)
    {
        outFile<< "orientation " << i << endl;
        if(isOpen(cube,orientations[i],point))
        {    
            outFile << "in" << endl;                   
            (*cube)[point[0]][point[1]][point[2]] = i;
            outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"} = "<<i<<endl;
            for(int j = 0; j < 3; j++)
            {
                (*cube)[point[0] + orientations[i][j][0]][point[1] + orientations[i][j][1]][point[2] + orientations[i][j][2]] = i;
                outFile <<"{"<<point[0] + orientations[i][j][0]<<","<<point[1] + orientations[i][j][1]<<","<<point[2] + orientations[i][j][2]<<"} = "<<i<<endl;
            }
            orientation = i;
            i = 12;
        }
    }
    return orientation;
}
void updateExplored (vector<vector<int>> *cubeFrontier ,vector<vector<int>> *cubeExplored, vector<vector<int>> newExplored)
{
    //remove newExplored points from frontier
    int index = 0;
    while(index < cubeFrontier->size())
    {
        bool in = false;
        for(vector<int> point : newExplored)
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if((*cubeFrontier )[index][k] != point[k])
                {
                    pointMatch = false;
                }
            }
            if(pointMatch)
            {
                in = true;
            }
        }
        if(in)
        {
            cubeFrontier->erase(next(cubeFrontier ->begin(),index));                       
            index --; 
        }
        index ++;
    }
    outFile << "frontier -  newExplored:"; 
    for(vector<int> point :(*cubeFrontier))
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
    
    //add newExplored points to explored
    index = 0;
    if(cubeExplored->size() == 0)
    {
        outFile << "0 cubeExplored" << endl;
        cubeExplored->insert(cubeExplored->end(),newExplored.begin(),newExplored.end());
    }
    else
    {
        for(vector<int> newpoint : newExplored)
        {
            bool in = false;
            for(vector<int> point : (*cubeExplored))
            {
                bool pointMatch = true;
                for(int k = 0; k < 3; k++)// match xyz
                {
                    if(newpoint[k] != point[k])
                    {
                        pointMatch = false;
                    }
                }
                if(pointMatch)
                {
                    in = true;
                }
            }
            if(!in)
            {
                cubeExplored->push_back(vector<int>{newpoint[0],newpoint[1],newpoint[2]});
                index --; 
            }
            index ++;
        }
    }
    outFile << "explored + newExplored:"; 
    for(vector<int> point :(*cubeExplored))
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
}
void updateFrontier (vector<vector<int>> *cubeFrontier ,vector<vector<int>> *cubeExplored, vector<vector<int>> newExplored)
{
    //get all possible new frontier points in range 
    vector<vector<int>> newFrontier;
    for(vector<int> point : newExplored)
    {
        if(point[0] + 1 >= 0 && point[0] + 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0] + 1,point[1],point[2]});
        }
        if(point[0] - 1 >= 0 && point[0] - 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0] - 1,point[1],point[2]});
        }
        if(point[1] + 1 >= 0 && point[1] + 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0],point[1] + 1,point[2]});
        }
        if(point[1] - 1 >= 0 && point[1] - 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0],point[1] - 1,point[2]});
        }
        if(point[2] + 1 >= 0 && point[2] + 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0],point[1],point[2] + 1});
        }
        if(point[2] - 1 >= 0 && point[2] - 1 < 6)
        {
            newFrontier.push_back(vector<int>{point[0],point[1],point[2] - 1});
        }
    }
    outFile << "newFrontier:"; 
    for(vector<int> point :newFrontier)
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
    
    //remove explored points from newFrontier
    int index = 0;
    while(index < newFrontier.size())
    {
        bool in = false;
        for(vector<int> point : (*cubeExplored))
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if(newFrontier[index][k] != point[k])
                {
                    pointMatch = false;
                }
            }
            if(pointMatch)
            {
                in = true;
            }
        }
        if(in)
        {
            newFrontier.erase(next(newFrontier.begin(),index));                       
            index --; 
        }
        index ++;
    }
    outFile << "newFrontier - explored:"; 
    for(vector<int> point :newFrontier)
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
    
    //add newFrontier points to frontier
    index = 0;
    if(cubeFrontier->size() == 0)
    {
        outFile << "0 cubeFrontier" << endl;
        for(int i = 0; i < newFrontier.size(); i++)
        {
            bool in = false;
            for(int j = i+1; j < newFrontier.size(); j++)
            {
                bool pointMatch = true;
                for(int k = 0; k < 3; k++)
                {
                    if(newFrontier[i][k] != newFrontier[j][k])
                    {
                        pointMatch = false;
                    }
                }
                if(pointMatch)
                {
                    in = true;
                }
            }
            if(!in)
            {
                cubeFrontier->push_back(vector<int>{newFrontier[i][0],newFrontier[i][1],newFrontier[i][2]});
            }
        }
    }
    else
    {
        for(vector<int> newpoint : newFrontier)
        {
            bool in = false;
            for(vector<int> point : (*cubeFrontier))
            {
                bool pointMatch = true;
                for(int k = 0; k < 3; k++)// match xyz
                {
                    if(newpoint[k] != point[k])
                    {
                        pointMatch = false;
                    }
                }
                if(pointMatch)
                {
                    in = true;
                }
            }
            if(!in)
            {
                cubeFrontier->push_back(vector<int>{newpoint[0],newpoint[1],newpoint[2]});
                index --; 
            }
            index ++;
        }
    }
    outFile << "frontier + newFrontier:"; 
    for(vector<int> point :(*cubeFrontier))
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
}
void update (vector<vector<int>> *cubeFrontier ,vector<vector<int>> *cubeExplored, int orientation)
{
    //get all new explored points
    vector<vector<int>> newExplored{vector<int>{(*cubeFrontier )[0][0],(*cubeFrontier )[0][1],(*cubeFrontier )[0][2]}};
    for(int i = 0; i < 3; i++)
    {
        newExplored.push_back(vector<int>{newExplored[0][0] + orientations[orientation][i][0],newExplored[0][1] + orientations[orientation][i][1],newExplored[0][2] + orientations[orientation][i][2]});
    }
    outFile << "newExplored :"; 
    for(vector<int> point :newExplored)
    {
        outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"}";
    }
    outFile<<endl;
    
    updateExplored(cubeFrontier,cubeExplored,newExplored); 
    updateFrontier(cubeFrontier,cubeExplored,newExplored);  
}

int main()
{    
    int cube [6][6][6];
    vector<vector<int>> cubeFrontier {vector<int> (3, 0)};
    vector<vector<int>> cubeExplored;
    outFile.open("output.txt");

    outFile<< "sdrghfd"<< endl;
    for(int x = 0; x < 6; x++) 
    {
        for(int y = 0; y < 6; y++) 
        {
            for(int z = 0; z < 6; z++) 
            {
                cube[x][y][z] = -1;
            }
        }
    }
    outFile<<endl;
    int tAdded = 0;
    while(cubeFrontier.size() > 0)
    {
        int orientation = addT(&cube,cubeFrontier [0]);
        if(orientation != -2)
        {
            update (&cubeFrontier,&cubeExplored,orientation);
            outFile << tAdded << " t's" << endl;
            for(int i = 0; i < 6; i++)
            {
                string layer = "";
                bool allEmpty = true;
                for(int j = 0; j < 6; j++)
                {
                    for(int k = 0; k < 6; k++)
                    {
                        if(cube[k][i][j] != -1)
                        {
                            allEmpty = false;
                            if(cube[k][i][j] < 10)
                            {
                                layer = layer + "0" + to_string(cube[k][i][j]) + ", ";
                            }
                            else
                            {
                                layer = layer + to_string(cube[k][i][j]) + ", ";
                            }
                        }
                        else
                        {
                            layer = layer + to_string(cube[k][i][j]) + ", ";
                        }                        
                    }
                    layer = layer + "\n";
                }
                if(!allEmpty)
                {
                    outFile << layer << endl;
                }
            }
            tAdded++;
        }
        else
        {
            outFile << "erase {" << cubeFrontier[0][0] << ", " << cubeFrontier[0][1] << ", " << cubeFrontier[0][2] << "}" << endl;
            cubeFrontier.erase(cubeFrontier.begin());
        }
    }
    outFile.close();
}