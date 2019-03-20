package team_2e.escape_farm;

public class Animal {
    private float animal_X, animal_Y;
    private String animal_Kind, animal_Block;

    public Animal(String animal_Kind, String animal_Block){
        this.animal_Kind = animal_Kind;
        this.animal_Block = animal_Block;
    }
    public float getX() {
        return animal_X;
    }

    public float getY() {
        return animal_Y;
    }

    public String getKind() {
        return animal_Kind;
    }

    public String getBlock() {
        return animal_Block;
    }

    public void setX(float animal_X) {
        this.animal_X = animal_X;
    }

    public void setY(float animal_Y) {
        this.animal_Y = animal_Y;
    }

}
