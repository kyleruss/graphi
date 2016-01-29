package com.graphi.plugins.st;


public class StalkingConfig 
{
    private int model;
    private int[][] matrix;
    private int numNodes;
    private int numMessages;
    private int messageLimit;
    private double edgeProb;
    private int numEdges;
    private int stalkerIndex;
    private int stalkingIndex;
    private boolean readIO;
    private int distNum;
    private int time;
    private float r;

    public StalkingConfig()
    {
        distNum =   3;
        time    =   50;
        r       =   0.6f;
    }
    
    public boolean isReadIO()
    {
        return readIO;
    }

    public void setReadIO(boolean readIO) 
    {
        this.readIO = readIO;
    }

    public int getModel() 
    {
        return model;
    }

    public void setModel(int model)
    {
        this.model = model;
    }

    public int[][] getMatrix()
    {
        return matrix;
    }

    public void setMatrix(int[][] matrix) 
    {
        this.matrix = matrix;
    }

    public int getNumNodes() 
    {
        return numNodes;
    }

    public void setNumNodes(int numNodes) 
    {
        this.numNodes = numNodes;
    }

    public int getNumMessages()
    {
        return numMessages;
    }

    public void setNumMessages(int numMessages)
    {
        this.numMessages = numMessages;
    }

    public int getMessageLimit()
    {
        return messageLimit;
    }

    public void setMessageLimit(int messageLimit) 
    {
        this.messageLimit = messageLimit;
    }

    public double getEdgeProb() 
    {
        return edgeProb;
    }

    public void setEdgeProb(double edgeProb)
    {
        this.edgeProb = edgeProb;
    }

    public int getNumEdges() 
    {
        return numEdges;
    }

    public void setNumEdges(int numEdges) 
    {
        this.numEdges = numEdges;
    }

    public int getStalkerIndex() 
    {
        return stalkerIndex;
    }

    public void setStalkerIndex(int stalkerIndex) 
    {
        this.stalkerIndex = stalkerIndex;
    }

    public int getStalkingIndex() 
    {
        return stalkingIndex;
    }

    public void setStalkingIndex(int stalkingIndex)
    {
        this.stalkingIndex = stalkingIndex;
    }
    
    public void setR(float r)
    {
        this.r  =   r;
    }
    
    public float getR()
    {
        return r;
    }
    
    public int getDistNum()
    {
        return distNum;
    }
    
    public void setDistNum(int distNum)
    {
        this.distNum    =   distNum;
    }
    
    public int getTime()
    {
        return time;
    }
    
    public void setTime(int time)
    {
        this.time   =   time;
    }
}
