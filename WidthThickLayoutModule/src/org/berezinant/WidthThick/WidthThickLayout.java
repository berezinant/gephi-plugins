/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berezinant.WidthThick;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterable;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutProperty;
import org.gephi.layout.plugin.*;
import org.openide.util.NbBundle;
/**
 *
 * @author Anton
 */
class WidthThickLayout extends AbstractLayout implements Layout, Comparator {

    private WidthThickLayoutBuilder builder;
    private Graph graph;
    private boolean converged;
    private double sizeX, sizeY;
    private Random random;
    
    public WidthThickLayout(LayoutBuilder layoutBuilder, double sizeX, double sizeY) {
        super(layoutBuilder);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        builder = new WidthThickLayoutBuilder();
    }

    @Override
    public void initAlgo() {
        converged = false;
        graph = graphModel.getGraphVisible();
    }
    /** i = 2 => thick
        i = 3 => width */
    private double parseAttr(Node n, int i) {
        try {
        return Double.parseDouble(n.getAttributes().getValue(i).toString());
        } catch (NumberFormatException e) {
            System.out.printf(e.getMessage());
            throw e;
        }
    }

    public int compare(Object o1, Object o2) {
        try {
            Node n1 = (Node) o1;
            Node n2 = (Node) o2;
        
        if (parseAttr(n1, 3) < parseAttr(n2, 3)) {
            return -2;
        } else if (parseAttr(n1, 3) > parseAttr(n2, 3)) {
            return 2;
        } else if (parseAttr(n1, 2) < parseAttr(n2,2)) {
            return -1;
        } else if (parseAttr(n1, 2) > parseAttr(n2, 2)) {
            return 1;
        }
        return 0;
        } catch (ClassCastException e) {
            throw e;
        }
    }
    
    public boolean equals(Object o1, Object o2) {
        try {
            Node n1 = (Node) o1;
            Node n2 = (Node) o2;
        /*
        if ((parseAttr(n1, 1) != parseAttr(n2, 1)) 
        || (parseAttr(n1, 0) != parseAttr(n2,0))) {
            return true;
        }
        return false;
            */
        return (n1.getId() == n2.getId());
        } catch (ClassCastException e) {
            throw e;
        }   
    }
    
    @Override
    public void goAlgo() {
        graph = graphModel.getGraphVisible();
        /** these lists contain unique values of attributes */
        ArrayList<Double> widthValues = new ArrayList();
        ArrayList<Double> thickValues = new ArrayList();
        for (Node n : graph.getNodes()) {
            if (!thickValues.contains(parseAttr(n, 2))) {
                thickValues.add(parseAttr(n, 2));
            } 
            if (!widthValues.contains(parseAttr(n, 3))) {
                widthValues.add(parseAttr(n, 3));
            }
        }
        /** that is how the size of graph defined */
        double stepY = sizeY / widthValues.size();
        double stepX = sizeX / thickValues.size();
               
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node n : graph.getNodes()) {
            nodes.add(n);
        }
        Collections.sort(nodes, this);
        double eps = 1e-5;
        double currW, currT;
        double prevW = parseAttr(nodes.get(0), 3);
        double prevT = parseAttr(nodes.get(0), 2);
        int x = 0;
        int y = 0;
        for (int i = 0; i < nodes.size(); i++) {   
            currW = parseAttr(nodes.get(i),3);
            currT = parseAttr(nodes.get(i),2);
            
            if (Math.abs(currW - prevW) > eps) {
                   y -= 2*stepY;
                   x = 0;
            } else if (Math.abs(currT - prevT) > eps) {
                x += 2*stepX;
            } else {
                x += 0.1*stepX;
            }
            prevW = currW;
            prevT = currT;
            for (Node n : graph.getNodes()) {
                if (nodes.get(i).equals(n)) {
                    n.getNodeData().setX((float) (x));
                    n.getNodeData().setY((float) (y));
                }
            }
        }
        converged = true;
    }

    @Override
    public boolean canAlgo() {
        return !converged;
    }

    @Override
    public void endAlgo() {
        
    }

    @Override
   public LayoutProperty[] getProperties() {
        List<LayoutProperty> properties = new ArrayList<LayoutProperty>();
        try {
            properties.add(LayoutProperty.createProperty(
                    this, Double.class,
                    NbBundle.getMessage(getClass(), "WidthThickLayoutBuilder.spaceSize.name"),
                    null,
                    "WidthThickLayoutBuilder.spaceSize.name",
                    NbBundle.getMessage(getClass(), "WidthThickLayoutBuilder.spaceSize.desc"),
                    "getSize",  "SetSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties.toArray(new LayoutProperty[0]);
    }

    @Override
    public void resetPropertiesValues() {
    }  
    
    public void setSize(Double size) {
        this.sizeX = size;
        this.sizeY = size;
    }

    public Double getSizeX() {
        return sizeX;
    }
}
