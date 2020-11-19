package com.dangbinh.dinter.mapper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseMapper<Entity, Model> implements IMapper<Entity, Model> {
    public Model map(DataSnapshot dataSnapshot) {
        Entity entity = (Entity) dataSnapshot.getValue();
        return map(entity);
    }
    public List<Model> mapList(DataSnapshot dataSnapshot) {
        List<Model> list = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            Log.d("FirebaseMapper", item.getValue().toString());
            list.add(map(item));
        }
        return list;
    }

    private Class<Entity> getEntityClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<Entity>) superclass.getActualTypeArguments()[0];
    }
}
