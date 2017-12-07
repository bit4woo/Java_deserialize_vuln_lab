package Step3;

abstract public class proxyTestSubject
{//抽象角色：通过接口或抽象类声明真实角色实现的业务方法。
    abstract public void request();
}

class RealSubject extends proxyTestSubject
{//真实角色：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理角色调用。
       public RealSubject()
       {
       }
      
       public void request()
       {
              System.out.println("From real subject.");
       }
}

class ProxySubject extends proxyTestSubject
{//代理角色：实现抽象角色，是真实角色的代理，通过真实角色的业务逻辑方法来实现抽象方法，并可以附加自己的操作。
    private RealSubject realSubject; //以真实角色作为代理角色的属性
      
       public ProxySubject()
       {
       }
 
       public void request() //该方法封装了真实对象的request方法
       {
        preRequest(); 
              if( realSubject == null )
        {
                     realSubject = new RealSubject();
              }
        realSubject.request(); //此处执行真实对象的request方法
        postRequest();
       }
 
    private void preRequest()
    {
        //something you want to do before requesting
    }
 
    private void postRequest()
    {
        //something you want to do after requesting
    }
}
客户端调用：
Subject sub=new ProxySubject();
Sub.request();