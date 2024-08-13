package BEANS;

public enum CategoryEnum {
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
    /**
     * Retrieves the enum constant of {@code CategoryEnum} with the specified ID.
     * <p>
     * This static method iterates through all enum constants of {@code CategoryEnum} to find
     * the one that matches the given ID. If no matching constant is found, an
     * {@code IllegalArgumentException} is thrown.
     *
     * @param id The ID of the enum constant to retrieve.
     * @return The {@code CategoryEnum} constant with the specified ID.
     * @throws IllegalArgumentException If no enum constant with the specified ID is found.
     */
    public static CategoryEnum fromId(int id) {
        for (CategoryEnum category : values()) {
            if (category.getId() == id) {
                return category;
            }
        }
        throw new IllegalArgumentException("No matching BEANS.CategoryEnum for id " + id);
    }
}
