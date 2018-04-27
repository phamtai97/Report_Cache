package data;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sun.javafx.collections.MappingChange;
import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.concurrent.TimeUnit;



public class DemoCaffeine_Manual {

    public static void Output(Map<String, DataObject> map) {
        Collection<DataObject> list = new ArrayList<DataObject>();
        list = map.values();
        int i = 0;
        for(DataObject dataObject : list){
            System.out.println("<<" + i ++ + ">> " + dataObject.GetValue());
        }

    }
    public static void main(String[] args) {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(5)
                .recordStats()
                .build();

        DataObject dataObject;
        ArrayList<String> listKey = new ArrayList<String>();
        int j = 0;
        for(int i = 0; i <= 4000; i ++) {
            dataObject = new DataObject("Toi dung vi tri " + i);
            System.out.println(cache.stats());
            cache.put("" + i, dataObject);
            listKey.add(String.valueOf(i));
            Map<String, DataObject> map = cache.getAllPresent(listKey);
            System.out.println("Lan: " + j++);
            Output(map);
        }
    }
}
