import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


/**
 * Created by wujianlong on 2017/3/3.
 */
public class StudentsTest {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        //创建配置对象
        Configuration config = new Configuration().configure();
        //创建服务注册对象  在hibernate5.0.2中buildServiceRegistry方法被替换掉了我们使用如下方式去创建ServiceRegistry对象
        ServiceRegistry serviceRegistry= new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        //ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
        //创建会话工厂对象
        sessionFactory = config.buildSessionFactory(serviceRegistry);
        //会话对象
        session = sessionFactory.openSession();
        //开启事务
        transaction = session.beginTransaction();
    }

    @After
    public void destory() {
        transaction.commit(); //提交事务
        session.close(); //关闭会话
        sessionFactory.close(); //关闭会话工厂
    }

    @Test
    public void testSaveStudents() {
        //生成学生对象
        Students s = new Students(1, "张三丰");
        session.save(s); //保存对象进入数据库
    }
}
