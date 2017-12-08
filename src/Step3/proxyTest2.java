package Step3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
 * 代理模式的简单demo，动态代理，动态代理利用了反射机制
 * 每一个动态代理类都会有一个与之关联的invocation handler。真正的调用是在invocation handler的invoke()方法里完成的。
 * 感谢蝶离飞、廖新喜2为师傅的指导
 */

public class proxyTest2{
	public static void main(String[] args) {
		DynamicSubject sub=new RealDynamicSubject();//之前这里sub的类型是RealDynamicSubject，不对；但是为什么呢？
		Handler handler = new Handler(sub);
		DynamicSubject sub2 = (DynamicSubject)Proxy.newProxyInstance(DynamicSubject.class.getClassLoader(), new Class[]{DynamicSubject.class}, handler); 
		//DynamicSubject sub2 = (DynamicSubject)Proxy.newProxyInstance(DynamicSubject.class.getClassLoader(), DynamicSubject.class.getInterfaces(), handler);
		//DynamicSubject.class.getInterfaces() 和 new Class[]{DynamicSubject.class}的区别是啥？
		//CLassLoader loader:指定动态代理类的类加载器
		//Class<?> interfaces:指定动态代理类需要实现的所有接口
		//InvocationHandler h: 指定与动态代理类关联的 InvocationHandler对象
		sub2.request();
	}
}

interface DynamicSubject
{//抽象角色：通过接口或抽象类声明真实角色实现的业务方法。注意:动态代理只能是接口，否则代理类转成该类型事会报错
	//类比网络代理，比如http代理，都支持http协议
    abstract void request();
}

class RealDynamicSubject implements DynamicSubject
{//真实角色：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理handler处理调用。
	//类比真实的http请求
       public RealDynamicSubject()
       {
       }
      
       public void request()
       {
              System.out.println("From real subject.");
       }
}
 
/**
 * 处理器
 */
class Handler implements InvocationHandler{
	private Object obj; //被代理的对象，不管对象是什么类型；之前声明成RealDynamicSubject，不应该这么做
    /**
     * 所有的流程控制都在invoke方法中
     * proxy：代理类
     * method：正在调用的方法
     * args：方法的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {//接口必须实现的方法，也是逻辑核心
    	System.out.println("Do something before requesting");
    	Object xxx = method.invoke(this.obj, args);
        System.out.println("Do something after requesting");
        return xxx;
    }
    public Handler(Object obj) {
    	//构造函数，把真实角色的实例传递进来,这个代理handler的目的就是处理它
        this.obj = obj;
    }
}
