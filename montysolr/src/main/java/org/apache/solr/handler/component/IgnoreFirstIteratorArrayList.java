package org.apache.solr.handler.component;

import java.util.ArrayList;
import java.util.Iterator;

public class IgnoreFirstIteratorArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -1467370841645621973L;
    private boolean foolThem = true;

    public Iterator<E> iterator() {
        if (foolThem) {
            ArrayList<E> al = new ArrayList<E>();
            foolThem = false;
            return al.iterator();
        }

        return super.iterator();
    }
}
