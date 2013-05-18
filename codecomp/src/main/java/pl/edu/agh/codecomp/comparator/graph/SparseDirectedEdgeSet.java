/*
 * Copyright 2011 David Jurgens
 *
 * This file is part of the S-Space package and is covered under the terms and
 * conditions therein.
 *
 * The S-Space package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation and distributed hereunder to you.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND NO REPRESENTATIONS OR WARRANTIES,
 * EXPRESS OR IMPLIED ARE MADE.  BY WAY OF EXAMPLE, BUT NOT LIMITATION, WE MAKE
 * NO REPRESENTATIONS OR WARRANTIES OF MERCHANT- ABILITY OR FITNESS FOR ANY
 * PARTICULAR PURPOSE OR THAT THE USE OF THE LICENSED SOFTWARE OR DOCUMENTATION
 * WILL NOT INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER
 * RIGHTS.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.agh.codecomp.comparator.graph;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import pl.edu.agh.codecomp.comparator.graph.primitive.AbstractIntSet;
import pl.edu.agh.codecomp.comparator.graph.primitive.IntIterator;
import pl.edu.agh.codecomp.comparator.graph.primitive.IntSet;
import pl.edu.agh.codecomp.comparator.graph.primitive.TroveIntSet;


/**
 * An {@link EdgeSet} implementation that stores {@link DirectedEdge} instances
 * for a vertex.
 *
 * @author David Jurgens
 */
public class SparseDirectedEdgeSet extends AbstractSet<DirectedEdge> 
        implements EdgeSet<DirectedEdge>, java.io.Serializable {

    private static final long serialVersionUID = 1L;
        
    /**
     * The vertex to which all edges in the set are connected
     */
    private final int rootVertex;
    
    /**
     * The set of vertices to which this vertex has an edge
     */
    private final IntSet outEdges;

    /**
     * The set of vertices that have an edge to the root vertex
     */
    private final IntSet inEdges;

    /**
     * Creates a new {@code SparseDirectedEdgeSet} for the specfied vertex.
     */
    public SparseDirectedEdgeSet(int rootVertex) {
        this.rootVertex = rootVertex;
        outEdges = new TroveIntSet();
        inEdges = new TroveIntSet();
    }
    
    /**
     * Adds the edge to this set if one of the vertices is the root vertex and
     * if the non-root vertex has a greater index that this vertex.
     */
    public boolean add(DirectedEdge e) {
        if (e.from() == rootVertex) {
            return outEdges.add(e.to());
        }
        else if (e.to() == rootVertex) {
            return inEdges.add(e.from());
        }
        return false;
    }

    /**
     * {@inheritDoc}  The set of vertices returned by this set is immutable.
     */
    public IntSet connected() {
        return new CombinedSet();
    }

    /**
     * {@inheritDoc}
     */
    public boolean connects(int vertex) {
        return inEdges.contains(vertex) || outEdges.contains(vertex);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o) {
        if (o instanceof DirectedEdge) {
            DirectedEdge e = (DirectedEdge)o;
            if (e.to() == rootVertex) 
                return inEdges.contains(e.from());
            else if (e.from() == rootVertex)
                return outEdges.contains(e.to());
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    public SparseDirectedEdgeSet copy(IntSet vertices) {        
        SparseDirectedEdgeSet copy = new SparseDirectedEdgeSet(rootVertex);
        if (vertices.size() < inEdges.size()
                && vertices.size() < outEdges.size()) {

            IntIterator iter = vertices.iterator();
            while (iter.hasNext()) {
                int v = iter.nextInt();
                if (inEdges.contains(v)) 
                    copy.inEdges.add(v);
                if (outEdges.contains(v)) 
                    copy.inEdges.add(v);
            }            
        }
        else {
            IntIterator iter = inEdges.iterator();
            while (iter.hasNext()) {
                int v = iter.nextInt();
                if (vertices.contains(v))
                    copy.inEdges.add(v);
            }
            iter = outEdges.iterator();
            while (iter.hasNext()) {
                int v = iter.nextInt();
                if (vertices.contains(v))
                    copy.outEdges.add(v);
            }
        }
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    public int disconnect(int vertex) {
        int removed = 0;
        if (inEdges.remove(vertex))
            removed++;
        if (outEdges.remove(vertex))
            removed++;
        return removed;
    }

    /**
     * {@inheritDoc}
     */
    public Set<DirectedEdge> getEdges(int vertex) {
        return new VertexEdgeSet(vertex);
    }    

    /**
     * {@inheritDoc}
     */
    public int getRoot() {
        return rootVertex;
    }

    /**
     * Returns the set of {@link DirectedEdge} instances that point to the root
     * vertex.  Changes to this set will be reflected in this {@link EdgeSet}
     * and vice versa.
     */
    public Set<DirectedEdge> inEdges() {
        return new EdgeSetWrapper(inEdges, true);        
    }

    /**
     * {@inheritDoc}
     */
    @Override public boolean isEmpty() {
        return inEdges().isEmpty() && outEdges.isEmpty();
    }

    /**
     * {@inheritDoc}
     */ 
    public Iterator<DirectedEdge> iterator() {
        return new DirectedEdgeIterator();
    }
    
    /**
     * Returns the set of {@link DirectedEdge} instances that originate from the
     * root vertex.  Changes to this set will be reflected in this {@link
     * EdgeSet} and vice versa.
     */
    public Set<DirectedEdge> outEdges() {
        return new EdgeSetWrapper(outEdges, false);
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o) {
        if (o instanceof DirectedEdge) {
            DirectedEdge e = (DirectedEdge)o;
            if (e.to() == rootVertex) 
                return inEdges.remove(e.from());
            else if (e.from() == rootVertex)
                return outEdges.remove(e.to());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */    
    public int size() {
        return inEdges.size() + outEdges.size();
    }

    /**
     * A wrapper around the set of edges that connect another vertex to the root
     * vertex
     */
    private class VertexEdgeSet extends AbstractSet<DirectedEdge> {
        
        /**
         * The vertex in the edges that is not this root vertex
         */
        private final int otherVertex;

        public VertexEdgeSet(int otherVertex) {
            this.otherVertex = otherVertex;
        }

        @Override public boolean add(DirectedEdge e) {
            return ((e.to() == rootVertex && e.from() == otherVertex)
                    || (e.from() == rootVertex && e.to() == otherVertex))
                && SparseDirectedEdgeSet.this.add(e);
        }

        @Override public boolean contains(Object o) {
            if (!(o instanceof DirectedEdge))
                return false;
            DirectedEdge e = (DirectedEdge)o;
            return ((e.to() == rootVertex && e.from() == otherVertex)
                    || (e.from() == rootVertex && e.to() == otherVertex))
                && SparseDirectedEdgeSet.this.contains(e);
        }

        @Override public Iterator<DirectedEdge> iterator() {
            return new EdgeIterator();
        }

        @Override public boolean remove(Object o) {
            if (!(o instanceof DirectedEdge))
                return false;
            DirectedEdge e = (DirectedEdge)o;
            return ((e.to() == rootVertex && e.from() == otherVertex)
                    || (e.from() == rootVertex && e.to() == otherVertex))
                && SparseDirectedEdgeSet.this.remove(e);
        }

        @Override public int size() {
            int size = 0;
            if (inEdges.contains(otherVertex))
                size++;
            if (outEdges.contains(otherVertex))
                size++;
            return size;
        }

        /**
         * An iterator over all the edges that connect the root vertex to a
         * single vertex
         */
        class EdgeIterator implements Iterator<DirectedEdge> {
            
            /**
             * The next edge to return
             */ 
            DirectedEdge next;

            /**
             * The edge that was most recently returned or {@code null} if the
             * edge was removed or has yet to be returned.
             */
            DirectedEdge cur;

            boolean returnedIn;
            boolean returnedOut;

            public EdgeIterator() {
                advance();
            }

            private void advance() {
                next = null;
                if (inEdges.contains(otherVertex) && !returnedIn) {
                    next = new SimpleDirectedEdge(otherVertex, rootVertex);
                    returnedIn = true;
                }
                else if (next == null && !returnedOut 
                         && outEdges.contains(otherVertex)) {
                    next = new SimpleDirectedEdge(rootVertex, otherVertex);
                    returnedOut = true;
                } 
            }

            public boolean hasNext() {
                return next != null;
            }

            public DirectedEdge next() {
                cur = next;
                advance();
                return cur;
            }

            public void remove() {
                if (cur == null)
                    throw new IllegalStateException();
                SparseDirectedEdgeSet.this.remove(cur);
                cur = null;
            }
        }
    }


    /**
     * A wrapper around the set of {@link DirectedEdge} instances that either
     * point to the root (the in-edges) or originate from the root (out-edges).
     * This class is a utility to expose both edges sets while allowing
     * modifications to the returned sets to be reflected in this {@code
     * SparseDirectedEdgeSet}.
     */
    private class EdgeSetWrapper extends AbstractSet<DirectedEdge> {
        
        /**
         * The set of vertices linked to the root vertex according the {@code
         * areInEdge} property
         */
        private final Set<Integer> vertices;

        /**
         * {@code true} if the edges being wraped are in-edges (point to the
         * root), or {@code false} if the edges are out-edges (originate from
         * the root).
         */
        private final boolean areInEdges;

        public EdgeSetWrapper(Set<Integer> vertices, boolean areInEdges) {
            this.vertices = vertices;
            this.areInEdges = areInEdges;
        }

        @Override public boolean add(DirectedEdge e) {
            if (areInEdges) {
                return vertices.add(e.from());
            }
            else {
                assert e.from() == rootVertex : "incorrect edge set view";
                return vertices.add(e.to());
            }
        }

        @Override public boolean contains(Object o) {
            if (!(o instanceof DirectedEdge))
                return false;
            DirectedEdge e  = (DirectedEdge)o;
            if (areInEdges) {
                return vertices.contains(e.from());
            }
            else {
                assert e.from() == rootVertex : "incorrect edge set view";
                return vertices.contains(e.to());
            }                             
        }

        @Override public Iterator<DirectedEdge> iterator() {
            return new EdgeSetWrapperIterator();
        }

        @Override public boolean remove(Object o) {
            if (!(o instanceof DirectedEdge))
                return false;
            DirectedEdge e  = (DirectedEdge)o;
            if (areInEdges) {
                return vertices.remove(e.from());
            }
            else {
                assert e.from() == rootVertex : "incorrect edge set view";
                return vertices.remove(e.to());
            }                             
        }

        @Override public int size() {
            return vertices.size();
        }

        public class EdgeSetWrapperIterator implements Iterator<DirectedEdge> {
            
            private final Iterator<Integer> iter;

            public EdgeSetWrapperIterator() {
                iter = vertices.iterator();
            }

            public boolean hasNext() {
                return iter.hasNext();
            }

            public DirectedEdge next() {
                return (areInEdges) 
                    ? new SimpleDirectedEdge(iter.next(), rootVertex)
                    : new SimpleDirectedEdge(rootVertex, iter.next());
            }

            public void remove() {
                iter.remove();
            }
        }

    }

    /**
     * An iterator over the edges in this set that constructs {@link
     * DirectedEdge} instances as it traverses through the set of connected
     * vertices.
     */
    private class DirectedEdgeIterator implements Iterator<DirectedEdge> {

        private Iterator<Integer> inVertices;

        private Iterator<Integer> outVertices;
        
        private Iterator<Integer> toRemoveFrom;

        public DirectedEdgeIterator() {
            inVertices = inEdges.iterator();
            outVertices = outEdges.iterator();
            toRemoveFrom = null;
        }

        public boolean hasNext() {
            return inVertices.hasNext() || outVertices.hasNext();
        }

        public DirectedEdge next() {
            if (!hasNext())
                throw new NoSuchElementException();
            int from = -1, to = -1;
            if (inVertices.hasNext()) {
                from = inVertices.next();
                to = rootVertex;
                toRemoveFrom = inVertices;
            }
            else {
                assert outVertices.hasNext() : "bad iterator logic";
                from = rootVertex;
                to = outVertices.next();
                toRemoveFrom = outVertices;
            }
            return new SimpleDirectedEdge(from, to);
        }

        public void remove() {
            if (toRemoveFrom == null)
                throw new IllegalStateException("No element to remove");
            toRemoveFrom.remove();
        }
    }

     private class CombinedSet extends AbstractIntSet {

        @Override public boolean contains(int i) {
            return inEdges.contains(i) || outEdges.contains(i);
        }

        @Override public boolean contains(Object o) {
            if (!(o instanceof Integer))
                return false;
            int i = ((Integer)o).intValue();
            return inEdges.contains(i) || outEdges.contains(i);
        }

        @Override public IntIterator iterator() {
            return new CombinedIterator();
        }

         @Override public int size() {
             IntSet smaller, larger;
             if (inEdges.size() < outEdges.size()) {
                 smaller = inEdges;
                 larger = outEdges;
             }
             else {
                 smaller = outEdges;
                 larger = inEdges;
             }
             int size = larger.size();
             IntIterator iter = smaller.iterator();
             while (iter.hasNext()) {
                 int i = iter.nextInt();
                 if (!larger.contains(i))
                     size++;
             }
             return size;
         }
     }

     private class CombinedIterator implements IntIterator {

         private IntIterator largerIter;
         private IntIterator smallerIter;
         private IntSet larger;

         int next;
         boolean hasNext;

         public CombinedIterator() {
             if (inEdges.size() < outEdges.size()) {
                 smallerIter = inEdges.iterator();
                 largerIter = outEdges.iterator();
                 larger = outEdges;
             }
             else {
                 smallerIter = outEdges.iterator();
                 largerIter = inEdges.iterator();
                 larger = inEdges;
             }
             advance();
         }

         private void advance() {
             hasNext = false;
             if (largerIter.hasNext()) {
                 next = largerIter.nextInt();
                 hasNext = true;
             }
             else {
                 while (smallerIter.hasNext()) {
                     int i = smallerIter.nextInt();
                     if (!larger.contains(i)) {
                         next = i;
                         hasNext = true;
                         break;
                     }
                 }
             }
         }
             
         public boolean hasNext() {
             return hasNext;
         }

         public int nextInt() {
             if (!hasNext())
                 throw new NoSuchElementException();
             int n = next;
             advance();
             return n;
         }

         public Integer next() {
             return nextInt();
         }

         public void remove() {
             throw new UnsupportedOperationException();
         }
     }
}