/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

/**
 *
 * @author kleopatra
 */
    //generate a filtered observable list from an existing observable list V
    //and a mapping function E
    //used to generate a list of major clue numbers (to generate Glyphs) from a list of clues
    //source
    //https://stackoverflow.com/questions/29656772/deriving-mapped-distinct-values-into-an-observablelist-off-another-observablelis
    public class DistinctMappingList<V, E> extends ObservableListBase<E> {

        private List<E> mapped;
        private Function<V, E> mapper;

        public DistinctMappingList(ObservableList<V> source, Function<V, E> mapper) {
            this.mapper = mapper;
            mapped = applyMapper(source); 
            ListChangeListener l = c -> sourceChanged(c);
            source.addListener(l);
        }

        private void sourceChanged(ListChangeListener.Change<? extends V> c) {
            beginChange();
            List<E> backing = applyMapper(c.getList());
            while(c.next()) {
                if (c.wasAdded()) {
                    wasAdded(c, backing);
                } else if (c.wasRemoved()) {
                    wasRemoved(c, backing);
                } else {
                    // throw just for the example
                    throw new IllegalStateException("unexpected change " + c);
                }
            }
            endChange();
        }

        private void wasRemoved(ListChangeListener.Change<? extends V> c, List<E> backing) {
            List<E> removedCategories = applyMapper(c.getRemoved());
            for (E e : removedCategories) {
                if (!backing.contains(e)) {
                    int index = indexOf(e);
                    mapped.remove(index);
                    nextRemove(index, e);
                }
            }
        }

        private void wasAdded(ListChangeListener.Change<? extends V> c, List<E> backing) {
            List<E> addedCategories = applyMapper(c.getAddedSubList());
            for (E e : addedCategories) {
                if (!contains(e)) {
                    int last = size();
                    mapped.add(e);
                    nextAdd(last, last +1);
                }
            }
        }

        private List<E> applyMapper(List<? extends V> list) {
            List<E> backing = list.stream().map(p -> mapper.apply(p)).distinct()
                    .collect(Collectors.toList());
            return backing;
        }

        @Override
        public E get(int index) {
            return mapped.get(index);
        }

        @Override
        public int size() {
            return mapped.size();
        }
    }
