package Step6EvilClass;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

public class  evilClassTest{
    public static void main(String[] argv){
        try {
			//触发方式1
        	Class<?> xxx = Class.forName("EvilClasses.evilClazz");//只会执行静态代码块中的命令。
        	evilClazz cs = (evilClazz)xxx.newInstance();//这里触发构造函数中的命令。
        	
        	Class<?> yyy = Class.forName("EvilClasses.evilClazz",true,ClassLoader.getSystemClassLoader());
        	//只会执行静态代码块中的命令,但它可以执行指定类加载器，更为灵活
        	evilClazz cs1 = (evilClazz)yyy.newInstance();//这里触发构造函数中的命令。
        	
        	
        	//触发方式2
    		Class<?> c1 = ClassLoader.getSystemClassLoader().loadClass("EvilClasses.evilClazz"); //这里不会触发静态代码块，因为是隐式加载方式。
    		c1.newInstance();//这里会触发静态代码块后，触发构造函数
			
    		
			//触发方式3，应该和方式2本质上是一样的！
        	new evilClazz();   //会执行静态代码块和构造函数中的命令
        	
        	
        	//触发方式4
        	new evilClazz().fun();//静态代码，构造函数，自定义函数都会触发。函数调用方式，不用多说
        	
        	//凡是能触发构造函数中代码的方法，都能触发静态代码块中的代码；凡是能触发自定义动态函数中代码的方法，都能触发静态代码块中的方法。
        	
        	//getObjectInstance()函数的代码如何触发？和web框架环境有关系，需要学习！！
        	
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

class evilClazz implements ObjectFactory{
	public static String aaa;
	
	//静态代码块命令执行
    static
    {
        try {
            Runtime.getRuntime().exec("explorer.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	//构造函数命令执行
    evilClazz(){
        try{
            Runtime.getRuntime().exec("calc");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //自定义函数
    public void fun() {
        try{
            Runtime.getRuntime().exec("notepad.exe");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //getObjectInstance命令执行,是因为实现了ObjectFactory接口。
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) {
    	try {
			Runtime.getRuntime().exec("mstsc.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
}
