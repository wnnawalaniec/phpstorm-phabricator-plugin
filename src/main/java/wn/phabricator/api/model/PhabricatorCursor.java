package wn.phabricator.api.model;

import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PhabricatorCursor {
    private Integer limit;
    private Order order;
    private String before;
    private String after;

    public PhabricatorCursor() {
    }

    public PhabricatorCursor(Integer limit) {
        assert limit == null || limit <= 100;
        this.limit = limit;
    }

    public PhabricatorCursor(Integer limit, Order order, @Nullable String before, @Nullable String after) {
        assert limit == null || limit <= 100;
        this.limit = limit;
        this.order = order;
        this.before = before;
        this.after = after;
    }

    public List<Pair<String, String>> asParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();

        params.add(new Pair<>("limit", String.valueOf(limit)));

        if (order != null) {
            params.add(new Pair<>("order", String.valueOf(order)));
        }

        if (after != null) {
            params.add(new Pair<>("after", after));
        }

        if (before != null) {
            params.add(new Pair<>("before", before));
        }

        return params;
    }

    public Boolean hasNextPage()
    {
        return this.after != null;
    }

    public Boolean hasPreviosPage()
    {
        return this.after != null;
    }

    public void setLimit(@Nullable Integer limit) {
        this.limit = limit;
    }

    public enum Order {
        ID("id"),
        NAME("name");

        private String name;

        Order(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
