package com.greenDao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.hengye.share.model.greenrobot");

        addNote(schema);

//        new DaoGenerator().generateAll(schema, "../DaoExample/src/main/java");
        new DaoGenerator().generateAll(schema, "C:\\programme\\Android\\code\\Share\\app\\src\\main\\java");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
}
