//================================
//  KYLE RUSSELL
//  13831056
//  ADA Assignment 2
//================================

package com.graphi.util;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MatrixTools 
{   
    //Returns the square identity matrix with size
    public static SparseDoubleMatrix2D identity(int size)
    {
        SparseDoubleMatrix2D identityMatrix  =   new SparseDoubleMatrix2D(size, size);
        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(row == col) identityMatrix.setQuick(row, col, 1);
                else identityMatrix.setQuick(row, col, 0);
            }
        }
        
        return identityMatrix;
    }
    
    //Returns the sum of matrices a, b 
    public static SparseDoubleMatrix2D addMatrix(SparseDoubleMatrix2D a, SparseDoubleMatrix2D b)
    {
        int size                    =   Math.max(a.rows(), b.rows());
        SparseDoubleMatrix2D c      =   new SparseDoubleMatrix2D(size, size);
        for(int row = 0; row < size; row++)
            for(int col = 0; col < size; col++)
                c.setQuick(row, col, a.getQuick(row, col) + b.getQuick(row, col));
        
        return c;
    }
    
    //Returns the product of matrices a, b
    public static SparseDoubleMatrix2D multiplyMatrix (SparseDoubleMatrix2D a, SparseDoubleMatrix2D b)
    {
        SparseDoubleMatrix2D c    =   new SparseDoubleMatrix2D(a.rows(), b.columns());
        double kSum     =   0.0;
        for(int row = 0; row < c.rows(); row++)
        {
            for(int col = 0; col < c.columns(); col++)
            {
                for(int k = 0; k < b.rows(); k++)
                    kSum += a.getQuick(row, k)  * b.getQuick(k, col);
                
                c.setQuick(row, col, kSum);
                kSum        =   0.0;
            }
        }
        
        return c;
    }
    
    //Divides a column vector by divisor div
    public static SparseDoubleMatrix2D divColVector(SparseDoubleMatrix2D v, double div)
    {
        final int LEN               =   v.columns();
        SparseDoubleMatrix2D c      =   new SparseDoubleMatrix2D(1, LEN);
        
        for(int i = 0; i < LEN; i++)
            c.setQuick(0, i, v.getQuick(0, i) / div);
        
        return c;
    }
    
    //Divides a row vector by divisor div
    public static SparseDoubleMatrix2D divRowVector(SparseDoubleMatrix2D v, double div)
    {
        final int LEN               =   v.rows();
        SparseDoubleMatrix2D c      =   new SparseDoubleMatrix2D(LEN, 1);
        
        for(int i = 0; i < LEN; i++)
            c.setQuick(i, 0, v.getQuick(i, 0) / div);
        
        return c;
    }
    
    //Returns true if the two column vectors a, b are comparable
    public static boolean colVectorSame(SparseDoubleMatrix2D a, SparseDoubleMatrix2D b)
    {
        if(a.columns() != b.columns()) return false;
        
        for(int i = 0; i < a.rows(); i++)
            if(a.getQuick(0, i) != b.getQuick(0, i)) return false;
        
        return true;
    }
    
    //Returns true if the vector is a row vector
    public static boolean isRowVector(SparseDoubleMatrix2D vector)
    {
        return vector.rows() > vector.columns();
    }
    
    //Returns the vectors length/dot product
    public static double getVectorLength(SparseDoubleMatrix2D vector)
    {
        double total    =   0;
        if(isRowVector(vector)) vector = transpose(vector);
        
        for(int i = 0; i < vector.columns(); i++)
            total += Math.pow(vector.getQuick(0, i), 2);
        
        return Math.sqrt(total);
    }
    
    //Returns a normalized vector of vector
    public static SparseDoubleMatrix2D normalizeVector(SparseDoubleMatrix2D vector)
    {
        double vectorLength =   getVectorLength(vector);
        if(!isRowVector(vector)) return divColVector(vector, vectorLength);
        else return divRowVector(vector, vectorLength);
    }
    
    //Returns the transpose of the matrix
    public static SparseDoubleMatrix2D transpose(SparseDoubleMatrix2D matrix)
    {
        SparseDoubleMatrix2D transpose    =   new SparseDoubleMatrix2D(matrix.columns(), matrix.rows());
        for(int row = 0; row < transpose.rows(); row++)
            for(int col = 0; col < transpose.columns(); col++)
                transpose.setQuick(row, col, matrix.getQuick(col, row));
        
        return transpose;
    }
    
    //Returns the stationary vector after performing power iteration on adjMatrix
    public static SparseDoubleMatrix2D powerIterationFull(SparseDoubleMatrix2D adjMatrix)
    {
        final int LIMIT                 =   20;
        final int n                     =   adjMatrix.rows();
        SparseDoubleMatrix2D b          =   addMatrix(adjMatrix, identity(n));
        SparseDoubleMatrix2D v          =   new SparseDoubleMatrix2D(1, n);
        SparseDoubleMatrix2D u;
        
        v.assign(1.0);
        
        v   =   transpose(v);
        b   =   transpose(b);

        
        for(int i = 0; i < LIMIT; i++)
        {
            u           =   multiplyMatrix(b, v);
            double div  =   u.getQuick(0, 0);
            v           =   (isRowVector(u))? divRowVector(u, div) : divColVector(u, div);
        }
        
        return normalizeVector(v);
    }
    
    //Returns the stationary vector
    //Where each entry corresponds to the page ranks of input adjMatrix
    public static SparseDoubleMatrix2D pageRankPI(SparseDoubleMatrix2D adjMatrix)
    {
        final int LIMIT             =   20;
        int n                       =   adjMatrix.rows();
        SparseDoubleMatrix2D v      =   new SparseDoubleMatrix2D(1, n);
        adjMatrix                   =   transpose(adjMatrix);
        
        v.assign(1.0 / n);
        v = transpose(v);
        
        for(int i = 0; i < LIMIT; i++)
            v     =   multiplyMatrix(adjMatrix, v);
        
        return v;
    }
    
    public static SparseDoubleMatrix2D createPageRankMatrix(Graph<Node, Edge> g)
    {
        int n                       =   g.getVertexCount();
        final double c              =   0.15;
        Collection<Node> vertices   =   g.getVertices();
        SparseDoubleMatrix2D matrix =  new SparseDoubleMatrix2D(n, n);
        int i = 0;
        int j = 0;
        
        for(Node node : vertices)
        {
            Collection<Edge> outEdges   =   g.getOutEdges(node);
            j = 0;
            
            for(Node outNode : vertices)
            {
                if(outEdges.isEmpty())
                    matrix.setQuick(i, j, 1.0 / n);
                
                else if(g.findEdge(node, outNode) != null)
                    matrix.setQuick(i, j, (1.0 - c) + (c / g.outDegree(node)));
                else
                    matrix.setQuick(i, j, (1.0 - c) / n);
                
                j++;
            }
            
            i++;
        }
        
        return matrix;
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
    
    public static Map<Node, Double> getScores(SparseDoubleMatrix2D v, Graph<Node, Edge> g)
    {
        Map<Node, Double> nodeScores    =   new HashMap<>();
        Collection<Node> vertices       =   g.getVertices();
        int index                       =   0;
        
        for(Node vertex : vertices)
        {
            double val = v.get(isRowVector(v)? index : 0, isRowVector(v)? 0 : index);
            nodeScores.put(vertex, val);
            index++;
        }
        
        return nodeScores;
    }
}
