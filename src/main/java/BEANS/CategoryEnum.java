package BEANS;

public enum CategoryEnum {
    // Define your enum constants
    FOOD(1),
    ELECTRONICS(2),
    FASHION(3),
    TRAVEL(4);

    private int id;

    // Private constructor to associate ID with the enum constant
    CategoryEnum(int id) {
        this.id = id;
    }

    // Getter for the ID
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

    // Static method to get the enum constant by name
    public static CategoryEnum fromString(String name) {
        for (CategoryEnum category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No matching BEANS.CategoryEnum for name " + name);
    }
}
