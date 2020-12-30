package mm.pndaza.thupyadictionary.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable {
    private int id;
    private String word;
    private String detail;

    public Word(int id, String word) {
        this.id = id;
        this.word = word;
    }

    protected Word(Parcel in) {
        id = in.readInt();
        word = in.readString();
        detail = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(word);
        parcel.writeString(detail);

    }
}
