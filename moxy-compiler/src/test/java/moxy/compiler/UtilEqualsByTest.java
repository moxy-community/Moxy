package moxy.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

@SuppressWarnings("UnnecessaryBoxing")
public class UtilEqualsByTest {

    @Test
    public void equalsByPositive() {
        ArrayList<Object> firstArray = new ArrayList<>();
        ArrayList<Object> secondArray = new ArrayList<>();

        firstArray.add(new Integer(1));
        secondArray.add(new Integer(1));

        boolean equals = Util.equalsBy(
            firstArray,
            secondArray,
            Objects::equals);
        assert equals;
    }

    @Test
    public void equalsByPredicate() {
        ArrayList<ComplexObject> firstArray = new ArrayList<>();
        ArrayList<ComplexObject> secondArray = new ArrayList<>();

        firstArray.add(new ComplexObject("1"));
        secondArray.add(new ComplexObject("1"));

        boolean equals = Util.equalsBy(
            firstArray,
            secondArray,
            (first, second) -> Objects.equals(first.field, second.field));
        assert equals;
    }

    @Test
    public void equalsByNulls() {
        boolean equals = Util.equalsBy(
            null,
            null,
            Objects::equals);
        assert !equals;
    }

    @Test
    public void equalsByListAndNull() {

        ArrayList<Object> firstArray = new ArrayList<>();

        firstArray.add(new Integer(1));

        boolean equals = Util.equalsBy(
            firstArray,
            null,
            Objects::equals);
        assert !equals;
    }

    @Test
    public void equalsByNullAndList() {

        ArrayList<Object> secondArray = new ArrayList<>();

        secondArray.add(new Integer(1));

        boolean equals = Util.equalsBy(
            null,
            secondArray,
            Objects::equals);
        assert !equals;
    }

    static class ListWrapper {
        private List<Object> first;
        private List<Object> second;

        public ListWrapper(List<Object> first, List<Object> second) {
            this.first = first;
            this.second = second;
        }

        public List<Object> getFirst() {
            return first;
        }

        public List<Object> getSecond() {
            return second;
        }
    }

    static class ComplexObject {
        private String field;

        ComplexObject(String field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ComplexObject)) return false;
            ComplexObject that = (ComplexObject) o;
            return Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }
    }
}


