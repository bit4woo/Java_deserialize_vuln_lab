package Step3;

/*
 * 代理模式的简单demo，静态代理
 */

public class proxyTest{
	public static void main(String[] args) {
		Subject sub=new ProxySubject();
		sub.request();
	}
}

abstract class Subject
{//抽象角色：通过接口或抽象类声明真实角色实现的业务方法。
	//类比网络代理，比如http代理，都支持http协议
    abstract void request();
}

class RealSubject extends Subject
{//真实角色：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理角色调用。
	//类比真实的http请求
       public RealSubject()
       {
       }
      
       public void request()
       {
              System.out.println("From real subject.");
       }
}

class ProxySubject extends Subject//关键是类的继承
{//代理角色：实现抽象角色，是真实角色的代理，通过真实角色的业务逻辑方法来实现抽象方法，并可以附加自己的操作。
	//类比通过代理发出http请求，这个代理当然可以对http请求做出任何想要的修改。
    private RealSubject realSubject; //以真实角色作为代理角色的属性
      
       public ProxySubject()
       {
       }
 
       public void request() //该方法封装了真实对象的request方法
       {//所谓的“控制”就体现在这里
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
    	System.out.println("Do something before requesting");
    }
 
    private void postRequest()
    {
        //something you want to do after requesting
    	System.out.println("Do something after requesting");
    }
}
