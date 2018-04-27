package com.gupao.vip.mic.dubbo.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
public class JmsSender {

    public static void main(String[] args) {
        //表示我们当前启动的activeMQ的实例就是一个Blocker     "tcp://192.168.159.132:61616"
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("" +
                "tcp://192.168.159.136:61616");
        Connection connection=null;
        try {
            //创建连接
            connection=connectionFactory.createConnection();
            connection.start();

            //通过连接创建一个事务性会话
//            Session session=connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //通过连接创建一个非事务性会话
            /*
            消息何时被确认取决于创建会话时候的应答模式,一共有三种消息应答模式
            AUTO_ACKNOWLEDGE   表示自动确认签收
            CLIENT_ACKNOWLEDGE  当设置成这个时候  必须在  client端调用  textMessage.acknowledge(); 这个方法之后客户端才能才消息队列中把消息删除，否则一直可以收到这个消息
                            客户端通过调用  textMessage.acknowledge()进行消息的确认.在这种模式中,如果一个消息消费了10条消息，
            DUPS_OK_ACKNOWLEDGE
            */
//            Session session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);  //只有当第一个参数设置为false 的时候，后面的第二个参数才会生效
            Session session = connection.createSession(Boolean.FALSE,Session.CLIENT_ACKNOWLEDGE);


            //创建队列（如果队列已经存在则不会创建， first-queue是队列名称）
            //destination表示目的地
            Destination destination=session.createQueue("first-queue");

            //Destination destination = session.createTopic("first-topic");

            //创建消息发送者
            MessageProducer producer=session.createProducer(destination);

            TextMessage textMessage=session.createTextMessage("hello, 您好");  //创建一个 textMessage
            textMessage.setStringProperty("key","value");  //textMessage  当中可以设置很多的属性
            producer.send(textMessage);
//            session.commit();   //发送端的  commit 表示的是 只有commit之后消息才会被发送到activeMQ当中，需要和前面的  Boolean.TRUE进行配合使用
            session.close();

            System.out.println("发送完毕消息!!");
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
