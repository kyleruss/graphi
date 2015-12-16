
package com.graphi.sim;


public class Edge
{
    private int id;
    private double weight;
    private double capacity;
    
    public Edge()
    {
        this(0, 0.0, 0.0);
    }
    
    public Edge(int id, double weight, double capacity)
    {
        this.id =   id;
        this.weight =   weight;
        this.capacity = capacity;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public double getCapacity()
    {
        return capacity;
    }
    
    @Override
    public String toString()
    {
        return "[ID: " + id + ", WEIGHT: " + weight + ", CAPACITY: " + capacity + "]";
    }
}
