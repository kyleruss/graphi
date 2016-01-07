//================================
//  KYLE RUSSELL
//  13831056
//  ADA Assignment 2
//================================

package com.graphi.util;

import java.util.Arrays;

public class MatrixTools 
{   
    //Returns the square identity matrix with size
    public static double[][] identity(int size)
    {
        double[][] identityMatrix  =   new double[size][size];
        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(row == col) identityMatrix[row][col] = 1;
                else identityMatrix[row][col] = 0;
            }
        }
        
        return identityMatrix;
    }
    
    //Returns the sum of matrices a, b 
    public static double[][] addMatrix(double[][] a, double[][] b)
    {
        int size    =   Math.max(a.length, b.length);
        double[][] c   =   new double[size][size];
        for(int row = 0; row < size; row++)
            for(int col = 0; col < size; col++)
                c[row][col] = a[row][col] + b[row][col];
        
        return c;
    }
    
    //Returns the product of matrices a, b
    public static double[][] multiplyMatrix (double[][] a, double[][] b)
    {
        double[][] c    =   new double[a.length][b[0].length];
        double kSum     =   0.0;
        for(int row = 0; row < c.length; row++)
        {
            for(int col = 0; col < c[0].length; col++)
            {
                for(int k = 0; k < b.length; k++)
                    kSum += a[row][k]  * b[k][col];
                
                c[row][col] =   kSum;
                kSum        =   0.0;
            }
        }
        
        return c;
    }
    
    //Divides a column vector by divisor div
    public static double[][] divColVector(double[][] v, double div)
    {
        final int LEN   =   v[0].length;
        double[][] c    =   new double[1][LEN];
        
        for(int i = 0; i < LEN; i++)
            c[0][i] = v[0][i] / div;
        
        return c;
    }
    
    //Divides a row vector by divisor div
    public static double[][] divRowVector(double[][] v, double div)
    {
        final int LEN   =   v.length;
        double[][] c    =   new double[LEN][1];
        
        for(int i = 0; i < LEN; i++)
            c[i][0] = v[i][0] / div;
        
        return c;
    }
    
    //Returns true if the two column vectors a, b are comparable
    public static boolean colVectorSame(double[][] a, double[][] b)
    {
        if(a[0].length != b[0].length) return false;
        
        for(int i = 0; i < a.length; i++)
            if(a[0][i] != b[0][i]) return false;
        
        return true;
    }
    
    //Returns true if the vector is a row vector
    public static boolean isRowVector(double[][] vector)
    {
        return vector.length > vector[0].length;
    }
    
    //Returns the vectors length/dot product
    public static double getVectorLength(double[][] vector)
    {
        double total    =   0;
        if(isRowVector(vector)) vector = transpose(vector);
        
        for(int i = 0; i < vector[0].length; i++)
            total += Math.pow(vector[0][i], 2);
        
        return Math.sqrt(total);
    }
    
    //Returns a normalized vector of vector
    public static double[][] normalizeVector(double[][] vector)
    {
        double vectorLength =   getVectorLength(vector);
        if(!isRowVector(vector)) return divColVector(vector, vectorLength);
        else return divRowVector(vector, vectorLength);
    }
    
    //Returns the transpose of the matrix
    public static double[][] transpose(double[][] matrix)
    {
        double[][] transpose    =   new double[matrix[0].length][matrix.length];
        for(int row = 0; row < transpose.length; row++)
            for(int col = 0; col < transpose[0].length; col++)
                transpose[row][col] = matrix[col][row];
        
        return transpose;
    }
    
    //Returns the stationary vector after performing power iteration on adjMatrix
    public static double[][] powerIterationFull(double[][] adjMatrix)
    {
        final int LIMIT             =   20;
        final int n                 =   adjMatrix.length;
        double[][] b                =   addMatrix(adjMatrix, identity(n));
        double[][] v                =   new double[1][n];
        double[][] u;
        
        Arrays.fill(v[0], 1.0 / n);
        v   =   transpose(v);
        b   =   transpose(b);

        
        for(int i = 0; i < LIMIT; i++)
        {
            u           =   multiplyMatrix(b, v);
            double div  =   u[0][0];
            v           =   (isRowVector(u))? divRowVector(u, div) : divColVector(u, div);
        }

        return normalizeVector(v);
    }
    
    //Returns the stationary vector
    //Where each entry corresponds to the page ranks of input adjMatrix
    public static double[][] pageRankPI(double[][] adjMatrix)
    {
        final int LIMIT             =   20;
        int n                       =   adjMatrix.length;
        double[][] v                =   new double[1][n];
        adjMatrix                   =   transpose(adjMatrix);
        
        Arrays.fill(v[0], 1.0 / n);
        v = transpose(v);
        
        for(int i = 0; i < LIMIT; i++)
            v     =   multiplyMatrix(adjMatrix, v);
        
        return v;
    }
    
    //Prints out the matrix
    public static void printMatrix(double[][] matrix)
    {
        for (double[] matrix1 : matrix) 
        {
            for (int col = 0; col < matrix[0].length; col++) 
                System.out.print(matrix1[col] + " ");
            
            System.out.println();
        }
    }
}
