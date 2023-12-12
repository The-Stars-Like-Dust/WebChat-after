package com.example.springbootchat.repository;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static java.lang.Thread.sleep;

@Repository
public class SessionRepository {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public SqlSession getSqlSession() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

    public <T> T getMapper(Class<T> type) {
        SqlSession sqlSession = getSqlSession();
        T mapper = sqlSession.getMapper(type);
        new Thread(() -> {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sqlSession.close();
        }).start();
        return mapper;
    }

}
