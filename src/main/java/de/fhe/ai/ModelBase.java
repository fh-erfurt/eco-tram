package de.fhe.ai;

public abstract class ModelBase {

    private final int id;

    public ModelBase(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected String getString() {
        //TODO

        return "";
    }

}
