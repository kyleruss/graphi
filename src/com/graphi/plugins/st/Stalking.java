package com.graphi.plugins.st;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Stalking 
{
    private static final Random RANDOM;
    private static final StalkingConfig CONFIG;
    
    static
    {
        RANDOM  =   new Random();
        CONFIG  =   getConfig();
    }
    
    public static void main(String[] args)
    {
        StalkingConfig config   =   getConfig();
        PrintStream out         =   System.out;
        
        float[][][] gPoint      =   (float[][][]) StalkingData.getGPoint(StalkingData.COMPUTE_P1, false, config);
        float[][][] gPoint_2    =   (float[][][]) StalkingData.getGPoint(StalkingData.COMPUTE_P2, false, config);
        float[][][] gPoint_3    =   (float[][][]) StalkingData.getGPoint(StalkingData.COMPUTE_P3, false, config);

        writeG("cxy.txt", gPoint);
        writeG("cxycy.txt", gPoint_2);
        writeG("average.txt", gPoint_3);
        
        System.setOut(out);
        System.out.print("FINISHED!");
    }
    
    private static StalkingConfig getConfig()
    {
        StalkingConfig config   =   new StalkingConfig();
        
        System.out.println("Do you want to create from txt file/ 1--yes,2--Random");
        Scanner input = new Scanner(System.in);

        config.setReadIO(input.nextInt() == 1);

        if (config.isReadIO())
        {
             config.setMatrix(readTxtFile(Stalking.class.getClassLoader()
                     .getResource(".")
                     .getPath()
                     + "../mMatrix.txt"));

             config.setNumNodes(config.getMatrix().length);
        } 

        else 
        {
            do 
            {
                System.out.println("The RANDOM graph model: 1 (G(n,p)), 2 (Power Law)");
                input = new Scanner(System.in);
                config.setModel(input.nextInt());
            } 

            while (config.getModel() == 0);

            System.out.println("The number of nodes:");
            input = new Scanner(System.in);
            config.setNumNodes(input.nextInt());

            System.out.println("The total number of messages:");
            input = new Scanner(System.in);
            config.setNumMessages(input.nextInt());

            System.out.println("The max number of messages of a person:");
            input = new Scanner(System.in);
            config.setMessageLimit(input.nextInt());


            if (config.getModel() == 1) 
            {
                System.out.println("The edge probability p = ");
                input = new Scanner(System.in);
                config.setEdgeProb(input.nextDouble());
                config.setMatrix(generationGraphp());
            } 

            else 
            {
                System.out.println("The fixed number of edges m = ");
                input = new Scanner(System.in);
                config.setNumEdges(input.nextInt());
                config.setMatrix(generationGraphPowerlaw());
            }

            System.out.println("Graph generated");
        }

        // Stalking
        System.out.println("who are you(only scanner the number):");
        config.setStalkerIndex(input.nextInt() - 1);
        System.out.println("who do you want to stalk(only input the number):");
        config.setStalkingIndex(input.nextInt() - 1);


        String[] POINTS = new String[config.getNumNodes()];
        for (int i = 0; i < config.getNumNodes(); i++)
                POINTS[i] = "P" + (i + 1);


        int[][] dist = new int[config.getNumNodes()][config.getNumNodes()];
        for (int i = 0; i < config.getNumNodes(); i++) 
                dist[i] = Dijsktra(config.getMatrix(), i);


        try 
        {
            System.setOut(new PrintStream(new FileOutputStream(Stalking.class
                    .getClassLoader().getResource(".").getPath()
                    + "log.txt")));
        } 

        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 

        System.out.println("Start!");
        System.out.println("Generate " + config.getNumNodes() + " nodes");
        printDegree();

        System.out.print("nodes[" + config.getNumNodes() + "]={");
        String[] points = new String[config.getNumNodes()];
        for (int pn = 0; pn < config.getNumNodes(); pn++) 
        {
            points[pn] = POINTS[pn];
            if (pn < config.getNumNodes() - 1) 
                System.out.print(points[pn] + " , ");

             else 
                System.out.print(points[pn]);
        }

        System.out.println(" }");
        printMatrix("");
        
        return config;
    }

    //calculate the stalking index according to the time
    protected static float[][][] getG_1(float[][][] gPoint, float[][][] dPoint) 
    {
        int num = gPoint[0].length;

        for (int time = 1; time <= CONFIG.getTime(); time++)
        {
            System.out.println();
            System.out.println("Time " + time);
            
            for (int i = 0; i < num; i++)
            {
                for (int j = 0; j < num; j++)
                {
                    float sumTmp = 0;
                    for (int k = time; k > 0; k--) 
                            sumTmp += (float) dPoint[time - k][i][j] / k;
                    
                    gPoint[time - 1][i][j] = sumTmp;
                }
            }
        }
        
        return gPoint;
    }

    protected static float[][][] getG_2(float[][][] gPoint, float dPoint[][]) 
    {
        int num = gPoint[0].length;
        for (int time = 1; time <= CONFIG.getTime(); time++) 
        {
                System.out.println();
                for (int i = 0; i < num; i++) 
                    gPoint[time - 1][0][i] = dPoint[time - 1][i];
        }

        return gPoint;
    }

    protected static float[][][] getG_dPoint(float[][][] gPoint, float[][][] dPoint)
    {
        gPoint = dPoint;
        return gPoint;
    }

    //calculate the c_xy
    protected static float[][] getDpoint_1(float[][] vectorArray) 
    {
        int num     =   CONFIG.getNumNodes();
        float[][] d = new float[num][num];
        for (int i = 0; i < num; i++)
        {
            for (int j = 0; j < num; j++) 
                d[i][j] = vectorArray[i][j];
        }

        return d;
    }

    //calculate the c_xy/average c_y
    protected static float[][] getDpoint_2(float[][] vectorArray) 
    {
        int num     =   CONFIG.getNumNodes();
        float[][] d =   new float[num][num];
        float[] sum =   new float[num];
        
        for (int i = 0; i < num; i++)
        {
            for (int j = 0; j < num; j++) 
                    sum[i] += vectorArray[j][i];

            sum[i] = sum[i] / num;
        }
        
        for (int i = 0; i < num; i++)
        {
            for (int j = 0; j < num; j++) 
                    d[i][j] = vectorArray[i][j] / sum[j];
        }

        return d;
    }

    //calculate the average c_y
    protected static float[] getDpoint_3(float[][] vectorArray) 
    {
        int num     =   CONFIG.getNumNodes();
        float[] sum =   new float[num];
        for (int i = 0; i < num; i++)
        {
                for (int j = 0; j < num; j++) 
                        sum[i] += vectorArray[j][i];

                sum[i] = sum[i] / num;
        }

        return sum;
    }

    //calculate the c_xy/average c_y -c_yx/ average c_x
    protected static float[][] getDpoint_4(float[][] vectorArray) 
    {
        int num     =   CONFIG.getNumNodes();
        float[][] d =   new float[num][num];
        float[] sum =   new float[num];

        for (int i = 0; i < num; i++) 
        {
            for (int j = 0; j < num; j++) 
                    sum[i] += vectorArray[j][i];

            sum[i] = sum[i] / num;
        }

        for (int i = 0; i < num; i++)
        {
            for (int j = 0; j < num; j++) 
                    d[i][j] = vectorArray[i][j] / sum[j] - vectorArray[j][i]/ sum[i];
        }

        return d;
    }

    protected static float[][] createNewMatrix1(float[][] weight)
    {
        int num             =   CONFIG.getNumNodes();
        float r             =   CONFIG.getR();
        float[][] vectorArray = new float[num][num];
        int[] degree;
        float[] distribution;
        float[] totalWeight1;
        float[] totalWeight2;
        float[][] subGraph;

        for (int k = 0; k < num; k++) 
        {
            subGraph = new float[num][num];
            totalWeight1 = new float[num];
            totalWeight2 = new float[num];
            distribution = new float[num];
            degree = new int[num];

            for (int i = 0; i < num; i++) 
            {
                for (int j = 0; j < num; j++) 
                {
                    if (CONFIG.getMatrix()[i][j] == 1) 
                    {
                        degree[i]++;
                        totalWeight1[i] = totalWeight1[i] + weight[k][j] + 1;
                    }

                    if (CONFIG.getMatrix()[i][j] == 0) 
                            totalWeight2[i] = totalWeight2[i] + weight[k][j];
                }

                distribution[i] = degree[i] / (degree[i] + r * (num - degree[i]));
            }


            for (int i = 0; i < num; i++) 
            {
                for (int j = 0; j < num; j++)
                {
                    if (CONFIG.getMatrix()[i][j] == 0 && weight[k][j] == 0) 
                            subGraph[i][j] = 0;

                    else if (CONFIG.getMatrix()[i][j] == 0) 
                            subGraph[i][j] = (1 - distribution[i]) * weight[k][j] / totalWeight2[i];

                    else if (CONFIG.getMatrix()[i][j] == 1)
                            subGraph[i][j] = distribution[i] * (weight[k][j] + 1) / totalWeight1[i];
                }
            }
            
            if (k == 0)
             printMatrix("Attention matrix ");

            float[][] transSubGraph = transposedMatrix(subGraph);
            float[] vec = new float[num];
            
            for (int i = 0; i < num; i++)
            {
                if (i == k) 
                    vec[i] = 1;
                else 
                    vec[i] = 0;
            }
            
            for (int i = 0; i < 10; i++) 
                    vec = matrixXvector(transSubGraph, vec);
            
            if (k == 0) printVector("Attention from 1 ", vec);
            
            vectorArray[k] = vec;
        }
        
        System.out.println();
        return vectorArray;
    }

    protected static float[][] getWeight(int[][] post, int[][] retrieve)
    {
        int num =   CONFIG.getNumNodes();
        float[][] weight = new float[num][num];
        for (int i = 0; i < num; i++)
        {
            for (int j = 0; j < num; j++)
            {
                int denominator = 0;// p+r
                int numerator = 0;
                Set<Integer> weightSet = new HashSet<Integer>();
                
                if (i == j) 
                    weight[i][j] = 0;
                
                else 
                {
                    for (Integer tmp : retrieve[i]) 
                    {
                        if (tmp != 0) 
                            weightSet.add(tmp);
                    }
                    
                    for (Integer tmp : post[j]) 
                    {
                        if (weightSet.contains(tmp)) 
                                numerator++;
                        else 
                        {
                            if (tmp != 0) 
                                weightSet.add(tmp);
                        }
                    }
                    
                    denominator = weightSet.size();
                    
                    if(i==0 && j==1)
                            System.out.println("\nP1 -> P2 UnionSize = "+denominator + " IntersectionSize = "+numerator);
                    

                    if(i==0 && j==4)
                            System.out.println("P1 -> P5 UnionSize = "+denominator + " IntersectionSize = "+numerator);

                    weight[i][j] = (float) numerator / denominator;
                }
            }
        }

        return weight;
    }

    protected static Integer[][] generateRetrieve(Integer[][] post) 
    {
        System.out.println(" Retrieve :");
        int num                 =   CONFIG.getNumNodes();
        Integer retrieve[][]    =   new Integer[num][CONFIG.getMessageLimit()];

        for (int i = 0; i < num; i++) 
        {
            System.out.print("Random Retrieve of P" + (i + 1) + " : ");
            List<Integer> retrieveMessageList = new ArrayList<Integer>();
            
            for (int x = 1; x <= CONFIG.getNumMessages(); x++) 
                    retrieveMessageList.add(x);
            

            for (int j = 0; j < CONFIG.getMessageLimit(); j++) 
            {
                int index = (int) (Math.random() * retrieveMessageList.size());
                Integer tmp = retrieveMessageList.get(index);
                if (!Arrays.asList(post[i]).contains(tmp)) 
                {
                        retrieve[i][j] = tmp;
                        System.out.print(retrieve[i][j] + " ");
                } 
                
                else j--;
                
                retrieveMessageList.remove(tmp);
            }
            
            System.out.println();
        }
        
        return retrieve;
    }

    protected static Integer[][] generateStalkingRetrieve(Integer[][] post, int[] copyTime, Integer [][] retrieveRandom) 
    {
        Integer[][] retrieve    =   new Integer [CONFIG.getMatrix().length][CONFIG.getMessageLimit()];
        int stalkedIndex        =   CONFIG.getStalkingIndex();
        int stalkingIndex       =   CONFIG.getStalkerIndex();
        
        for(int i =0; i < CONFIG.getMatrix().length; i++)
        {
            for (int j=0;j < CONFIG.getMessageLimit();j++)
                retrieve[i][j]= new Integer(retrieveRandom[i][j]);
            
        }
        
        int[][] dist = new int[CONFIG.getMatrix().length][CONFIG.getMatrix().length];
        for (int i = 0; i < CONFIG.getMatrix().length; i++) 
                dist[i] = Dijsktra(CONFIG.getMatrix(), i);
        
        System.out.println();
        System.out.println(" Stalking Retrieve :");

        List<List<Integer>> retrieveList = new ArrayList<>();
        for (int i = 0; i <= CONFIG.getDistNum(); i++) 
        {
                List<Integer> retrieveListItem = new ArrayList<>();
                retrieveList.add(retrieveListItem);
        }
        
        int lengthSum = 0;
        List<Integer> item;
        
        for (int i = 0; i < CONFIG.getMatrix().length; i++) 
        {
            if (dist[i][stalkedIndex] <= CONFIG.getDistNum() )
            {
                item = retrieveList.get(dist[i][stalkedIndex]);
                for (Integer tmp : post[i]) 
                {
                    if (!item.contains(tmp))
                    {
                        item.add(tmp);
                        lengthSum++;
                    }
                }
            }
        }
        
        int t = 0;
        for (List<Integer> retrieveListItem : retrieveList)
        {
            while (copyTime[t] > 0)
            {
                retrieveListItem.addAll(retrieveListItem);
                copyTime[t]--;
                t++;
            }
        }

        for (int i = 0; i < CONFIG.getMessageLimit(); i++) 
        {
            int index = (int) (Math.random() * lengthSum);
            int[] retrieveLength = getRetrieveLength(retrieveList);
            int j = 0;
            int length = 0;

            for (int lengthTmp : retrieveLength)
            {
                length += lengthTmp;
                if (index < length) break;

                j++;
            }

            List<Integer> retrieveItem = retrieveList.get(j);
            Integer retrieveItemTmp = null;

            if (j == 0 ) 
                retrieveItemTmp = retrieveItem.get(index);

            else 
                retrieveItemTmp = retrieveItem.get(index - (length - retrieveList.get(j).size()));

            System.out.print("Stalker retrieve: ");
            retrieve[stalkingIndex][i] = retrieveItemTmp;
            System.out.print(retrieve[stalkingIndex][i] + " ");
            retrieveItem.remove(retrieveItemTmp);

            while (j >= 0)
            {
                List<Integer> itemTmp = retrieveList.get(j);
                retrieveList.add(itemTmp);
                j--;
            }
        }

        return retrieve;
    }

    protected static int[] getRetrieveLength(List<List<Integer>> retrieveList) 
    {
        int[] retrieveLength = new int[retrieveList.size()];
        int i = 0;
        for (List<Integer> setTmp : retrieveList) 
        {
            retrieveLength[i] = setTmp.size();
            i++;
        }
        
        return retrieveLength;
    }

    protected static Integer[][] generationRetrievel(int num, Integer[][] post)
    {
        boolean flag;
        flag = false;
        Integer retrieve[][] = new Integer[num][CONFIG.getMessageLimit()];

        for (int i = 0; i < num; i++)
        {
            System.out.print("P" + (i + 1) + " : ");
            int limit = Math.abs(RANDOM.nextInt()) % CONFIG.getMessageLimit() + 1;

            for (int j = 0; j < CONFIG.getMessageLimit(); j++)
            {
                flag = false;
                if (j < limit)
                {
                    int msgId = Math.abs(RANDOM.nextInt()) % CONFIG.getNumMessages() + 1;

                    for (int k = 0; k < CONFIG.getMessageLimit(); k++)
                    {
                        if (post[i][k] == msgId) 
                                flag = true;
                    }

                    for (int q = 0; q < j; q++) 
                    {
                        if (retrieve[i][q] == msgId) 
                        {
                                flag = true;
                        }
                    }

                    if (flag) j--;

                    else 
                    {
                        if (isExistInPost(msgId, post)) 
                        {
                                retrieve[i][j] = msgId;
                                System.out.print(retrieve[i][j] + " ");
                        } 

                        else j--;
                    }
                } 

                else retrieve[i][j] = 0;
            }

            System.out.println();
        }

        return retrieve;
    }

    protected static Integer[][] generationStalkingRetrievel(Integer[][] post, double[] probability) 
    {
        Integer[][] retrieve = generationRetrievel(CONFIG.getMatrix().length, post);
        Integer[][] retrieveTmp;
        int stalkedIndex    =   CONFIG.getStalkingIndex();
        int stalkingIndex   =   CONFIG.getStalkerIndex();
        
        int[] dist = Dijsktra(CONFIG.getMatrix(), stalkedIndex);
        Set<Integer> retrieveSet = new HashSet<Integer>();
        
        for (int r = 0; r < post[stalkedIndex].length; r++)
        {
            double pro = Math.random();
            if (pro < probability[0]) 
                    retrieveSet.add(post[stalkedIndex][r]);
        }
        
        for (int r = 0; r < dist.length; r++) 
        {
            int distNum = CONFIG.getDistNum(); // retrieve///
            if (dist[r] <= distNum && dist[r] > 0) 
            {
                for (int p = 0; p < post[r].length; p++) 
                {
                    if (post[r][p].equals(0)) break;

                    double pro = Math.random();
                    if (pro < probability[dist[r]]) 
                        retrieveSet.add(post[r][p]);
                }
            }
            
            for (int i = 0; i < post.length; i++)
            {
                for (int j = 0; j < post[i].length; j++) 
                {
                    double pro = Math.random();
                    if (pro < probability[CONFIG.getDistNum() + 1]) 
                    {
                        pro = Math.random();
                        if (pro < probability[CONFIG.getDistNum() + 1]) 
                                retrieveSet.add(post[i][j]);
                    }
                }
            }
        }
        
        retrieveSet.remove(0);
        
        if (retrieve[0].length < retrieveSet.size()) 
        {
            retrieveTmp = new Integer[CONFIG.getMatrix().length][retrieveSet.size()];
            for (int i = 0; i < CONFIG.getMatrix().length; i++) 
            {
                for (int j = 0; j < retrieveSet.size(); j++)
                {
                    if (i < CONFIG.getMessageLimit() && j < CONFIG.getMessageLimit()) 
                        retrieveTmp[i][j] = retrieve[i][j];
                    else 
                        retrieveTmp[i][j] = 0;
                }
            }
        } 
        
        else retrieveTmp = retrieve;
        
        int index = 0;
        for (Integer tmp : retrieveSet) 
        {
                retrieveTmp[stalkingIndex][index] = tmp;
                index++;
        }
        
        return retrieveTmp;
    }

    protected static Integer[][] generationStalkingRetrievel2(Integer[][] post, double[] probability) 
    {
        int[][] mMatrix     =   CONFIG.getMatrix();
        int stalkedIndex    =   CONFIG.getStalkingIndex();
        int stalkingIndex   =   CONFIG.getStalkerIndex();
        
        Integer[][] retrieve = generationRetrievel(mMatrix.length, post);
        Integer[][] retrieveTmp;
        int[][] dist = new int[mMatrix.length][mMatrix.length];
        
        for (int i = 0; i < mMatrix.length; i++) 
            dist[i] = Dijsktra(mMatrix, i);
        
        System.out.println();
        System.out.println(" Retrieve :");
        Set<Integer> retrieveSet = new HashSet<Integer>();
        
        for (int r = 0; r < post[stalkedIndex].length; r++) 
        {
            double pro = Math.random();
            if (pro < probability[0]) 
                    retrieveSet.add(post[stalkedIndex][r]);
        }

        for (int r = 0; r < mMatrix.length; r++) 
        {
            if (dist[r][stalkedIndex] <= CONFIG.getDistNum() && dist[r][stalkedIndex] > 0) {
                for (int p = 0; p < post[r].length; p++) 
                {
                    if (post[r][p].equals(0)) break;
                    
                    double pro = Math.random();
                    if (pro < probability[dist[r][stalkedIndex]]) 
                            retrieveSet.add(post[r][p]);
                }
            }
            
            for (int i = 0; i < post.length; i++)
            {
                for (int j = 0; j < post[i].length; j++)
                {
                    double pro = Math.random();
                    if (pro < probability[CONFIG.getDistNum() + 1])
                    {
                        pro = Math.random();
                        if (pro < probability[CONFIG.getDistNum() + 1]) 
                            retrieveSet.add(post[i][j]);
                    }
                }
            }
        }
        
        retrieveSet.remove(0);

        if (retrieve[0].length < retrieveSet.size()) 
        {
            retrieveTmp = new Integer[mMatrix.length][retrieveSet.size()];
            for (int i = 0; i < mMatrix.length; i++) {
                for (int j = 0; j < retrieveSet.size(); j++) 
                {
                    if (j < CONFIG.getMessageLimit()) 
                        retrieveTmp[i][j] = retrieve[i][j];
                    
                    else 
                        retrieveTmp[i][j] = 0;
                }
            }
        } 
        
        else retrieveTmp = retrieve;
        
        int index = 0;
        for (Integer tmp : retrieveSet) 
        {
            retrieveTmp[stalkingIndex][index] = tmp;
            index++;
        }
        
        for (int i = 0; i < retrieveTmp.length; i++)
        {
            System.out.print("P" + (i + 1) + " ");

            for (int j = 0; j < retrieveTmp[i].length; j++) 
                System.out.print(retrieveTmp[i][j] + " ");
            
            System.out.println();
        }
        
        return retrieveTmp;
    }

    protected static Integer[][] generatePost(int time)
    {
        int num     =   CONFIG.getNumNodes();
        System.out.println(" POST :" + time);
        Integer post[][] = new Integer[num][CONFIG.getMessageLimit()];


        for (int i = 0; i < num; i++) 
        {
            System.out.print("Random Post of P" + (i + 1) + " : ");
            List<Integer> postMessageList = new ArrayList<>();
            
            for (int x = 1; x <= CONFIG.getNumMessages(); x++) 
                postMessageList.add(x);
            
            for (int j = 0; j < CONFIG.getMessageLimit(); j++) 
            {
                int index = (int) (Math.random() * postMessageList.size());
                Integer tmp = postMessageList.get(index);
                post[i][j] = tmp;
                System.out.print(post[i][j] + " ");
                postMessageList.remove(tmp);
            }
            
            System.out.println();
        }

        System.out.println();
        return post;
    }

    protected static Integer[][] generationPost(String[] points, int time)
    {
        int num =   CONFIG.getNumNodes();
        boolean flag;
        System.out.println(" POST :" + time);
        flag = false;
        Integer post[][] = new Integer[num][CONFIG.getMessageLimit()];
        
        for (int i = 0; i < num; i++) 
        {
            System.out.print(points[i] + " : ");
            int limit = Math.abs(RANDOM.nextInt()) % CONFIG.getMessageLimit() + 1;

            for (int j = 0; j < CONFIG.getMessageLimit(); j++) 
            {
                flag = false;
                if (j < limit)
                {
                    int msgId = Math.abs(RANDOM.nextInt()) % CONFIG.getNumMessages() + 1;
                    
                    for (int k = 0; k < j; k++) 
                    {
                        if (post[i][k] == msgId) 
                                flag = true;
                    }
                    
                    if (flag) j--;
                    
                    else 
                    {
                        post[i][j] = msgId;
                        System.out.print(post[i][j] + " ");
                    }
                } 
                
                else post[i][j] = 0;
                
            }
            System.out.println();
        }
        return post;
    }

    protected static int[][] generationGraphPowerlaw() 
    {
        int num         =   CONFIG.getNumNodes();
        int edgesToAdd  =   CONFIG.getNumEdges();
        int[][] mMatrix;
        
        System.out.println("Generate Directed Graph using the Barabasi-Albert model where edgesToAdd= " + edgesToAdd);

        // Create N nodes
        // Keep track of the degree of each node
        int indegrees[] = new int[num];
        int outdegrees[] = new int[num];

        // For each node
        // Save node in array
        List<Integer> nodesList = new ArrayList<Integer>();
        for (int x = 0; x < num; x++) 
                nodesList.add(x);

        // set the number of edges to zero
        int numEdges = 0;

        // Set up the initial complete seed network
        mMatrix = new int[num][num];

        for (int i = 0; i < num - 1; i++)
        {
            mMatrix[i][i + 1] = 1;
            outdegrees[i]++;
            indegrees[i + 1]++;
        }

        mMatrix[num - 1][0] = 1;
        outdegrees[num - 1]++;
        indegrees[0]++;
        numEdges = num;

        int added = 0;
        double degreeIgnore = 0;

        int index = 0;
        int j = 0;
        ArrayList<Integer> nodeList = new ArrayList<Integer>();

        // Add each node one at a time
        for (int i = 0; i < num; i++) 
        {
            added = 0;
            degreeIgnore = indegrees[i];

            // Add the appropriate number of edges
            for (int m = 0; m < edgesToAdd; m++) 
            {
                nodeList = new ArrayList<Integer>();

                // adding all elements to nodeList
                for (int k = 0; k < num; k++) 
                        nodeList.add(k);

                nodeList.remove(i);

                // keep a running talley of the probability
                double prob = 0;

                // Choose a RANDOM number
                double randNum = RANDOM.nextDouble();

                // Try to add this node to every existing node
                while (!nodeList.isEmpty()) 
                {
                    index = (int) (Math.random() * nodeList.size());
                    j = nodeList.get(index);
                    nodeList.remove(index);

                    // Check for an existing connection between these two nodes
                    if (mMatrix[i][j] != 1)
                    {
                        // Increment the talley by the jth node's probability
                        prob = (double) Math.pow(((double) indegrees[j]) / ((double) (numEdges) - degreeIgnore), 0.25d);

                        // If this pushes us past the the probability
                        if (randNum <= prob) 
                        {
                            // Create and edge between node i and node j
                            mMatrix[i][j] = 1;
                            
                            degreeIgnore += indegrees[j];

                            // increment the number of edges
                            added++;
                            // increment the degrees of each node
                            indegrees[j]++;
                            outdegrees[i]++;
                            
                            // Stop iterating for this probability, once we have
                            // found a single edge
                            break;
                        }
                    }
                }
            }
                
            numEdges += added;
        }

        // return the resulting network
        return mMatrix;
    }

    // print out the indegrees of all nodes
    protected static void printDegree()
    {
        int num =   CONFIG.getNumNodes();
        int[] degrees = new int[num];
        for (int i = 0; i < num; i++) 
        {
            for (int j = 0; j < num; j++)
            {
                if (CONFIG.getMatrix()[i][j] == 1)
                        degrees[j]++;
            }
        }
        
        for (int i = 0; i < num; i++) 
                System.out.print(degrees[i] + " ");
        
        System.out.print("\n");
    }

    protected static int[][] initialGraph()
    {
        int n   =   CONFIG.getNumNodes();
        int m   =   CONFIG.getNumEdges();
        int[][] mMatrix = new int[n][n];

        for (int x = 0; x < m; x++) 
        {
            for (int y = 0; y < m; y++) 
            {
                if (x != y) 
                    mMatrix[x][y] = 1;
                else
                    mMatrix[x][y] = 0;
            }
        }
        
        return mMatrix;
    }

    
    protected static int[][] generationGraphp() 
    {
        double p    =   CONFIG.getEdgeProb();
        int num     =   CONFIG.getNumNodes();
        int[][] mMatrix;
        
        System.out.println("Generate Directed Graph using the G(n,p) model where p= " + p);

        mMatrix = new int[num][num];
        for (int x = 0; x < num; x++) 
        {
            for (int y = 0; y < num; y++) 
            {
                double r = Math.random();
                if (r < p && x != y)
                    mMatrix[x][y] = 1;
                else
                    mMatrix[x][y] = 0;

                System.out.print(mMatrix[x][y] + " ");
            }

            System.out.println();
        }

        return mMatrix;
    }

    protected static void printMatrix(String name) 
    {
        System.out.println();
        System.out.println(name + " : ");
        
        int[][] matrix  =   CONFIG.getMatrix();
        for (int i = 0; i < matrix.length; i++) 
        {
            for (int j = 0; j < matrix[i].length; j++)
                    System.out.print(matrix[i][j] + " ");
            
            System.out.println();
        }
    }

    protected static void printVector(String name, float[] vector) 
    {
        System.out.println();
        System.out.println(name + " : ");
        
        for (int i = 0; i < vector.length; i++) 
                System.out.print(vector[i] + " ");
    }

    protected static float[][] transposedMatrix(float[][] matrix) 
    {
        float[][] transposedMatrix = new float[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix[0].length; i++) 
        {
            for (int j = 0; j < matrix.length; j++) 
                transposedMatrix[i][j] = matrix[j][i];
        }
        
        return transposedMatrix;
    }

    protected static float[] matrixXvector(float[][] matrix, float[] vector) 
    {
        float[] c_point = new float[vector.length];
        for (int i = 0; i < vector.length; i++) 
        {
            for (int j = 0; j < vector.length; j++) 
                c_point[i] += matrix[i][j] * vector[j];
        }
        
        return c_point;
    }

    protected static void writeG(String fileName, float[][][] g)
    {
        File file = new File(Stalking.class.getClassLoader().getResource(".").getPath() + "../" + fileName);
        System.out.println("Save g.txt to:" + file.getPath());

        try(PrintStream p = new PrintStream(new FileOutputStream(file, false)))
        {
            if (!file.exists()) 
                file.createNewFile();
            
            for (int time = 0; time < g.length; time++)
            {
                p.println("line:");
                for (int i = 0; i < g[time].length; i++)
                {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < g[time][i].length; j++) 
                    {
                        sb.append(g[time][i][j]);
                        if (j != g[time][i].length - 1) 
                            sb.append(" ");
                    }
                    
                    p.println(sb.toString());
                }
            }
        } 
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected static int[][] readTxtFile(String filePath)
    {
        List<String> tmpList = new ArrayList<>();
        String encoding = "UTF-8";
        File file = new File(filePath);
        
        if(file.isFile() && file.exists())
        {
            try(BufferedReader bufferedReader   =   new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding)))
            {
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null) 
                        tmpList.add(lineTxt);
            } 

            catch (Exception e) 
            {
                System.out.println("////////");
                e.printStackTrace();
            }
        }
        
        else
            System.out.println("////////");
        
        int[][] mMatrix = new int[tmpList.size()][tmpList.size()];
        for (int i = 0; i < tmpList.size(); i++) 
        {
            for (int j = 0; j < tmpList.size(); j++) 
                    mMatrix[i][j] = Integer.parseInt(tmpList.get(i).split(" ")[j]);
        }
        
        return mMatrix;
    }

    protected static boolean isExistInPost(Integer retrieve, Integer[][] post)
    {
        return true;
    }

    protected static int[] Dijsktra(int[][] mMatrix, int start) 
    {
        int INF = Integer.MAX_VALUE;
        int[][] weight = new int[mMatrix.length][mMatrix.length];
        
        for (int i = 0; i < mMatrix.length; i++)
        {
            for (int j = 0; j < mMatrix.length; j++) 
            {
                    if (mMatrix[i][j] == 0 && i != j) 
                        weight[i][j] = 2000;
                    
                    else 
                        weight[i][j] = mMatrix[i][j];
            }
        }
        
        int n = weight.length; 
        int[] shortPath = new int[n]; 
        String[] path = new String[n]; 
        
        for (int i = 0; i < n; i++) 
                path[i] = "P" + (start + 1) + "-->P" + (i + 1);
        
        int[] visited = new int[n]; 
        
        shortPath[start] = 0;
        visited[start] = 1;
        for (int count = 1; count <= n - 1; count++) 
        { 
            int k = -1; 
            int dmin = INF;
            for (int i = 0; i < n; i++)
            {
                if (visited[i] == 0 && weight[start][i] < dmin) 
                {
                    dmin = weight[start][i];
                    k = i;
                }
            }
            
            shortPath[k] = dmin;
            visited[k] = 1;
            for (int i = 0; i < n; i++) 
            {
                if (visited[i] == 0
                                && weight[start][k] + weight[k][i] < weight[start][i]) {
                        weight[start][i] = weight[start][k] + weight[k][i];
                        path[i] = path[k] + "-->P" + (i + 1);
                }
            }
        }
        
        return shortPath;
    }
}
