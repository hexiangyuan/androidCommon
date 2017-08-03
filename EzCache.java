public class EzCache implements IEzCache {
    private Context context;
    private File cacheFileDir;

    public EzCache(Context context) {
        this.context = context;
        File cacheDir = context.getCacheDir();
        this.cacheFileDir = new File(cacheDir, "ezcache");
    }

    public EzCache(Context context, String cacheFile) {
        this.context = context;
        File cacheDir = context.getCacheDir();
        this.cacheFileDir = new File(cacheDir, cacheFile);
    }

    @Override
    public boolean saveObject(Serializable object, String key) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File cacheFile = new File(cacheFileDir, key);
            if(!this.cacheFileDir.exists()){
                this.cacheFileDir.mkdirs();
            }
            if (cacheFile.exists()) {
                cacheFile.delete();
            }
            boolean b = cacheFile.createNewFile();
            if (b) {
                fos = new FileOutputStream(cacheFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(object);
                oos.flush();
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Serializable getObject(String key) {
        FileInputStream fis = null;
        ObjectInput ois = null;
        File cacheFile = new File(this.cacheFileDir, key);
        if (!this.cacheFileDir.exists() || !cacheFile.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(cacheFile);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean removeObject(String key) {
        File cacheFile = new File(this.cacheFileDir, key);
        if (this.cacheFileDir.exists() && cacheFile.exists()) {
            return cacheFile.delete();
        } else {
            return true;
        }
    }
}
