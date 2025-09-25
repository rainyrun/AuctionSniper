package pojo;

import java.util.Objects;

public class Item {
    public final String itemId;
    public final int stopPrice;

    public Item(String itemId, int stopPrice) {
        this.itemId = itemId;
        this.stopPrice = stopPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return stopPrice == item.stopPrice && Objects.equals(itemId, item.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, stopPrice);
    }

    public boolean allowsBid(int bid) {
        return bid <= stopPrice;
    }
}
