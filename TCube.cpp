#include <iostream>
#include <vector>
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
const int x1 [3][3] = {{1,0,0},{2,0,0},{1,1,0}};
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
const int z4 [3][3] = {{0,0,1},{0,0,2},{0,-1,1}};

bool inRange(const int orientation[3][3], vector<int> point)
{
    bool in = true;
    for(int i = 0; i < 3; i++)
    {
        for(int p = 0; p < 3; p++)
        {
            if(orientation[i][p] + point[p] < 0 && orientation[i][p] + point[p] > 5)
            {
                in = false;
            }
        }
    }
    return in;
}

int addT(int (* cube)[6][6][6], vector<int> point)
{
    cout <<"addT "<< point[0] << " " << point[1] << " " << point[2] << endl;
    int orientation = -2;
    for(int i = 0; i < 12; i++)
    {
        cout<< "orientation " << i << endl;
        if(inRange(orientations[i],point))
        {    
            cout << "in" << endl;                   
            (*cube)[point[0]][point[1]][point[2]] = i;
            printf("{%d,%d,%d} = %d\n",point[0],point[1],point[2],i);
            for(int j = 0; j < 3; j++)
            {
                (*cube)[point[0] + orientations[i][j][0]][point[1] + orientations[i][j][1]][point[2] + orientations[i][j][2]] = i;
                printf("{%d,%d,%d} = %d\n",point[0] + orientations[i][j][0],point[1] + orientations[i][j][1],point[2] + orientations[i][j][2],i);
            }
            orientation = i;
            i = 12;
        }
    }
    return orientation;
}

void updateFroteier(vector<vector<int>> *cubeFroteier,vector<vector<int>> *cubeExplored, int orientation)
{
    //add new froteier points
    //find and remove froteier points that were not explored but still filled
    //add exlored points
    int start[3] = {(*cubeFroteier)[0][0],(*cubeFroteier)[0][1],(*cubeFroteier)[0][2]};
    
    //find all posible new froteier points
    vector<vector<int>> newFroteier;
    if(start[0] + 1 >= 0 && start[0] + 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0] + 1,start[1],start[2]});
    }
    if(start[0] - 1 >= 0 && start[0] - 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0] - 1,start[1],start[2]});
    }
    if(start[1] + 1 >= 0 && start[1] + 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0],start[1] + 1,start[2]});
    }
    if(start[1] - 1 >= 0 && start[1] - 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0],start[1] - 1,start[2]});
    }
    if(start[2] + 1 >= 0 && start[2] + 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0],start[1],start[2] + 1});
    }
    if(start[2] - 1 >= 0 && start[2] - 1 < 6)
    {
        newFroteier.push_back(vector<int>{start[0],start[1],start[2] - 1});
    }
    for(int i = 0; i < 3; i++)
    {
        if(start[0] + orientations[orientation][i][0] + 1 >= 0 && start[0] + orientations[orientation][i][0] + 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0] + 1,start[1] + orientations[orientation][i][1],start[2] + orientations[orientation][i][2]});
        }
        if(start[0] + orientations[orientation][i][0] - 1 >= 0 && start[0] + orientations[orientation][i][0] - 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0] - 1,start[1] + orientations[orientation][i][1],start[2] + orientations[orientation][i][2]});
        }
        if(start[1] + orientations[orientation][i][1] + 1 >= 0 && start[1] + orientations[orientation][i][1] + 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0],start[1] + orientations[orientation][i][1] + 1,start[2] + orientations[orientation][i][2]});
        }
        if(start[1] + orientations[orientation][i][1] - 1 >= 0 && start[1] + orientations[orientation][i][1] - 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0],start[1] + orientations[orientation][i][1] - 1,start[2] + orientations[orientation][i][2]});
        }
        if(start[2] + orientations[orientation][i][2] + 1 >= 0 && start[2] + orientations[orientation][i][2] + 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0],start[1] + orientations[orientation][i][1],start[2] + orientations[orientation][i][2] + 1});
        }
        if(start[2] + orientations[orientation][i][2] - 1 >= 0 && start[2] + orientations[orientation][i][2] - 1 < 6)
        {
            newFroteier.push_back(vector<int>{start[0] + orientations[orientation][i][0],start[1] + orientations[orientation][i][1],start[2] + orientations[orientation][i][2] - 1});
        }
    }
    cout<<"newFroteier: ";
    for(vector<int> point :newFroteier)
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;

    //get unique new elements
    vector<vector<int>> addFroteier;
    for(vector<int> newPoint : newFroteier)
    {
        for(vector<int> point : (*cubeFroteier))
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if(newPoint[k] != point[k])
                {
                    pointMatch = false;
                }
            }
            if(!pointMatch)
            {
                addFroteier.push_back(vector<int>{newPoint[0],newPoint[1],newPoint[2]});
            }
        }
    }
    /*cout<<"addFroteier: ";
    for(vector<int> point :addFroteier)
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;*/
    //filter addFroteier
    int index = 0;
    while(index < addFroteier.size())
    {
        bool in = false;
        for(int i = index+1; i < addFroteier.size();i++)
        {    
            bool pointMatch = true;        
            for(int k = 0; k < 3; k++)// match xyz
            {
                if(addFroteier[index][k] != addFroteier[i][k])
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
            addFroteier.erase(next(addFroteier.begin(),index));
        }
        else
        {
            index++;
        }
    }
    cout<<"filterdaddFroteier: ";
    for(vector<int> point :addFroteier)
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;

    //find elements not explored but filled   
    vector<int> remove{0};
    index = 0;
    while (index < cubeFroteier->size())
    {
        for(int j = 0; j < 3; j++)
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if((*cubeFroteier)[index][k] != start[k] + orientations[orientation][j][k])
                {
                    pointMatch = false;
                }
            }
            if(pointMatch)
            {
                cubeExplored->push_back(vector<int>{(*cubeFroteier)[index][0],(*cubeFroteier)[index][1],(*cubeFroteier)[index][2]});
                if(index == 0)
                {
                    cubeFroteier->erase(cubeFroteier->begin());
                }
                else
                {
                    cubeFroteier->erase(next(cubeFroteier->begin(),index));
                }                
                index --;
            }
        }
        index ++;
    }
    
    /*for(int i = 0; i < cubeFroteier->size(); i++)
    {
        for(int j = 0; j < 3; j++)
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if((*cubeFroteier)[i][k] != start[k] + orientations[orientation][j][k])
                {
                    pointMatch = false;
                }
            }
            if(pointMatch)
            {
                remove.push_back(i);
            }
        }
    }*/

    //remove elements not explored but filled
    //loop backwards so that the largest indexes are erased first to avoid problem of index shifting
    for(int i = remove.size()-1; i >= 0 ; i--)
    {
        cubeExplored->push_back(vector<int>{(*cubeFroteier)[remove[i]][0],(*cubeFroteier)[remove[i]][1],(*cubeFroteier)[remove[i]][2]});
        cubeFroteier->erase(next(cubeFroteier->begin(),i)); 
    }  

    cout<<"eraseFroteier: ";
    for(vector<int> point :(*cubeFroteier))
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }  
    cout<<endl;

    cout<<"xxExplored: ";
    for(vector<int> point :(*cubeExplored))
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;
    //add elemnts that were never in froteier but filled
    index = 0;
    int end = cubeExplored->size();
    while(index < end)
    {
        //printf("{%d,%d,%d}\n",(*cubeExplored)[index][0],(*cubeExplored)[index][1],(*cubeExplored)[index][2]);
        for(int j = 0; j < 3; j++)
        {
            bool pointMatch = true; 
            //printf("{%d,%d,%d}\n",start[0] + orientations[orientation][j][0],start[1] + orientations[orientation][j][1],start[2] + orientations[orientation][j][2]);           
            for(int k = 0; k < 3; k++)// match xyz
            { 
                if((*cubeExplored)[index][k] != start[k] + orientations[orientation][j][k])
                {
                    pointMatch = false;
                }
            }
            if(!pointMatch)
            {
                cubeExplored->push_back(vector<int>{start[0] + orientations[orientation][j][0],start[1] + orientations[orientation][j][1],start[2] + orientations[orientation][j][2]});
            }
        }
        index++;
    }
    cout<<"Explored: ";
    for(vector<int> point :(*cubeExplored))
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;

    //add new froteier that is not in explored
    for(vector<int> newPoint : addFroteier)
    {
        bool notin = true;
        for(vector<int> point : (*cubeExplored))
        {
            bool pointMatch = true;
            for(int k = 0; k < 3; k++)// match xyz
            {
                if(newPoint[k] != point[k])
                {
                    pointMatch = false;
                }
            }
            if(pointMatch)
            {
                notin = false;
            }
        }
        if(notin)
        {
            cubeFroteier->push_back(vector<int>{newPoint[0],newPoint[1],newPoint[2]});
        }
    }   

    cout<<"Froteier: ";
    for(vector<int> point :(*cubeFroteier))
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;    
}

int main()
{    
    int cube [6][6][6];
    vector<vector<int>> cubeFroteier{vector<int> (3, 0)};
    vector<vector<int>> cubeExplored;
    
    cout<< "sdrghfd"<< endl;
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
    cout<<"Froteier: ";
    for(vector<int> point :cubeFroteier)
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;
    cout<<"Explored: ";
    for(vector<int> point :cubeExplored)
    {
        printf("{%d,%d,%d}",point[0],point[1],point[2]);
    }
    cout<<endl;
    int a = 3;
    while(a > 0)
    {
        int orientation = addT(&cube, cubeFroteier[0]);
        updateFroteier(&cubeFroteier,&cubeExplored,orientation);
        a--;
   }
}