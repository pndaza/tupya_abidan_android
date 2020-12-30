package mm.pndaza.thupyadictionary.models;

public class Favourite {
    private int id;
    private String word;

    public Favourite(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}


