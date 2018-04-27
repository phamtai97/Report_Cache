package data;

public class DataObject {
    private String value;
    private static  int count = 0;

    public DataObject (){};
    public DataObject(String value) {

        this.value = value;
    }

    public static DataObject GetDataObject(String value){
        count++;
        return new DataObject(value);
    }

    public String GetValue(){
        return this.value;
    }
}
