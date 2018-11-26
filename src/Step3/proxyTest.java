package Step3;

/*
 * 代理模式的简单demo，静态代理
 * 
 * 代理的使用场景：某程序员入职公司接手了一个项目，他读源码发现某些地方可以增强（比如在某些函数执行前应该打印日志）。
 * 如果他直接在原始代码的基础上直接修改容易出错，他的做法是：自己实现一个类，和原始类实现相同的接口（或者继承相同的类），
 * 通过在方法中引用老程序的方法来实现自己的方法，从而实现增强方法的目的。
 */

public class proxyTest{
	public static void main(String[] args) {
		//Subject sub = new RealSubject();//场景中得旧代码，老程序员写的。
		Subject sub = new ProxySubject();//新入职的程序员，自己实现了ProxySubject类，然后改成了这句。来增强老程序的代码。
		sub.request();
	}
}

abstract class Subject//也可以是接口interface
{//抽象角色：通过接口或抽象类声明真实角色实现的业务方法。
	//类比网络代理，比如http代理，都支持http协议
    abstract void request();
}

//老程序员写的代码，实现了需要的主要功能。
class RealSubject extends Subject
{//真实角色：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理角色调用。
	//类比真实的http请求
       public RealSubject()//默认构造方法
       {
       }
       
       @Override
       public void request()
       {
              System.out.println("From real subject.");
       }
}

//新入职程序员实现的类，目的是增强老程序员的代码。
class ProxySubject extends Subject//关键是类的继承。
{//代理角色：实现抽象角色，是真实角色的代理，通过真实角色的业务逻辑方法来实现抽象方法，并可以附加自己的操作。
	//类比通过代理发出http请求，这个代理当然可以对http请求做出任何想要的修改。
    private RealSubject realSubject; //以真实角色作为代理角色的属性
      
       public ProxySubject()
       {
       }
       
       @Override
       public void request() //该方法封装了真实对象的request方法，老程序员的方法。
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
        //在请求前做某些处理，比如打印日志，修改请求包等等
    	System.out.println("Do something before requesting: print log,change request");
    }
 
    private void postRequest()
    {
        //在请求后做某些处理，打印日志
    	System.out.println("Do something after requesting: print log");
    }
}
