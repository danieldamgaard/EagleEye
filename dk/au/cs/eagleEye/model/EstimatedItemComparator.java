/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.au.cs.eagleEye.model;

import java.util.Comparator;

/**
 *
 * @author GalaxyNetworks
 */
public class EstimatedItemComparator implements Comparator<EstimatedItem> {
    @Override
    public int compare(EstimatedItem compareFrom, EstimatedItem compareTo) {
        return compareFrom.compareTo(compareTo);
    }
}
