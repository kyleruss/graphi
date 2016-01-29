package com.graphi.plugins.st;

public class StalkingUtils 
{
    public static final int COMPUTE_P1      =   0;
    public static final int COMPUTE_P2      =   1;
    public static final int COMPUTE_P3      =   2;
    public static final int COMPUTE_P4      =   3;
    
    private static void setDPoint(Object obj, StalkingConfig config, int point, boolean stalking)
    {
        for(int time = 1; time < config.getTime(); time++)
        {
            int[][] post             =   Stalking.generatePost(time);
            int[] copyTime               =   { 1, 1, 1, 0, 0 };
            float[][] weight;
            float[][] vector;
            int[][] retrieve;

            if(!stalking)
            {
                retrieve    =   Stalking.generateRetrieve(post);   
                weight      =   Stalking.getWeight(post, retrieve);
                vector      =   Stalking.createNewMatrix1(config.getNumNodes(), config.getMatrix(), weight, config.getR());
            }

            else
            {
                retrieve    =   Stalking.generateStalkingRetrieve(config.getMatrix(), post, config.getStalkerIndex(), config.getStalkingIndex(), copyTime,retrieve);   
                weight      =   Stalking.getWeight(config.getNumNodes(), post, retrieveStalking);
                vector      =   Stalking.createNewMatrix1(config.getNumNodes(), config.getMatrix(), weight, config.getR());
            }
            
            switch(point)
            {
                case COMPUTE_P1: ((float[][][]) obj)[time - 1]  =   Stalking.getDpoint_1(config.getNumNodes(), time, vector); break;
                    
                case COMPUTE_P2: ((float[][][]) obj)[time - 1]  =   Stalking.getDpoint_2(config.getNumNodes(), time, vector); break;
                    
                case COMPUTE_P3: ((float[][]) obj)[time - 1]    =   Stalking.getDpoint_3(config.getNumNodes(), vector); break;
                    
                case COMPUTE_P4: ((float[][][]) obj)[time - 1]  =   Stalking.getDpoint_4(config.getNumNodes(), time, vector); break;
            }
        }
    }
    
    public static Object getGPoint(int point, boolean stalking, StalkingConfig config)
    {
        Object dPoint, gPoint;
        
        if(point == COMPUTE_P3)
        {
            dPoint      =   new float[config.getTime()][config.getNumNodes()];
            gPoint      =   new float[config.getTime()][config.getNumNodes()][config.getNumNodes()];
        }
            
        else
            dPoint  =   gPoint  =   new float[config.getTime()][config.getNumNodes()][config.getNumNodes()];
        
        setDPoint(dPoint, config, point, stalking);
        
        switch(point)
        {
            case COMPUTE_P1:    gPoint  =   Stalking.getG_dPoint((float[][][]) gPoint, (float[][][]) dPoint); break;
                
            case COMPUTE_P2:    gPoint  =   Stalking.getG_dPoint((float[][][]) gPoint, (float[][][]) dPoint); break;
                
            case COMPUTE_P3:    gPoint  =   Stalking.getG_2((float[][][]) gPoint, (float[][]) dPoint); break;
                
            case COMPUTE_P4:    gPoint  =   Stalking.getG_1((float[][][]) gPoint, (float[][][]) dPoint); break;
        }
        
        return gPoint;
    }
}
