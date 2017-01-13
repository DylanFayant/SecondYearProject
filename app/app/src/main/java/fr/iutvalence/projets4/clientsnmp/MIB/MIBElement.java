package fr.iutvalence.projets4.clientsnmp.MIB;

/**
 * Created by ethis on 09/01/17.
 */

/**
 * A MIBElement is a leaf of the MIB tree who have a behavior
 * @param <T> the type of the element returned by getValue method of the MIB leaf.
 */
public interface MIBElement<T> {
    /**
     * The only behavior of the MIB leaf
     * It fetch and returns the needed value into the MIBElement
     * @return T the value represented into the MIBElement
     */
    public T getValue();
}
