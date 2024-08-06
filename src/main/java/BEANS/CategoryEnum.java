package BEANS;

public enum CategoryEnum {
    // Define your enum constants
    FOOD(1),
    ELECTRONICS(2),
    FASHION(3),
    TRAVEL(4);

    private int id;

    CategoryEnum(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    // Static method to get the enum constant by ID
    public static CategoryEnum fromId(int id) {
        for (CategoryEnum category : values()) {
            if (category.getId() == id) {
                return category;
            }
        }
        throw new IllegalArgumentException("No matching BEANS.CategoryEnum for id " + id);
    }

}
