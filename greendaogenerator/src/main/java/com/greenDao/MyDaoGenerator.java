package com.greenDao;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * greenDao生成器用不了的时候改成2.1.3(com.android.tools.build:gradle)
 */
public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(2, "com.hengye.share.model.greenrobot");

        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        addEntity(schema);
//        new DaoGenerator().generateAll(schema, "../DaoExample/src/main/java");
//        new DaoGenerator().generateAll(schema, "G:\\android\\code\\android-project\\personal\\androidstudio\\Share\\app\\src\\main\\java");
//        /Users/yuhy/Programme/android/code/personal/Share/app/src/main/java
        new DaoGenerator().generateAll(schema, "/Users/yuhy/Programme/android/code/personal/Share/app/src/main/java");
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
        entity.addStringProperty("account");
        entity.addStringProperty("password");
        entity.addStringProperty("adToken");
        entity.addStringProperty("cookie");
        entity.addStringProperty("extra");
    }


    private static void addTopicDraft(Schema schema) {
        Entity entity = schema.addEntity("TopicDraft");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addStringProperty("content").notNull();
        entity.addDateProperty("date");
        entity.addStringProperty("urls");
        entity.addStringProperty("uid");
        entity.addStringProperty("targetTopicJson");
        entity.addStringProperty("targetTopicId");
        entity.addStringProperty("targetCommentId");
        entity.addStringProperty("targetCommentUserName");
        entity.addStringProperty("targetCommentContent");
        entity.addStringProperty("assignGroupIdStr");
        entity.addLongProperty("publishTiming");
        entity.addIntProperty("isCommentOrigin");
        entity.addIntProperty("status").notNull();
        entity.addIntProperty("isMention");
        entity.addIntProperty("type");
        entity.addIntProperty("parentType");
    }

    private static void addGroupList(Schema schema) {
        Entity entity = schema.addEntity("GroupList");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addIntProperty("insertNumber").notNull();
        entity.addStringProperty("uid").notNull();
        entity.addStringProperty("gid").notNull();
        entity.addStringProperty("title").notNull();
        entity.addStringProperty("name").notNull();
        entity.addIntProperty("count");
        entity.addIntProperty("type");
        entity.addIntProperty("visible");
        entity.addIntProperty("remind");
    }

    private static void addGroupMember(Schema schema) {
        Entity entity = schema.addEntity("GroupMember");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addStringProperty("uid").notNull();
        entity.addStringProperty("tid").notNull();
        entity.addStringProperty("gid").notNull();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("gender");
        entity.addStringProperty("description");
        entity.addStringProperty("avatar");
        entity.addIntProperty("verified");
        entity.addIntProperty("verifiedType");
        entity.addIntProperty("following");
        entity.addIntProperty("followMe");
        entity.addIntProperty("isGroupMember");
    }

    private static void addFollower(Schema schema) {
        Entity entity = schema.addEntity("Follower");
        entity.implementsSerializable();
        entity.addIdProperty().autoincrement().primaryKeyAsc();
        entity.addStringProperty("uid").notNull();
        entity.addStringProperty("tid").notNull();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("pinyinName");
        entity.addStringProperty("gender");
        entity.addStringProperty("description");
        entity.addStringProperty("avatar");
        entity.addIntProperty("verified");
        entity.addIntProperty("verifiedType");
        entity.addIntProperty("following");
        entity.addIntProperty("followMe");
    }

    private static void addShareJson(Schema schema) {
        Entity entity = schema.addEntity("ShareJson");
        entity.implementsSerializable();
        entity.addStringProperty("model").primaryKey().notNull();
        entity.addStringProperty("json");
    }

    private static void addEntity(Schema schema){
        addTopicDraft(schema);
        addUser(schema);
        addGroupList(schema);
        addGroupMember(schema);
        addFollower(schema);
        addShareJson(schema);
    }
}
