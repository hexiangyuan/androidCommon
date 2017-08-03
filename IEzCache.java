public interface IEzCache {
    boolean saveObject(Serializable object, String key);

    Serializable getObject(String key);

    boolean removeObject(String key);
