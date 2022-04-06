public class Worker {
    private Database database;
    private static final Worker instance;

    public Worker(){
        this.database = new Database();
    }

    public static Worker getWorker() {
        return Worker
    }

    public static Worker getInstance() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }


    // methodes recu par les endpoints create table, insert into table, ...
}
