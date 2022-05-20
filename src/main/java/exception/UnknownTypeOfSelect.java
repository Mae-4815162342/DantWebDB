package exception;

public class UnknownTypeOfSelect extends Exception {
    public UnknownTypeOfSelect(String type){
        super(type + "isn't a supported request type");
    }
}
