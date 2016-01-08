package com.greenDao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.hengye.share.model.greenrobot");

        schema.enableKeepSectionsByDefault();
        addTopicDraft(schema);
        addUser(schema);
//        new DaoGenerator().generateAll(schema, "../DaoExample/src/main/java");
//        new DaoGenerator().generateAll(schema, "G:\\android\\code\\android-project\\personal\\androidstudio\\Share\\app\\src\\main\\java");
        new DaoGenerator().generateAll(schema, "C:\\programme\\Android\\code\\Share\\app\\src\\main\\java");
    }

    private static void addUser(Schema schema) {
        Entity entity = schema.addEntity("User");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addStringProperty("uid").notNull();
        entity.addStringProperty("token").notNull();
        entity.addIntProperty("parentType").notNull();
        entity.addStringProperty("parentJson");
        entity.addStringProperty("refreshToken");
        entity.addLongProperty("expiresIn");
        entity.addStringProperty("name");
        entity.addStringProperty("avatar");
        entity.addStringProperty("gender");
        entity.addStringProperty("sign");
        entity.addStringProperty("cover");
    }


    private static void addTopicDraft(Schema schema) {
        Entity entity = schema.addEntity("TopicDraft");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addStringProperty("content").notNull();
        entity.addDateProperty("date");
        entity.addStringProperty("urls");
        entity.addStringProperty("uid");
        entity.addStringProperty("targetTopicId");
        entity.addStringProperty("targetCommentId");
        entity.addIntProperty("isCommentOrigin");
        entity.addIntProperty("isComment");
        entity.addIntProperty("type");
        entity.addIntProperty("parentType");
    }
}
