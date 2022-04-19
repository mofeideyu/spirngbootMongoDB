package com.mongo;

import com.mongo.entity.User;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MongoDbApplicationTests {
    @Resource
    private MongoTemplate mongoTemplate;

    /**创建集合*/
    @Test
    void createCollection() {
        if (!mongoTemplate.collectionExists("user")) {
            mongoTemplate.createCollection("user");
        }
    }

    /**删除集合*/
    @Test
    void dropCollection() {
        mongoTemplate.dropCollection("user");
    }


    /**文档操作-> 添加*/
    @Test
    void insertDocument() {
        User user = new User(4, "thinking", 150.0, new Date());
//        mongoTemplate.insert(user);
        mongoTemplate.save(user);
    }

    /**文档操作 -> 查询*/
    @Test
    void selectDocument() {
        List<User> allUsers = mongoTemplate.findAll(User.class);
//        allUsers.forEach(System.out::println);

        User user = mongoTemplate.findById(1, User.class);
//        System.out.println(user);

        /**条件查询*/
        List<User> users = mongoTemplate.find(new Query(), User.class);
//        users.forEach(System.out::println);

        /**等值查询*/
        List<User> list1 = mongoTemplate.find(Query.query(Criteria.where("name").is("mofei")), User.class);
//        list1.forEach(System.out::println);

        /** >  <  >=  <= </>*/
        List<User> usersList = mongoTemplate.find(Query.query(Criteria.where("salary").gte(150.0)), User.class);
//        System.out.println(usersList);

        /**and查询*/
        List<User> andSelect = mongoTemplate.find(Query.query(Criteria.where("name").is("pika").and("salary").gte(172)), User.class);
//        System.out.println(andSelect);

        /**or查询*/
//        Criteria criteria = new Criteria();
//        Criteria criteria1 = criteria.orOperator(Criteria.where("name").is("pika"), Criteria.where("salary").gte(180));
        List<User> orSelect = mongoTemplate.find(Query.query(new Criteria().orOperator(Criteria.where("name").is("mofei"), Criteria.where("salary").gte(180))), User.class);
//        System.out.println(orSelect);

        /**and和or同时使用，先满足and后添加or -> 在and的的查询基础上找出符合or条件的数据*/
        List<User> userList = mongoTemplate.find(Query.query(Criteria.where("name").is("mofei").orOperator(Criteria.where("salary").gte(173))), User.class);
//        System.out.println(userList);

        /**排序*/
        List<User> salaryDesc = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("salary"))), User.class);
//        System.out.println(salaryDesc);

        /**分页1*/
//        List<User> salaryPage = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("salary"))).with(Pageable.ofSize(0)), User.class);
//        System.out.println(salaryPage);

        /**分页2 每页两条，第二页*/
        List<User> salaryPage2 = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("salary"))).skip(2).limit(2), User.class);
//        salaryPage2.forEach(System.out::println);

        /**总条数*/
        long count = mongoTemplate.count(new Query(), User.class);
//        System.out.println(count);

        /**去重
         * 参数一：查询条件
         * 参数二：对哪个字段进行去重
         * 参数三：集合类型
         * 参数四：返回结果类型
         */
        List<String> nameDistinctList = mongoTemplate.findDistinct(new Query(), "name", User.class, String.class);
        System.out.println(nameDistinctList);

        /**使用json字符串方式查询*/
        BasicQuery query = new BasicQuery("{name:'mofei'}");
        List<User> basicQuery = mongoTemplate.find(query, User.class);
        System.out.println(basicQuery);
    }


    /**文档操作 -> 更新*/
    @Test
    public void update() {
        Update update = new Update();
        update.set("salary",2300);
        /**更新符合条件的第一条语句*/
//        mongoTemplate.updateFirst(Query.query(Criteria.where("name").is("thinking")), update, User.class);

        /**更新多条*/
//        mongoTemplate.updateMulti(Query.query(Criteria.where("name").is("mofei")), update, User.class);

        /**没有符合条件数据则插入数据*/
        UpdateResult upsert = mongoTemplate.upsert(Query.query(Criteria.where("name").is("Eng")), update, User.class);
        System.out.println(upsert.getModifiedCount());
        System.out.println(upsert.getMatchedCount());
        System.out.println(upsert.getUpsertedId());
    }


    @Test
    public void remove() {
        /**根据条件删除*/
        mongoTemplate.remove(Query.query(Criteria.where("name").is("Eng")),User.class);

        /**删除所有*/
//        mongoTemplate.remove(new Query(),User.class);
    }

}
