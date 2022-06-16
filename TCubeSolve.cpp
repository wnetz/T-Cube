#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>
#include <string.h>
#include <sys/resource.h>
#include <unistd.h>
using namespace std;
const int numorientations = 24;
const int orientations [numorientations][3][3] =
{
    //          x1                        x2                        x3                         x4
    {{1,0,0},{2,0,0},{1,1,0}},{{1,0,0},{2,0,0},{1,0,1}},{{1,0,0},{2,0,0},{1,-1,0}},{{1,0,0},{2,0,0},{1,0,-1}},
    //          y1                        y2                        y3                         y4
    {{0,1,0},{0,2,0},{1,1,0}},{{0,1,0},{0,2,0},{0,1,1}},{{0,1,0},{0,2,0},{-1,1,0}},{{0,1,0},{0,2,0},{0,1,-1}},
    //          z1                        z2                        z3                         z4
    {{0,0,1},{0,0,2},{1,0,1}},{{0,0,1},{0,0,2},{0,1,1}},{{0,0,1},{0,0,2},{-1,0,1}},{{0,0,1},{0,0,2},{0,-1,1}},
    //          xc1                       xc2                       xc3                        xc4
    {{-1,-1,0},{0,-1,0},{1,-1,0}},{{-1,0,-1},{0,0,-1},{1,0,-1}},{{-1,1,0},{0,1,0},{1,1,0}},{{-1,0,1},{0,0,1},{1,0,1}},
    //          yc1                       yc2                       yc3                        yc4
    {{-1,-1,0},{-1,0,0},{-1,1,0}},{{0,-1,-1},{0,0,-1},{0,1,-1}},{{1,-1,0},{1,0,0},{1,1,0}},{{0,-1,1},{0,0,1},{0,1,1}},
    //          zc1                       zc2                       zc3                        zc4
    {{-1,0,-1},{-1,0,0},{-1,0,1}},{{0,-1,-1},{0,-1,0},{0,-1,1}},{{1,0,-1},{1,0,0},{1,0,1}},{{0,1,-1},{0,1,0},{0,1,1}}
};
/*
0 const int x1 [3][3] = {{1,0,0},{2,0,0},{1,1,0}};
1 const int x2 [3][3] = {{1,0,0},{2,0,0},{1,0,1}};
2 const int x3 [3][3] = {{1,0,0},{2,0,0},{1,-1,0}};
3 const int x4 [3][3] = {{1,0,0},{2,0,0},{1,0,-1}};
4 const int y1 [3][3] = {{0,1,0},{0,2,0},{1,1,0}};
5 const int y2 [3][3] = {{0,1,0},{0,2,0},{0,1,1}};
6 const int y3 [3][3] = {{0,1,0},{0,2,0},{-1,1,0}};
7 const int y4 [3][3] = {{0,1,0},{0,2,0},{0,1,-1}};
8 const int z1 [3][3] = {{0,0,1},{0,0,2},{1,0,1}};
9 const int z2 [3][3] = {{0,0,1},{0,0,2},{0,1,1}};
10const int z3 [3][3] = {{0,0,1},{0,0,2},{-1,0,1}};
11const int z4 [3][3] = {{0,0,1},{0,0,2},{0,-1,1}};
12const int xc1[3][3] = {{-1,-1,0},{0,-1,0},{1,-1,0}}
13const int xc2[3][3] = {{-1,0,-1},{0,0,-1},{1,0,-1}}
14const int xc3[3][3] = {{-1,1,0},{0,1,0},{1,1,0}}
15const int xc4[3][3] = {{-1,0,1},{0,0,1},{1,0,1}}
16const int yc1[3][3] = {{-1,-1,0},{-1,0,0},{-1,1,0}}
17const int yc2[3][3] = {{0,-1,-1},{0,0,-1},{0,1,-1}}
18const int yc3[3][3] = {{1,-1,0},{1,0,0},{1,1,0}}
19const int yc4[3][3] = {{0,-1,1},{0,0,1},{0,1,1}}
20const int zc1[3][3] = {{-1,0,-1},{-1,0,0},{-1,0,1}}
21const int zc2[3][3] = {{0,-1,-1},{0,-1,0},{0,-1,1}}
22const int zc3[3][3] = {{1,0,-1},{1,0,0},{1,0,1}}
23const int zc4[3][3] = {{0,1,-1},{0,1,0},{0,1,1}}
*/
ofstream outFile;
const int height = 2;

long getMemusage()
{
    struct rusage myusage;
    getrusage(RUSAGE_SELF, &myusage);
    return myusage.ru_maxrss;
}
bool isOpen(int (* cube)[6][height][6],const int orientation[3][3], vector<int> point)
{
    bool in = true;
    bool open = true;
    for(int i = 0; i < 3; i++)
    {
        if(orientation[i][0] + point[0] < 0 || orientation[i][0] + point[0] >= 6)
        {
            in = false;
        }
        else if(orientation[i][1] + point[1] < 0 || orientation[i][1] + point[1] >= height)
        {
            in = false;
        }
        else if(orientation[i][2] + point[2] < 0 || orientation[i][2] + point[2] >= 6)
        {
            in = false;
        }
        if(in)
        {
            if((*cube)[orientation[i][0] + point[0]][orientation[i][1] + point[1]][orientation[i][2] + point[2]] != -1)
            {
                outFile << "colision at {" << orientation[i][0] + point[0] << ", " << orientation[i][1] + point[1] << ", " << orientation[i][2] + point[2] << "}" <<endl;
                open = false;
            }
        }
        else
        {
            outFile << "out at {" << orientation[i][0] + point[0] << ", " << orientation[i][1] + point[1] << ", " << orientation[i][2] + point[2] << "}" <<endl;
        }
    }
    return (open && in);
}
int addT(int (* cube)[6][height][6], vector<int> point, int  o)
{
    int orientation = o;
    outFile <<"addT "<< point[0] << " " << point[1] << " " << point[2] << endl;
    for(int i = orientation+1; i < numorientations; i++)
    {
        outFile<< "orientation " << i << endl;
        if(isOpen(cube,orientations[i],point))
        {    
            outFile << "in: " << i << endl;                   
            (*cube)[point[0]][point[1]][point[2]] = i;
            outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"} = "<<i<<endl;
            for(int j = 0; j < 3; j++)
            {
                (*cube)[point[0] + orientations[i][j][0]][point[1] + orientations[i][j][1]][point[2] + orientations[i][j][2]] = i;
                outFile <<"{"<<point[0] + orientations[i][j][0]<<","<<point[1] + orientations[i][j][1]<<","<<point[2] + orientations[i][j][2]<<"} = "<<i<<endl;
            }
            orientation = i;
            i = numorientations;
        }
    }
    if(orientation == o)
    {
        return -2;
    }
    outFile<< "orientation " << orientation << endl;
    return orientation;
}
void removeT(int (* cube)[6][height][6], vector<int> point, int orientation)
{
    outFile <<"removeT "<< point[0] << " " << point[1] << " " << point[2] << " orientation " << orientation << endl;                  
    (*cube)[point[0]][point[1]][point[2]] = -1;
    outFile <<"{"<<point[0]<<","<<point[1]<<","<<point[2]<<"} = "<<-1<<endl;
    for(int j = 0; j < 3; j++)
    {
        (*cube)[point[0] + orientations[orientation][j][0]][point[1] + orientations[orientation][j][1]][point[2] + orientations[orientation][j][2]] = -1;
        outFile <<"{"<<point[0] + orientations[orientation][j][0]<<","<<point[1] + orientations[orientation][j][1]<<","<<point[2] + orientations[orientation][j][2]<<"} = "<<-1<<endl;
    }
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
            if((*cubeFrontier )[index] == point)
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
                if(newpoint == point)
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
        if(point[1] + 1 >= 0 && point[1] + 1 < height)
        {
            newFrontier.push_back(vector<int>{point[0],point[1] + 1,point[2]});
        }
        if(point[1] - 1 >= 0 && point[1] - 1 < height)
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
            if(newFrontier[index] == point)
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
                if(newFrontier[i] == newFrontier[j])
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
                if(newpoint == point)
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
void update(vector<vector<int>> *cubeFrontier ,vector<vector<int>> *cubeExplored, int orientation)
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
int recursiveSolve(int (* cube)[6][height][6],vector<vector<int>> *f ,vector<vector<int>> *e, int d)
{
    //int cube[6][height][6];
    int depth = d;
    int orientation = -1;
    vector<vector<int>> cubeFrontier = *f;
    vector<vector<int>> cubeExplored = *e;
    //cout << depth << endl;
    /*for(int x = 0; x < 6; x++) 
    {
        for(int y = 0; y < height; y++) 
        {
            for(int z = 0; z < 6; z++) 
            {
                cube[x][y][z] = (*c)[x][y][z];
            }
        }
    }*/
    
    outFile << "depth: " << depth << endl;
    while (orientation != -2)
    {
        vector<int> point{cubeFrontier [0][0],cubeFrontier [0][1],cubeFrontier [0][2]};
        orientation = addT(cube,point,orientation);
        outFile << "orientation: " << orientation << endl;
        //cout << "depth: " << depth << " orientation: " << orientation << endl;
        if(orientation != -2)
        {
            vector<vector<int>> oldCubeFrontier = cubeFrontier;
            vector<vector<int>> oldCubeExplored = cubeExplored;
            update(&cubeFrontier,&cubeExplored,orientation);
            for(int i = 0; i < height; i++)
            {
                string layer = "";
                bool allEmpty = true;
                for(int j = 0; j < 6; j++)
                {
                    for(int k = 0; k < 6; k++)
                    {
                        if((*cube)[k][i][j] != -1)
                        {
                            allEmpty = false;
                            if((*cube)[k][i][j] < 10)
                            {
                                layer = layer + "0" + to_string((*cube)[k][i][j]) + ", ";
                            }
                            else
                            {
                                layer = layer + to_string((*cube)[k][i][j]) + ", ";
                            }
                        }
                        else
                        {
                            layer = layer + to_string((*cube)[k][i][j]) + ", ";
                        }                        
                    }
                    layer = layer + "\n";
                }
                if(!allEmpty)
                {
                    outFile << layer << endl;
                }
            }
            if(cubeFrontier.size()>0)
            {
                int result = recursiveSolve(cube,&cubeFrontier,&cubeExplored,depth + 1); 
                outFile << "depth: " << depth << " result: " << result << endl; 
                //cout << "depth: " << depth << " result: " << result << endl;
                if(result == 0)    
                {
                    return 0;
                } 
                else
                {
                    removeT(cube,point,orientation);
                    cubeFrontier = oldCubeFrontier;
                    cubeExplored = oldCubeExplored;
                }     
            }
            else
            {
                return 0;
            }
            *f = cubeFrontier;
            *e = cubeExplored;
        }
        else
        {
            //outFile << "erase {" << cubeFrontier[0][0] << ", " << cubeFrontier[0][1] << ", " << cubeFrontier[0][2] << "}" << endl;
            //cubeFrontier.erase(cubeFrontier.begin());
            return -2;
        }        
    }  
    return -2; 
}

int main()
{    
    int cube [6][height][6];
    vector<vector<int>> cubeFrontier {vector<int> (3, 0)};
    vector<vector<int>> cubeExplored;
    outFile.open("output2.txt");

    outFile<< "sdrghfd"<< endl;
    for(int x = 0; x < 6; x++) 
    {
        for(int y = 0; y < height; y++) 
        {
            for(int z = 0; z < 6; z++) 
            {
                cube[x][y][z] = -1;
            }
        }
    }
    outFile<<endl;
    recursiveSolve(&cube,&cubeFrontier,&cubeExplored,0);
    string layer = "";
    bool allEmpty = true;
    for(int i = 0; i < height; i++)
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
        outFile << layer << endl;
    }
    outFile.close();
}