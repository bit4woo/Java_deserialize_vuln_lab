package Step2;

import java.lang.reflect.Method;

public class reflectionTest {
	
	public static void main(String[] args){

		Method[] methods = test.class.getMethods();
		//获取类的方法二，有点类似python的getattr()。java中每个类型都有class 属性
		//通过类的class属性获取对应的Class类的对象，通过这个Class类的对象获取test类中的方法集合

		/* String name = test.class.getName();
		 * int modifiers = test.class.getModifiers();
		 * .....还有很多方法
		 * 也就是说，对于一个任意的可以访问到的类，我们都能够通过以上这些方法来知道它的所有的方法和属性；
		 * 知道了它的方法和属性，就可以调用这些方法和属性。
		 */
		
		//调用test类中的方法
		for(Method method : methods){
		    if(method.getName().equals("int2string")) {
		    	System.out.println("method = " + method.getName());
		    	
		    	Class[] parameterTypes = method.getParameterTypes();//获取方法的参数
		    	Class returnType = method.getReturnType();//获取方法的返回类型
		    	try {
		    		//method.invoke(test.class.newInstance(), 666);
		    		Object x = method.invoke(new test(1), 666);
		    		System.out.println(x);
		    		// new关键字能调用任何构造方法。 newInstance()只能调用无参构造方法。
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}

		try {
			Method method = test.class.getMethod("int2string", Integer.class);
			Object x = method.invoke(new test(2), 666);//第一个参数是类的对象。第二参数是函数的参数
			System.out.println(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class test{
	private Integer n;  
    
    public test(Integer n){ //构造函数，初始化时执行
    	this.n = n;
    }
    public String int2string(Integer n) {
    	System.out.println("here");
    	return Integer.toString(n);
    }
}